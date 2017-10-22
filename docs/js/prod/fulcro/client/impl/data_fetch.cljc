(ns fulcro.client.impl.data-fetch
  (:require [om.next.impl.parser :as op]
            [om.next :as om]
            [om.next.protocols :as omp]
            [om.util :as util]
            [clojure.walk :refer [prewalk]]
            [clojure.set :as set]
            [fulcro.client.logging :as log]
            [fulcro.client.util :refer [force-render]]
            [fulcro.client.mutations :as m]
            [fulcro.client.impl.om-plumbing :as plumbing]))

(declare data-remote data-target data-path data-uuid data-field data-query-key data-query set-loading! full-query loaded-callback error-callback data-marker?)
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Implementation for public api
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; Built-in mutation for adding a remote query to the network requests.
(defn data-state?
  "Test if the given bit of state is a data fetch state-tracking marker"
  [state] (contains? state ::type))

(letfn [(is-kind? [state type]
          (if (data-state? state)
            (= type (::type state))
            false))]
  (defn ready?
    "Test if the given item is a data state marker that is in the ready state"
    [state] (is-kind? state :ready))
  (defn loading?
    "Test if the given item is a data state marker in the loading state"
    [state] (is-kind? state :loading))
  (defn failed?
    "Test if the given item is a data state marker in the failed state"
    [state] (is-kind? state :failed)))

(defn is-direct-table-load? [load-marker]
  (and
    (not (data-field load-marker))
    (util/ident? (data-query-key load-marker))))


(defn- place-load-marker [state-map marker]
  (update-in state-map (data-path marker)
    (fn [current-val]
      (if (is-direct-table-load? marker)
        (when (map? current-val) (assoc current-val :ui/fetch-state marker))
        {:ui/fetch-state marker}))))

(defn- place-load-markers
  "Place load markers in the app state at their data paths so that UI rendering can see them."
  [state-atom items-to-load]
  (swap! state-atom
    (fn [state-map]
      (reduce (fn [s item]
                (let [i (set-loading! item)]
                  (cond-> (update s :fulcro/loads-in-progress (fnil conj #{}) (data-uuid i))
                    (data-marker? i) (place-load-marker i))))
        state-map items-to-load))))

(defn mark-parallel-loading
  "Marks all of the items in the ready-to-load state as loading, places the loading markers in the appropriate locations
  in the app state, and return maps with the keys:

  `query` : The full query to send to the server.
  `on-load` : The function to call to merge a response. Detects missing data and sets failure markers for those.
  `on-error` : The function to call to set network/server error(s) in place of loading markers.
  `load-descriptors` : Args to pass back to on-load and on-error. These are separated
    so that `rewrite-tempids-in-request-queue` can rewrite tempids for merge and
    error callbacks

  response-channel will have the response posted to it when the request is done.
  ."
  [remote-name reconciler]
  (let [state                (om/app-state reconciler)
        queued-items         (get @state :fulcro/ready-to-load)
        is-eligible?         (fn [item] (and (::parallel item) (= remote-name (data-remote item))))
        other-items-loading? (boolean (seq (get @state :fulcro/loads-in-progress)))
        items-to-load        (filter is-eligible? queued-items)
        remaining-items      (filter (comp not is-eligible?) queued-items)
        loading?             (or (seq items-to-load) other-items-loading?)]
    (when-not (empty? items-to-load)
      (place-load-markers state items-to-load)
      (swap! state assoc :ui/loading-data loading? :fulcro/ready-to-load remaining-items)
      (for [item items-to-load]
        {:query            (full-query [item])
         :on-load          (loaded-callback reconciler)
         :on-error         (error-callback reconciler)
         :load-descriptors [item]}))))

(defn dedupe-by
  "Returns a lazy sequence of the elements of coll with dupes removed.
   An element is a duplicate IFF (keys-fn element) has key collision with any prior element
   to come before it. E.g. (dedupe-by identity [[:a] [:b] [:a] [:a :c]]) => [[:a] [:b]]
   Returns a stateful transducer when no collection is provided."
  ([keys-fn]                                                ;; transducer fn
   (fn [rf]
     (let [keys-seen (volatile! #{})]
       (fn
         ([] (rf))
         ([result] (rf result))
         ([result input]
          (let [input-keys (set (keys-fn input))]
            ;; if no keys seen, include input in the reduction
            (if (empty? (set/intersection @keys-seen input-keys))
              (do (vswap! keys-seen set/union input-keys)
                  (rf result input))
              result)))))))
  ([keys-fn coll] (sequence (dedupe-by keys-fn) coll)))

(defn join-key-or-nil [expr]
  (when (util/join? expr)
    (let [join-key-or-ident (util/join-key expr)]
      (if (util/ident? join-key-or-ident)
        (first join-key-or-ident)
        join-key-or-ident))))

(defn split-items-ready-to-load
  "This function is used to split accidental colliding queries into separate network
  requests. The most general description of this issue is
  from two unrelated `load` calls when black-box composing functions. The two
  separate queries: One issues `[{:entitlements [:foo]}]`, and the other
  asks for `[{:entitlements [:bar]}]`. Fulcro merges these into a single query
  [{:entitlements [:foo]} {:entitlements [:bar]}]. However, the response to a query
  is a map, and such a query would result in the backend parser being called twice (once per key in the subquery)
  but one would stomp on the other. Thus, this function ensures such accidental collisions are
  not combined into a single network request."
  [items-ready-to-load]
  (let [items-to-load-now (->> items-ready-to-load
                            (dedupe-by (fn [item]
                                         (->> (data-query item)
                                           (map join-key-or-nil))))
                            vec)
        is-loading-now?   (set items-to-load-now)
        items-to-defer    (->> items-ready-to-load
                            (remove is-loading-now?)
                            (vec))]
    [items-to-load-now items-to-defer]))

(defn mark-loading
  "Marks all of the items in the ready-to-load state as loading, places the loading markers in the appropriate locations
  in the app state, and returns a map with the keys:

  `query` : The full query to send to the server.
  `on-load` : The function to call to merge a response. Detects missing data and sets failure markers for those.
  `on-error` : The function to call to set network/server error(s) in place of loading markers.
  `load-descriptors` : Args to pass back to on-load and on-error. These are separated
    so that `rewrite-tempids-in-request-queue` can rewrite tempids for merge and
    error callbacks

  response-channel will have the response posted to it when the request is done.
  ."
  [remote reconciler]
  (let [state                   (om/app-state reconciler)
        is-eligible?            (fn [item] (= remote (data-remote item)))
        all-items               (get @state :fulcro/ready-to-load)
        items-ready-to-load     (filter is-eligible? all-items)
        items-for-other-remotes (filter (comp not is-eligible?) all-items)
        other-items-loading?    (boolean (seq (get @state :fulcro/loads-in-progress)))
        [items-to-load-now items-to-defer] (split-items-ready-to-load items-ready-to-load)
        remaining-items         (concat items-for-other-remotes items-to-defer)
        loading?                (or (seq items-to-load-now) other-items-loading?)]
    (when-not (empty? items-to-load-now)
      (place-load-markers state items-to-load-now)
      (swap! state assoc :ui/loading-data loading? :fulcro/ready-to-load remaining-items)
      {:query            (full-query items-to-load-now)
       :on-load          (loaded-callback reconciler)
       :on-error         (error-callback reconciler)
       :load-descriptors items-to-load-now})))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Testing API, used to write tests against specific data states
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; not-present represented by nil
;; ok represented by data
(def valid-types #{:ready :loading :failed})

(defn make-data-state
  "This is just a testing function -- using ready-state as public interface and call the
  `set-{type}!` functions to change it as needed."
  ([type]
   (make-data-state type {}))

  ([type params]
   (if (get valid-types type)
     {::type type ::params params}
     (throw (ex-info (str "INVALID DATA STATE TYPE: " type) {})))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Helpers -- not intended for public use
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn elide-ast-nodes
  "Remove items from a query (AST) that have a key listed in the elision-set"
  [{:keys [key union-key children] :as ast} elision-set]
  (let [union-elision? (contains? elision-set union-key)]
    (when-not (or union-elision? (contains? elision-set key))
      (when (and union-elision? (<= (count children) 2))
        (log/warn "Om unions are not designed to be used with fewer than two children. Check your calls to Fulcro
        load functions where the :without set contains " (pr-str union-key)))
      (update ast :children (fn [c] (vec (keep #(elide-ast-nodes % elision-set) c)))))))

(defn inject-query-params
  "Inject parameters into elements of the top-level query.

  `params` is a map from keyword (on the query in the AST) to parameter maps. So, given the AST for this query:

  ```
  [:a :b :c]
  ```

  and a `params` of `{:a {:x 1} :c {:y 2}}` you'll get an AST representing:

  ```
  [(:a {:x 1}) :b (:c {:y 2})]
  ```
  "
  [ast params]
  (let [top-level-keys (set (map :dispatch-key (:children ast)))
        param-keys     (set (keys params))
        unknown-keys   (set/difference param-keys top-level-keys)]
    (when (not (empty? unknown-keys))
      (log/error (str "Error: You attempted to add parameters for " (pr-str unknown-keys) " to top-level key(s) of " (pr-str (om/ast->query ast)))))
    (update-in ast [:children] #(map (fn [c] (if-let [new-params (get params (:dispatch-key c))]
                                               (update c :params merge new-params)
                                               c)) %))))


(defn ready-state
  "Generate a ready-to-load state with all of the necessary details to do
  remoting and merging."
  [{:keys [ident field params remote without query post-mutation post-mutation-params fallback parallel refresh marker target env]
    :or   {remote :remote without #{} refresh [] marker true}}]
  (assert (or field query) "You must supply a query or a field/ident pair")
  (assert (or (not field) (and field (util/ident? ident))) "Field requires ident")
  (let [old-ast     (om/query->ast query)
        ast         (cond-> old-ast
                      (not-empty without) (elide-ast-nodes without)
                      params (inject-query-params params))
        query-field (first query)
        key         (if (util/join? query-field) (util/join-key query-field) query-field)
        query'      (om/ast->query ast)]
    (assert (or (not field) (= field key)) "Component fetch query does not match supplied field.")
    {::type                 :ready
     ::uuid                 #?(:cljs (str (cljs.core/random-uuid))
                               :clj  (str (System/currentTimeMillis)))
     ::target               target
     ::remote               remote
     ::ident                ident                           ; only for component-targeted loads
     ::field                field                           ; for component-targeted load
     ::query                query'                          ; query, relative to root of db OR component
     ::post-mutation        post-mutation
     ::post-mutation-params post-mutation-params
     ::refresh              refresh
     ::marker               marker
     ::parallel             parallel
     ::fallback             fallback
     ; stored on metadata so it doesn't interfere with serializability (this marker ends up in state)
     ::original-env         (with-meta {} env)}))

(defn mark-ready
  "Place a ready-to-load marker into the application state. This should be done from
  a mutate function that is abstractly loading something. This is intended for internal use.

  See the `load` and `load-field` functions in `fulcro.client.data-fetch` for the public API."
  [{:keys [env] :as config}]
  (let [state        (get env :state)
        marker?      (not (identical? false (:marker config)))
        load-request (ready-state (merge {:marker true :refresh [] :without #{} :env env} config))]
    (swap! state (fn [s]
                   (cond-> (update s :fulcro/ready-to-load (fnil conj []) load-request)
                     marker? (place-load-marker load-request))))))

(defn data-target
  "Return the ident (if any) of the component related to the query in the data state marker. An ident is required
  to be present if the marker is targeting a field."
  [state] (::target state))
(defn data-ident
  "Return the ident (if any) of the component related to the query in the data state marker. An ident is required
  to be present if the marker is targeting a field."
  [state] (::ident state))
(defn data-query
  "Get the query that will be sent to the server as a result of the given data state marker"
  [state]
  (if (data-ident state)
    [{(data-ident state) (::query state)}]
    (::query state)))
(defn data-field
  "Get the target field (if any) from the data state marker"
  [state] (::field state))
(defn data-uuid
  "Get the UUID of the data fetch"
  [state] (::uuid state))
(defn data-marker?
  "Test if the user desires a copy of the state marker to appear in the app state at the data path of the target data."
  [state] (::marker state))
(defn data-refresh
  "Get the list of query keywords that should be refreshed (re-rendered) when this load completes."
  [state] (::refresh state))
(defn data-remote
  "Get the remote that this marker is meant to talk to"
  [state] (::remote state))
(defn data-query-key
  "Get the 'primary' query key of the data fetch. This is defined as the first keyword of the overall query (which might
  be a simple prop or join key for example)"
  [state]
  (let [ast  (om/query->ast (-> state ::query))
        node (-> ast :children first)]
    (:key node)))

(defn data-path
  "Get the app-state database path of the target of the load that the given data state marker is trying to load."
  [state]
  (let [target (data-target state)]
    (cond
      (and (nil? (data-field state)) (vector? target) (not-empty target)) target
      (and (vector? (data-ident state)) (keyword? (data-field state))) (conj (data-ident state) (data-field state))
      (util/ident? (data-query-key state)) (data-query-key state)
      :otherwise [(data-query-key state)])))

(defn data-params
  "Get the parameters that the user wants to add to the first join/keyword of the data fetch query."
  [state] (::params state))

(defn data-exclusions
  "Get the keywords that should be (recursively) removed from the query that will be sent to the server."
  [state] (::without state))

;; Setters
(letfn [(set-type [state type params]
          (merge state {::type   type
                        ::params params}))]
  (defn set-ready!
    "Returns a state (based on the input state) that is in the 'ready' to load state."
    ([state] (set-ready! state nil))
    ([state params] (set-type state :ready params)))
  (defn set-loading!
    "Returns a marker (based on the input state) that is in the loading state (and ensures that it has a UUID)"
    ([state] (set-loading! state nil))
    ([state params] (let [rv (set-type state :loading params)]
                      (with-meta rv {:state rv}))))
  (defn set-failed!
    "Returns a marker (based on the input state) that is in the error state"
    ([state] (set-failed! state nil))
    ([state params]
     (set-type state :failed params))))

(defn full-query
  "Composes together the queries of a sequence of data states into a single query."
  [items] (vec (mapcat (fn [item] (data-query item)) items)))

(defn- set-global-loading [reconciler]
  "Sets the global :ui/loading-data to false if there are no loading fetch states in the entire app-state, otherwise sets to true."
  (let [state-atom (om/app-state reconciler)
        loading?   (boolean (seq (get @state-atom :fulcro/loads-in-progress)))]
    (swap! state-atom assoc :ui/loading-data loading?)))

(defn relocate-targeted-results
  "For items that are manually targeted, move them in app state from their result location to their target location."
  [state-atom items]
  (swap! state-atom
    (fn [state-map]
      (reduce (fn [state item]
                (let [default-target  (data-query-key item)
                      explicit-target (or (data-target item) [])
                      relocate?       (and
                                        (nil? (data-field item))
                                        (keyword? (data-query-key item))
                                        (not-empty explicit-target))]
                  (if relocate?
                    (let [value (get state default-target)]
                      (-> state
                        (dissoc (data-query-key item))
                        (assoc-in explicit-target value)))
                    state))) state-map items))))

(defn- remove-marker
  "Returns app-state without the load marker for the given item."
  [app-state item]
  (let [path (data-path item)
        data (get-in app-state path)]
    (cond
      (and (map? data) (= #{:ui/fetch-state} (set (keys data)))) (assoc-in app-state path nil) ; to-many (will become a vector)
      (and (map? data) (contains? data :ui/fetch-state)) (update-in app-state path dissoc :ui/fetch-state)
      :else (assoc-in app-state path nil))))

(defn callback-env
  "Build a callback env for post mutations and fallbacks"
  [reconciler load-request original-env]
  (let [state (om/app-state reconciler)
        {:keys [::target ::remote ::ident ::field ::query ::post-mutation ::post-mutation-params ::refresh ::marker ::parallel ::fallback]} load-request]
    (merge original-env
      {:state state
       :load-request
              (cond-> {:target target :remote remote :marker marker :server-query query :parallel (boolean parallel)}
                post-mutation (assoc :post-mutation post-mutation)
                post-mutation-params (assoc :post-mutation-params post-mutation-params)
                refresh (assoc :refresh refresh)
                fallback (assoc :fallback fallback))})))

(defn- loaded-callback
  "Generates a callback that processes all of the post-processing steps once a remote load has completed. This includes:

  - Marking the items that were queries for but not returned as 'missing' (see documentation on mark and sweep of db)
  - Refreshing elements of the UI that were included in the data fetch :refresh option
  - Removing loading markers related to the executed loads that were not overwritten by incoming data
  - Merging the incoming data into the normalized database
  - Running post-mutations for any fetches that completed
  - Updating the global loading marker
  - Triggering re-render for all data item refresh lists
  "
  [reconciler]
  (fn [response items]
    (let [query              (full-query items)
          loading-items      (into #{} (map set-loading! items))
          refresh-set        (into #{:ui/loading-data} (mapcat data-refresh items))
          to-refresh         (vec refresh-set)
          marked-response    (plumbing/mark-missing response query)
          app-state          (om/app-state reconciler)
          ran-mutations      (atom false)
          remove-markers     (fn [] (doseq [item loading-items]
                                      (swap! app-state (fn [s]
                                                         (cond-> s
                                                           :always (update :fulcro/loads-in-progress disj (data-uuid item))
                                                           (data-marker? item) (remove-marker item))))))
          run-post-mutations (fn [] (doseq [item loading-items]
                                      (when-let [mutation-symbol (::post-mutation item)]
                                        (reset! ran-mutations true)
                                        (let [params       (or (::post-mutation-params item) {})
                                              original-env (-> item ::original-env meta)]
                                          (some-> (m/mutate (callback-env reconciler item original-env) mutation-symbol params)
                                            :action
                                            (apply []))))))]
      (remove-markers)
      (om/merge! reconciler marked-response query)
      (relocate-targeted-results app-state loading-items)
      (run-post-mutations)
      (set-global-loading reconciler)
      (if (contains? refresh-set :fulcro/force-root)
        (om/force-root-render! reconciler)
        (force-render reconciler to-refresh)))))

(defn- error-callback
  "Generates a callback that is used whenever a hard server error occurs (status code 400+ or network error).

  The generated callback:

  - Replaces affected loading markers with error markers (if :marker is true on the load item)
  - Runs fallbacks associated with the loads
  - Sets the global error marker (:fulcro/server-error)
  - Refreshes UI (from root if there were fallbacks)
  "
  [reconciler]
  (fn [error items]
    (let [loading-items (into #{} (map set-loading! items))
          app-state     (om/app-state reconciler)
          refresh-set   (into #{:ui/loading-data} (mapcat data-refresh items))
          to-refresh    (vec refresh-set)
          ran-fallbacks (atom false)
          mark-errors   (fn []
                          (swap! app-state assoc :fulcro/server-error error)
                          (doseq [item loading-items]
                            (swap! app-state (fn [s]
                                               (cond-> s
                                                 (data-marker? item) (update-in (conj (data-path item) :ui/fetch-state) set-failed! error)
                                                 :always (update :fulcro/loads-in-progress disj (data-uuid item)))))))
          run-fallbacks (fn [] (doseq [item loading-items]
                                 (when-let [fallback-symbol (::fallback item)]
                                   (let [original-env (-> item ::original-env meta)
                                         env          (callback-env reconciler item original-env)]
                                     (reset! ran-fallbacks true)
                                     (some->
                                       (m/mutate env fallback-symbol {:error error})
                                       :action
                                       (apply []))))))]
      (mark-errors)
      (run-fallbacks)
      (set-global-loading reconciler)
      (om/force-root-render! reconciler))))

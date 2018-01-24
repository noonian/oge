(ns fulcro.client.impl.application
  (:require [fulcro.client.logging :as log]
            [fulcro.client.primitives :as prim]
            [fulcro.i18n :as i18n]
            [fulcro.client.mutations :as m]
            [fulcro.history :as hist]
            [fulcro.client.impl.data-fetch :as f]
            [fulcro.util :as futil]
            [fulcro.client.util :as util]
    #?(:clj
            [clojure.future :refer :all])
            [clojure.spec.alpha :as s]
    #?(:cljs [cljs.core.async :as async]
       :clj
            [clojure.core.async :as async :refer [go]])
            [fulcro.client.network :as net]
            [fulcro.client.impl.protocols :as p])
  #?(:cljs (:require-macros
             [cljs.core.async.macros :refer [go]])))

(defn fallback-handler
  "This internal function is responsible for generating and returning a function that can accomplish calling the fallbacks that
  appear in an incoming transaction, which is in turn used by the error-handling logic of the plumbing."
  [{:keys [reconciler]} query]
  (fn [error]
    (swap! (prim/app-state reconciler) assoc :fulcro/server-error error)
    (if-let [q (prim/fallback-tx query error)]
      (do (log/warn (log/value-message "Transaction failed. Running fallback." q))
          (prim/transact! reconciler q))
      (log/warn "Fallback triggered, but no fallbacks were defined."))))

;; this is here so we can do testing (can mock core async stuff out of the way)
(defn- enqueue
  "Enqueue a send to the network queue. This is a standalone function because we cannot mock core async functions."
  [q v]
  (go (async/>! q v)))

(s/fdef enqueue
  :args (s/cat :queue any? :payload ::f/payload))

(defn real-send
  "Do a properly-plumbed network send. This function recursively strips ui attributes from the tx and pushes the tx over
  the network. It installs the given on-load and on-error handlers to deal with the network response."
  [net tx on-done on-error on-load]
  ; server-side rendering doesn't do networking. Don't care.
  (if #?(:clj  false
         :cljs (implements? net/ProgressiveTransfer net))
    (net/updating-send net (prim/strip-ui tx) on-done on-error on-load)
    (net/send net (prim/strip-ui tx) on-done on-error)))

(defn split-mutations
  "Split a tx that contains mutations. Returns a vector that contains at least one tx (the original).

   Examples:
   [(f) (g)] => [[(f) (g)]]
   [(f) (g) (f) (k)] => [[(f) (g)] [(f) (k)]]
   [(f) (g) (f) (k) (g)] => [[(f) (g)] [(f) (k) (g)]]
   "
  [tx]
  (if-not (and (vector? tx) (every? (fn [t] (or (futil/mutation-join? t) (and (list? t) (symbol? (first t))))) tx))
    (do
      (log/error "INTERNAL ERROR: split-mutations was asked to split a tx that contained things other than mutations." tx)
      [tx])
    (if (empty? tx)
      []
      (let [mutation-name (fn [m]
                            (cond
                              (list? m) (first m)
                              (futil/mutation-join? m) (-> m keys ffirst)
                              :otherwise 'unrecongnized-mutation))
            {:keys [accumulator current-tx]}
            (reduce (fn [{:keys [seen accumulator current-tx]} mutation]
                      (if (contains? seen (mutation-name mutation))
                        {:seen #{} :accumulator (conj accumulator current-tx) :current-tx [mutation]}
                        {:seen        (conj seen (mutation-name mutation))
                         :accumulator accumulator
                         :current-tx  (conj current-tx mutation)})) {:seen #{} :accumulator [] :current-tx []} tx)]
        (conj accumulator current-tx)))))

(defn enqueue-mutations
  "Splits out the (remote) mutations and fallbacks in a transaction, creates an error handler that can
   trigger fallbacks, and enqueues the remote mutations on the network queue. If duplicate mutation names
   appear, then they will be separated into separate network requests.

   NOTE: If the mutation in the tx has duplicates, then the same fallback will be used for the
   resulting split tx. See `split-mutations` (which is used by this function to split dupes out of txes)."
  [{:keys [reconciler send-queues] :as app} remote-tx-map cb]
  ; NOTE: for history navigation we need to track the time at which the mutation was submitted. If we roll back, we want the db-before of that tx-time.
  (let [history (prim/get-history reconciler)]
    (doseq [remote (keys remote-tx-map)]
      (let [queue                    (get send-queues remote)
            full-remote-transaction  (get remote-tx-map remote)
            refresh-set              (or (some-> full-remote-transaction meta ::prim/refresh vec) [])
            tx-time                  (some-> full-remote-transaction meta ::hist/tx-time)
            ; IMPORTANT: fallbacks claim to be on every remote (otherwise we have to make much more complicated logic about
            ; the tx submission in transact and parsing). So, you will get a fallback handler for every **defined** remote.
            ; The remove-loads-and-fallbacks will return an empty list if all there are is fallbacks, keeping us from submitting
            ; a tx that contains only fallbacks.
            fallback                 (fallback-handler app full-remote-transaction)
            desired-remote-mutations (prim/remove-loads-and-fallbacks full-remote-transaction)
            tx-list                  (split-mutations desired-remote-mutations)
            ; todo: split remote mutations
            has-mutations?           (fn [tx] (> (count tx) 0))
            payload                  (fn [tx]
                                       {::prim/query        tx
                                        ::hist/tx-time      tx-time
                                        ::hist/history-atom history
                                        ::prim/remote       remote
                                        ::f/on-load         (fn [result]
                                                              ; NOTE: We could queue refreshes that we got back
                                                              ; from the server, if we can send metadata, or something.
                                                              ; (p/queue! reconciler refresh-set remote)
                                                              (cb result tx remote))
                                        ::f/on-error        (fn [result] (fallback result))})]
        (when history
          (swap! history hist/remote-activity-started remote tx-time))
        (doseq [tx tx-list]
          (when (has-mutations? tx)
            (enqueue queue (payload tx))))))))

(defn enqueue-reads
  "Finds any loads marked `parallel` and triggers real network requests immediately. Remaining loads
  are pulled into a single fetch payload (combined into one query) and enqueued behind any prior mutations/reads that
  were already requested in a prior UI/event cycle. Thus non-parallel reads are processed in clusters grouped due to UI
  events (a single event might trigger many reads which will all go to the server as a single combined request).
  Further UI events that trigger remote interaction will end up waiting until prior network request(s) are complete.

  This ensures that default reasoning is simple and sequential in the face of optimistic UI updates (real network
  traffic characteristics could cause out of order processing, and you would not want
  a 'create list' to be processed on the server *after* an 'add an item to the list'). "
  [{:keys [send-queues reconciler networking]}]
  (doseq [remote (keys send-queues)]
    (let [queue            (get send-queues remote)
          network          (get networking remote)
          parallel-payload (f/mark-parallel-loading! remote reconciler)]
      (doseq [{:keys [::prim/query ::f/on-load ::f/on-error ::f/load-descriptors] :as payload} parallel-payload]
        (let [on-load'  #(on-load % load-descriptors)
              on-error' #(on-error % load-descriptors)]
          ; TODO: queries cannot report progress, yet. Could update the payload marker in app state.
          ; FIXME: History activity tracking!
          (real-send network query on-load' on-error' nil)))
      (loop [fetch-payload (f/mark-loading remote reconciler)]
        (when fetch-payload
          (enqueue queue (assoc fetch-payload :networking network))
          (recur (f/mark-loading remote reconciler)))))))

(defn detect-errant-remotes [{:keys [reconciler send-queues] :as app}]
  (let [state           (prim/app-state reconciler)
        all-items       (get @state :fulcro/ready-to-load)
        item-remotes    (into #{} (map f/data-remote all-items))
        all-remotes     (set (keys send-queues))
        invalid-remotes (clojure.set/difference item-remotes all-remotes)]
    (when (not-empty invalid-remotes) (log/error (str "Use of invalid remote(s) detected! " invalid-remotes)))))

(defn server-send
  "Puts queries/mutations (and their corresponding callbacks) onto the send queue. The networking code will pull these
  off one at a time and send them through the real networking layer. Reads are guaranteed to *follow* writes."
  [app remote-tx-map merge-result-callback]
  (detect-errant-remotes app)
  (enqueue-mutations app remote-tx-map merge-result-callback)
  (enqueue-reads app))

(defn- send-payload
  "Sends a network payload. There are two kinds of payloads in Fulcro. The first is
  for reads, which are tracked by load descriptors in the app state. These load descriptors
  tell the plumbing how to handle the response, and expect to only be merged in once. Mutations
  do not have a payload, and can technically receive progress updates from the network. The built-in
  networking does not (currently) give progress events, but plugin networking can. It is currently not
  supported to give an update on a load, so this function is careful to detect that a payload is a send
  and turns all but the last update into a no-op. The send-complete function comes from the
  network sequential processing loop, and when called unblocks the network processing to allow the
  next request to go. Be very careful with this code, as bugs will cause applications to stop responding
  to remote requests."
  [network payload send-complete]
  ; Note, only data-fetch reads will have load-descriptors,
  ; in which case the payload on-load is data-fetch/loaded-callback, and cannot handle updates.
  (let [{:keys [::prim/query ::f/on-load ::f/on-error ::f/load-descriptors]} payload
        merge-data (if load-descriptors #(on-load % load-descriptors) on-load)
        on-update  (if load-descriptors identity merge-data) ; TODO: queries cannot handle progress
        on-error   (if load-descriptors #(on-error % load-descriptors) on-error)
        on-error   (comp send-complete on-error)
        on-done    (comp send-complete merge-data)]
    (if (f/is-deferred-transaction? query)
      (on-done {})                                          ; immediately let the deferred tx go by pretending that the load is done
      (real-send network query on-done on-error on-update))))

(defn is-sequential? [network]
  (if (and #?(:clj false :cljs (implements? net/NetworkBehavior network)))
    (net/serialize-requests? network)
    true))

(defn start-network-sequential-processing
  "Starts a async go loop that sends network requests on a networking object's request queue. Must be called once and only
  once for each active networking object on the UI. Each iteration of the loop pulls off a
  single request, sends it, waits for the response, and then repeats. Gives the appearance of a separate networking
  'thread' using core async."
  [{:keys [networking send-queues response-channels]}]
  (doseq [remote (keys send-queues)]
    (let [queue            (get send-queues remote)
          network          (get networking remote)
          sequential?      (is-sequential? network)
          response-channel (get response-channels remote)
          send-complete    (if sequential?
                             (fn [] (go (async/>! response-channel :complete)))
                             identity)]
      (go
        (loop [payload (async/<! queue)]
          (let [{:keys [::hist/tx-time ::hist/history-atom ::prim/remote]} payload]
            (send-payload network payload send-complete)    ; async call. Calls send-complete when done
            (when sequential?
              (async/<! response-channel))                  ; block until send-complete
            (when (and history-atom tx-time)
              (swap! history-atom hist/remote-activity-finished remote tx-time))
            (recur (async/<! queue))))))))

(defn initialize-internationalization
  "Configure a re-render when the locale changes and also when the translations arrive from a module load.
   During startup this function will be called once for each reconciler that is running on a page."
  [reconciler]
  (let [app-id (p/get-id reconciler)]
    (letfn [(re-render [k r o n] #?(:cljs
                                    ; the delay is necessary because the locale change triggers the watch before
                                    ; it has affected the state of the application
                                    (when (prim/mounted? (prim/app-root reconciler))
                                      (js/setTimeout #(util/force-render reconciler) 0))))]
      (add-watch i18n/*current-locale* app-id re-render))))

(defn generate-reconciler
  "The reconciler's send method calls FulcroApplication/server-send, which itself requires a reconciler with a
  send method already defined. This creates a catch-22 / circular dependency on the reconciler and :send field within
  the reconciler.

  To resolve the issue, we def an atom pointing to the reconciler that the send method will deref each time it is
  called. This allows us to define the reconciler with a send method that, at the time of initialization, has an app
  that points to a nil reconciler. By the end of this function, the app's reconciler reference has been properly set."
  [{:keys [send-queues mutation-merge] :as app} initial-state parser {:keys [migrate] :as reconciler-options}]
  (let [rec-atom                  (atom nil)
        remotes                   (keys send-queues)
        tempid-migrate            (fn [pure _ tempids]
                                    (doseq [queue (vals send-queues)]
                                      (prim/rewrite-tempids-in-request-queue queue tempids))
                                    (let [state-migrate (or migrate prim/resolve-tempids)]
                                      (state-migrate pure tempids)))
        initial-state-with-locale (let [set-default-locale (fn [s] (update s :ui/locale (fnil identity :en)))
                                        is-atom?           (futil/atom? initial-state)
                                        incoming-locale    (get (if is-atom? @initial-state initial-state) :ui/locale)]
                                    (when incoming-locale
                                      (reset! i18n/*current-locale* incoming-locale))
                                    (if is-atom?
                                      (do
                                        (swap! initial-state set-default-locale)
                                        initial-state)
                                      (do
                                        (set-default-locale initial-state))))
        config                    (merge {}
                                    reconciler-options
                                    {:migrate     tempid-migrate
                                     :state       initial-state-with-locale
                                     :send        (fn [sends-keyed-by-remote result-merge-callback]
                                                    (server-send (assoc app :reconciler @rec-atom) sends-keyed-by-remote result-merge-callback))
                                     :normalize   true
                                     :remotes     remotes
                                     :merge-ident (fn [reconciler app-state ident props]
                                                    (update-in app-state ident (comp prim/sweep-one merge) props))
                                     :merge-tree  (fn [target source]
                                                    (prim/merge-handler mutation-merge target source))
                                     :parser      parser})
        rec                       (prim/reconciler config)]
    (reset! rec-atom rec)
    rec))

(defn initialize-global-error-callbacks
  [app]
  (doseq [remote (keys (:networking app))]
    (let [cb-atom (get-in app [:networking remote :global-error-callback])]
      (when (futil/atom? cb-atom)
        (swap! cb-atom #(if (fn? %)
                          (partial % (prim/app-state (:reconciler app)))
                          (throw (ex-info "Networking error callback must be a function." {}))))))))

(defn read-local
  "Read function for the built-in parser.

  *** NOTE: This function only runs when it is called without a target -- it is not triggered for remote reads. To
  trigger a remote read, use the `fulcro/data-fetch` namespace. ***

  If a user-read is supplied, *it will be allowed* to trigger remote reads. This is not recommended, as you
  will probably have to augment the networking layer to get it to do what you mean. Use `load` instead. You have
  been warned. Triggering remote reads is allowed, but discouraged and unsupported.

  Returns the current locale when reading the :ui/locale keyword. Otherwise pulls data out of the app-state.
  "
  [user-read {:keys [query target state ast] :as env} dkey params]
  (if-let [custom-result (user-read env dkey params)]
    custom-result
    (when (not target)
      (case dkey
        (let [top-level-prop (nil? query)
              key            (or (:key ast) dkey)
              by-ident?      (futil/ident? key)
              union?         (map? query)
              data           (if by-ident? (get-in @state key) (get @state key))]
          {:value
           (cond
             union? (get (prim/db->tree [{key query}] @state @state) key)
             top-level-prop data
             :else (prim/db->tree query data @state))})))))

(defn write-entry-point
  "This is the entry point for writes. In general this is simply a call to the multi-method
  defined by Fulcro (mutate); however, Fulcro supports the concept of a global `post-mutate`
  function that will be called anytime the general mutate has an action that is desired. This
  can be useful, for example, in cases where you have some post-processing that needs
  to happen for a given (sub)set of mutations (that perhaps you did not define)."
  [env k params]
  (let [rv     (try
                 (m/mutate env k params)
                 (catch #?(:cljs :default :clj Exception) e
                   (log/error (str "Mutation " k " failed with exception") e)
                   nil))
        action (:action rv)]
    (if action
      (assoc rv :action (fn []
                          (try
                            (let [action-result (action env k params)]
                              (try
                                (m/post-mutate env k params)
                                (catch #?(:cljs :default :clj Exception) e (log/error (str "Post mutate failed on dispatch to " k))))
                              action-result)
                            (catch #?(:cljs :default :clj Exception) e
                              (log/error (str "Mutation " k " failed with exception") e)
                              (throw e)))))
      rv)))


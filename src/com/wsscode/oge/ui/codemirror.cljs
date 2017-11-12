(ns com.wsscode.oge.ui.codemirror
  (:require [om.next :as om]
            [om.dom :as dom]
            [goog.object :as gobj]
            [com.wsscode.pathom.connect :as p.connect]
            [cljsjs.codemirror]
            [cljsjs.codemirror.mode.clojure]
            [cljsjs.codemirror.addon.dialog.dialog]
            [cljsjs.codemirror.addon.edit.matchbrackets]
            [cljsjs.codemirror.addon.edit.closebrackets]
            [cljsjs.codemirror.addon.fold.foldcode]
            [cljsjs.codemirror.addon.fold.foldgutter]
            [cljsjs.codemirror.addon.fold.brace-fold]
            [cljsjs.codemirror.addon.fold.indent-fold]
            [cljsjs.codemirror.addon.selection.active-line]
            [cljsjs.codemirror.addon.search.match-highlighter]
            [cljsjs.codemirror.addon.search.search]
            [cljsjs.codemirror.addon.search.searchcursor]
            [cljsjs.codemirror.addon.hint.anyword-hint]
            [cljsjs.codemirror.addon.hint.show-hint]
            [cljsjs.codemirror.addon.display.placeholder]
            [codemirror.oge]
            [codemirror.parinfer]
            [cljs.spec.alpha :as s]
            [cljs.reader :refer [read-string]]
            [clojure.string :as str]))

(s/def ::mode (s/or :string string? :obj map?))
(s/def ::theme string?)
(s/def ::indentUnit pos-int?)
(s/def ::smartIndent boolean?)
(s/def ::lineNumbers boolean?)
(s/def ::readOnly boolean?)

(s/def ::value string?)
(s/def ::onChange (s/fspec :args (s/cat :code string?)))

(s/def ::options (s/keys :opt [::mode
                               ::theme
                               ::indentUnit
                               ::smartIndent
                               ::lineNumbers]))

(s/def ::props (s/keys :req-un [::value]
                       :opt [::options]))

(s/def ::extraKeys
  (s/map-of string? (s/or :str string? :fn fn?)))

(defn prop-call [comp name & args]
  (when-let [f (-> comp om/props name)]
    (apply f args)))

(defn html-props [props]
  (->> props
       (remove (fn [[k _]] (namespace k)))
       (into {})
       (clj->js)))

(def oge-cache (atom {}))

(declare autocomplete)

(om/defui ^:once Editor
  Object
  (componentWillReceiveProps [this {:keys            [value]
                                    ::p.connect/keys [indexes]}]
    (let [cm        (gobj/get this "codemirror")
          cur-index (gobj/getValueByKeys cm #js ["options" "ogeIndex"])]
      (when (and cur-index (not= indexes @cur-index))
        (reset! oge-cache {})
        (reset! cur-index indexes)
        (gobj/set (gobj/getValueByKeys cm #js ["options" "hintOptions"]) "hint" (partial autocomplete indexes)))

      ; there is a race condition that happens when user types something, react updates state and try to update
      ; the state back to the editor, which moves the cursor in the editor in weird ways. the workaround is to
      ; stop accepting external values after a short period after user key strokes.
      (if-not (gobj/get this "editorHold")
        (let [cur-value (.getValue cm)]
          (if (and cm value (not= value cur-value))
            (.setValue cm value))))))

  (componentDidMount [this]
    (let [textarea   (gobj/get this "textNode")
          options    (-> this om/props ::options (or {}) clj->js)
          process    (-> this om/props ::process)
          codemirror (js/CodeMirror.fromTextArea textarea options)]

      (try
        (.on codemirror "change" #(when (not= (gobj/get % "origin") "setValue")
                                    (js/clearTimeout (gobj/get this "editorHold"))
                                    (gobj/set this "editorHold"
                                      (js/setTimeout
                                        (fn []
                                          (gobj/set this "editorHold" false))
                                        800))
                                    (prop-call this :onChange (.getValue %))))
        (.setValue codemirror (-> this om/props :value))
        (if process (process codemirror))
        (catch :default e (js/console.warn "Error setting up CodeMirror" e)))
      (gobj/set this "codemirror" codemirror)))

  (componentWillUnmount [this]
    (if-let [cm (gobj/get this "codemirror")]
      (.toTextArea cm)))

  (render [this]
    (let [props (om/props this)]
      (dom/div (-> props (dissoc :value :onChange) (html-props))
        (js/React.createElement "textarea"
          #js {:ref          #(gobj/set this "textNode" %)
               :defaultValue (:value props)})))))

(def editor (om/factory Editor))

(defn escape-re [input]
  (let [re (js/RegExp. "([.*+?^=!:${}()|[\\]\\/\\\\])" "g")]
    (-> input str (.replace re "\\$1"))))

(defn fuzzy-re [input]
  (-> (reduce (fn [s c] (str s (escape-re c) ".*")) "" input)
      (js/RegExp "i")))

(defn str->keyword [s] (keyword (subs s 1)))

(defn token-context [{::p.connect/keys [index-io]} token]
  (let [state      (gobj/get token "state")
        mode       (gobj/get state "mode")
        path-stack (gobj/get state "pathStack")

        find-ctx   (fn find-ctx
                     ([s] (find-ctx s []))
                     ([s ctx]
                      (cond
                        ; ident join: [{[:ident x] [|]}]
                        (and (= "join" (gobj/get s "mode"))
                             (= "ident" (gobj/getValueByKeys s #js ["key" "mode"])))
                        (let [key (str->keyword (gobj/getValueByKeys s #js ["key" "key"]))]
                          {:type :attribute :context (conj ctx key)})

                        ; join: [{:child [|]}]
                        (and (= "join" (gobj/get s "mode"))
                             (= (string? (gobj/get s "key"))))
                        (let [key (str->keyword (gobj/get s "key"))]
                          (if (contains? (get index-io #{}) key)
                            {:type :attribute :context (conj ctx key)}
                            (recur (gobj/getValueByKeys s #js ["prev" "prev"]) (conj ctx key))))

                        (not (seq (js->clj s)))
                        {:type :attribute :context ctx})))]

    (cond
      (and (= "ident" mode)
           (or (nil? (gobj/get path-stack "key"))
               (= (gobj/get token "string") (gobj/get path-stack "key"))))
      {:type :ident}

      (and (= "join" mode)
           (or (= (gobj/get token "string") (gobj/get path-stack "key"))
               (nil? (gobj/get path-stack "key"))))
      (find-ctx (gobj/getValueByKeys path-stack #js ["prev" "prev"]))

      (or (= "attr-list" mode))
      (if (gobj/getValueByKeys path-stack #js ["prev" "mode"])
        (find-ctx (gobj/get path-stack "prev"))
        ; no stack, empty context
        {:type :attribute :context []}))))

(defn ^:export completions [index token reg]
  (let [ctx (token-context index token)]
    (when reg
      (case (:type ctx)
        :attribute (->> (p.connect/discover-attrs (assoc index ::p.connect/cache oge-cache)
                          (->> ctx :context (remove (comp #{">"} namespace)))))
        :ident (into {} (map #(hash-map % {})) (-> index ::p.connect/idents))
        {}))))

(gobj/set js/window "cljsDeref" deref)

(defn cm-completions [index cm]
  (let [cur   (.getCursor cm)
        ch    (.-ch cur)
        token (.getTokenAt cm cur)
        reg   (subs (.-string token) 0 (- ch (.-start token)))]
    (completions index token reg)))

(defn autocomplete [index cm options]
  (let [cur    (.getCursor cm)
        line   (.-line cur)
        ch     (.-ch cur)
        token  (.getTokenAt cm cur)
        reg    (subs (.-string token) 0 (- ch (.-start token)))
        blank? (#{"[" "{" " "} reg)
        start  (if blank? cur (-> js/CodeMirror (.Pos line (- ch (count reg)))))
        end    (if blank? cur (-> js/CodeMirror (.Pos line (gobj/get token "end"))))
        words  (->> (cm-completions index cm) (mapv first))]

    (if words
      (let [fuzzy (if blank? #".*" (fuzzy-re reg))]
        #js {:list (->> words
                        (remove (get index ::p.connect/autocomplete-ignore #{}))
                        (map str)
                        (filter #(re-find fuzzy %))
                        sort
                        clj->js)
             :from start
             :to   end}))))

(defn def-cm-command [name f]
  (gobj/set (gobj/get js/CodeMirror "commands") name f))

(defn ^:export key-has-children? [completions token]
  (let [reg (str->keyword (gobj/get token "string"))]
    (and (= "atom" (gobj/get token "type"))
         (or (seq (get completions reg))
             (= ">" (namespace reg))))))

(defn str-repeat [s n]
  (str/join (repeat n s)))

(def-cm-command "ogeJoin"
  (fn [cm]
    (let [cur    (.getCursor cm)
          token  (.getTokenAt cm cur)
          indent (or (gobj/getValueByKeys token #js ["state" "pathStack" "indent"])
                     0)]

      (if (and (= "attr-list" (gobj/getValueByKeys token #js ["state" "mode"]))
               (= "atom-composed" (gobj/get token "type")))
        (let [line  (.-line cur)
              start (.Pos js/CodeMirror line (gobj/get token "start"))
              end   (.Pos js/CodeMirror line (gobj/get token "end"))
              s     (gobj/get token "string")

              [cursor-end joined]
              (if (= (.-ch start) indent)
                [(.Pos js/CodeMirror (inc line) (+ 2 indent))
                 (str "{" s "\n" (str-repeat " " (inc indent)) "[]}")]

                [(.Pos js/CodeMirror line (+ (gobj/get token "start")
                                            (count s)
                                            3))
                 (str "{" s " []}")])]
          (.replaceRange cm joined start end)
          (.setCursor cm cursor-end)
          (.showHint cm))))))

(defn oge [{::p.connect/keys [indexes] :as props}]
  (let [options {::lineNumbers               true
                 ::mode                      "oge"
                 ::matchBrackets             true
                 ::autoCloseBrackets         true
                 ::highlightSelectionMatches true
                 ::foldGutter                true
                 ::hintOptions               {:hint           (partial autocomplete indexes)
                                              :completeSingle false}
                 ::extraKeys                 {"Ctrl-Space" "autocomplete"}
                 ::gutters                   ["CodeMirror-linenumbers" "CodeMirror-foldgutter"]
                 :ogeIndex                   (atom indexes)}]
    (editor (-> props
                (assoc ::process (fn [cm]
                                   (.on cm "keyup" (fn [cm e] (when (and (not (gobj/getValueByKeys cm #js ["state" "completionActive"]))
                                                                         (= 1 (-> (gobj/get e "key") (count))))
                                                                (js/CodeMirror.showHint cm))))
                                   ((gobj/get js/parinferCodeMirror "init") cm "smart" #js {:forceBalance true})))
                (update ::options #(merge options %))))))

(defn clojure [props]
  (let [options {::lineNumbers               true
                 ::mode                      "clojure"
                 ::matchBrackets             true
                 ::highlightSelectionMatches true
                 ::foldGutter                true
                 ::gutters                   ["CodeMirror-linenumbers" "CodeMirror-foldgutter"]}]
    (editor (-> props
                (update ::options #(merge options %))))))

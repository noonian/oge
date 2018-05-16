(ns com.wsscode.oge.ui.flame-graph
  (:require [fulcro.client.primitives :as om]
            [fulcro.client.dom :as dom]
            [com.wsscode.pathom.profile :as p.profile]
            [cljsjs.d3]
            ["d3-tip" :as tip]
            ["d3-flame-graph" :as flame]
            [goog.object :as gobj]))

(defn js-call [obj fn & args]
  (let [f (gobj/get obj fn)]
    (.apply f obj (to-array args))))

(js/console.log "FLAME" flame)

(defn render-flame [profile target]
  (let [profile' (-> profile p.profile/profile->flame-graph clj->js)
        tooltip  (fn [d]
                   (let [name        (gobj/getValueByKeys d #js ["data" "name"])
                         value       (gobj/get d "value")
                         children    (gobj/get d "children")
                         self        (->> (transduce (map #(gobj/get % "value")) + children)
                                          (- value))
                         total-value (gobj/get profile' "value")
                         pct         (-> (/ value total-value) (* 100) (.toFixed 2))]
                     (str name "<br>"
                       self "/" value "ms<br />"
                       pct "% of total")))
        tip      (-> (tip)
                     (js-call "attr" "class" "d3-flame-graph-tip")
                     (js-call "html" tooltip))

        flame    (-> (flame/flamegraph)
                     (js-call "width" 600)
                     (js-call "height" 300)
                     (js-call "tooltip" tip))]

    (-> (js/d3.select target)
        (.datum profile')
        (.call flame))))

(om/defui ^:once FlameGraph
  Object
  (componentDidMount [this]
    (let [{:keys [profile]} (om/props this)]
      (render-flame profile (gobj/get this "root"))))

  (componentWillReceiveProps [this {:keys [profile]}]
    (when (not= profile (-> this om/props :profile))
      (render-flame profile (gobj/get this "root"))))

  (render [this]
    (dom/div #js {:ref #(gobj/set this "root" %)})))

(def flame-graph (om/factory FlameGraph))

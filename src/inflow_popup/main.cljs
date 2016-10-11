
(ns inflow-popup.main
  (:require [respo.core :refer [render! clear-cache!]]
            [inflow-popup.component.container :refer [comp-container]]
            [cljs.reader :refer [read-string]]))

(defn dispatch! [op op-data])

(defonce store-ref (atom {}))

(defonce states-ref (atom {}))

(defn render-app! []
  (let [target (.querySelector js/document "#app")]
    (render! (comp-container @store-ref) target dispatch! states-ref)))

(defn -main []
  (enable-console-print!)
  (render-app!)
  (add-watch store-ref :changes render-app!)
  (add-watch states-ref :changes render-app!)
  (println "app started!")
  (let [configEl (.querySelector js/document "#config")
        config (read-string (.-innerHTML configEl))]
    (if (and (some? navigator.serviceWorker) (:build? config))
      (-> navigator.serviceWorker
       (.register "./sw.js")
       (.then
         (fn [registration]
           (println "resigtered:" registration.scope)))
       (.catch (fn [error] (println "failed:" error)))))))

(defn on-jsload! []
  (clear-cache!)
  (render-app!)
  (println "code updated."))

(set! js/window.onload -main)
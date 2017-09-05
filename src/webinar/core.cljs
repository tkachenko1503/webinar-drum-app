(ns webinar.core
  (:require [reagent.core :as r]
            [goog.dom :as dom]
            [goog.json :as json]
            [webinar.views :refer [App]]
            [webinar.store :as store :refer [state]]))


(enable-console-print!)


(defn ls-get-item
  "Get item from local storage"
  [item-key]
  (some-> js/localStorage
          (.getItem item-key)
          (json/parse)
          (js->clj :keywordize-keys true)))


(defn ls-set-item
  "Set item to local storage"
  [item-key value]
  (some->> (clj->js value)
           (json/serialize)
           (.setItem js/localStorage item-key)))


(defn start-app
  "Entry point to start the app"
  [drum-kit root]
  (let [kit (or drum-kit {})]

    ;Set initial state before render
    (store/dispatch :initialize-kit kit)

    ;Persist store on change
    (store/subscribe
      (fn [state]
        (ls-set-item "saved-kit"
                     (:kit state))))

    ;Initial app render
    (r/render-component
      [App state]
      root)))


(start-app
  (ls-get-item "saved-kit")
  (dom/getElement "app"))

(ns webinar.core
  (:require [reagent.core :as r]
            [goog.events :as events]
            [goog.dom :as dom]
            [goog.json :as json]
            [goog.functions]
            [webinar.views :refer [App]]
            [webinar.store :as store :refer [state]])
  (:import [goog.events EventType]))


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


(defn event->position
  "Calculates position coords from event"
  [event]
  (let [target        (dom/getElement "canvas")
        target-rect   (.getBoundingClientRect target)
        target-width  (.-clientWidth target)
        target-height (.-clientHeight target)
        client-x      (- (.-clientX event) (.-left target-rect))
        client-y      (- (.-clientY event) (.-top target-rect))
        normalized-x  (/ (* client-x 10) target-width)
        normalized-y  (/ (* client-y 6) target-height)]

    {:x normalized-x
     :y normalized-y}))


"Fn for changing position"
(def change-position
  (goog.functions.throttle
    #(store/dispatch :change-drum-position %1 %2)
    50))


(defn drag-move
  "Dispathes drag drum action"
  [event]
  (when-let [drum-id (:dragged-drum-id @store/state)]
    (change-position drum-id
                     (event->position event))))


(defn stop-dragging
  "Dispathes stop drugging action"
  [& args]
  (events/unlisten js/window EventType.MOUSEMOVE drag-move)
  (store/dispatch :stop-dragging-drum))


(defn start-app
  "Entry point to start the app"
  [drum-kit root]
  (let [kit (or drum-kit {})]

    ;Set initial state before render
    (store/dispatch :initialize-kit kit)

    ;Persist store on change
    (store/subscribe
      :persist
      (fn [state]
        (ls-set-item "saved-kit"
                     (:kit state))))

    ;DnD side effect
    (store/subscribe
      :drag
      (fn [state]
        (let [dragging?  (:is-dragging state)
              dragged-id (:dragged-drum-id state)]

          (when (and dragging? dragged-id)
            (events/listen js/window EventType.MOUSEMOVE drag-move)
            (events/listenOnce js/window EventType.MOUSEUP stop-dragging)))))

    ;Initial app render
    (r/render-component
      [App state]
      root)))


(start-app
  (ls-get-item "saved-kit")
  (dom/getElement "app"))

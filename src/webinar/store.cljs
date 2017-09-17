(ns webinar.store
  (:require [reagent.core :as r]
            [webinar.storage :as storage]))


;Dynamic app state
(def state
  (r/atom
    {:kit      {}
     :app-mode :play}))


;Change state functions
(defn set-app-mode
  "Sets new app mode"
  [s mode]
  (swap! s assoc :app-mode mode))


(defn toggle-add-panel
  "Toggle add drum panel"
  [s]
  (swap! s update :add-dialog-visible? not))


(defn hide-add-panel
  "Hide add drum panel"
  [s]
  (swap! s assoc :add-dialog-visible? false))


(defn add-drum
  "Add drum to kit"
  [s drum-id]
  (swap! s assoc-in [:kit drum-id] {:x 5 :y 3}))


(defn delete-drum
  "Add drum to kit"
  [s drum-id]
  (swap! s update :kit dissoc drum-id))


(defn change-drum-position
  "Move drum around kit"
  [s drum-id {:keys [x y]}]
  (let [drum           (get storage/drums drum-id)
        drum-half-size (:size drum)

        min-x-position (- x drum-half-size)
        max-x-position (+ x drum-half-size)
        position-x     (cond
                         (< min-x-position 0) drum-half-size
                         (> max-x-position 10) (- 10 drum-half-size)
                         :else x)

        min-y-position (- y drum-half-size)
        max-y-position (+ y drum-half-size)
        position-y     (cond
                         (< min-y-position 0) drum-half-size
                         (> max-y-position 6) (- 6 drum-half-size)
                         :else y)]

    (swap! s assoc-in [:kit drum-id] {:x position-x :y position-y})))


(defn init-kit
  "Reset kit"
  [s kit]
  (swap! s assoc :kit kit))


(defn dragging-drum
  "Start dragging drum"
  [s drum-id]
  (swap! s assoc :is-dragging true)
  (swap! s assoc :dragged-drum-id drum-id))


(defn dont-drag
  "Stop dragging"
  [s]
  (swap! s assoc :is-dragging false)
  (swap! s dissoc :dragged-drum-id))


;Dispatch actions
(defmulti dispatch identity)


(defmethod dispatch :set-app-mode
  [_ mode]
  (set-app-mode state mode))

(defmethod dispatch :add-drum
  [_ id]
  (add-drum state id))

(defmethod dispatch :toggle-add-panel
  [_]
  (toggle-add-panel state))

(defmethod dispatch :hide-add-panel
  [_]
  (hide-add-panel state))

(defmethod dispatch :delete-drum
  [_ id]
  (delete-drum state id))

(defmethod dispatch :change-drum-position
  [_ id position]
  (change-drum-position state id position))

(defmethod dispatch :initialize-kit
  [_ kit]
  (init-kit state kit))

(defmethod dispatch :start-dragging-drum
  [_ drum-id]
  (dragging-drum state drum-id))

(defmethod dispatch :stop-dragging-drum
  [_]
  (dont-drag state))


(defn dispatch-all
  "Dispatch multiple actions"
  [actions]
  (doseq [action actions]
    (apply dispatch action)))


(defn subscribe
  "Call side effects on store updates"
  [id fx]
  (add-watch state id #(fx %4)))

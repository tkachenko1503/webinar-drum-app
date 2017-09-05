(ns webinar.store
  (:require [reagent.core :as r]))


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
  (swap! s assoc-in [:kit drum-id] {:x x :y y}))


(defn init-kit
  "Reset kit"
  [s kit]
  (swap! s assoc :kit kit))


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


(defn dispatch-all
  "Dispatch multiple actions"
  [actions]
  (doseq [action actions]
    (apply dispatch action)))


(defn subscribe
  "Call side effects on store updates"
  [fx]
  (add-watch state :persist #(fx %4)))

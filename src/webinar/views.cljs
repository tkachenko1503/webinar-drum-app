(ns webinar.views
  (:require [webinar.storage :as storage]
            [webinar.store :as store]))


(def drum-animation
  [:animateTransform
   {:attributeType "XML"
    :attributeName "transform"
    :type          "scale"
    :from          1
    :to            1.04
    :dur           "0.1s"
    :begin         "click"}])


(defn play
  "Play sound by specified name"
  [drum-id]
  (let [sound (get storage/sounds drum-id)]
    (doto sound
      (aset "currentTime" 0)
      (.play))))


(defn Panel
  "Abstract panel for controls"
  ([Controls]
   [Panel {} Controls])

  ([props Controls]
   [:g props
    [:rect {:x       0
            :y       -0.6
            :width   10
            :height  0.6
            :opacity 0.4
            :fill    "#FFF"}]
    Controls]))


(defn Text
  "Wrapped text tag"
  [params text]
  [:text (merge
           {:fill        "#000"
            :stroke      "none"
            :font-size   "0.3px"
            :text-anchor "middle"}
           params)
   text])


(defn EditControls
  "Controls to add and delete drums"
  []
  [Panel
   [:g
    [Text {:x        1
           :y        -0.2
           :cursor   :pointer
           :on-click #(store/dispatch
                        :toggle-add-panel)}
     "Add"]

    [Text {:x 5
           :y -0.2}
     "Delete"]

    [Text {:x        9
           :y        -0.2
           :cursor   :pointer
           :on-click #(store/dispatch-all
                        [[:hide-add-panel]
                         [:set-app-mode :play]])}
     "Play"]]])


(defn PlayControls
  "Button to turn on edit mode"
  []
  [Panel {:opacity 0.3}
   [Text {:x        5
          :y        -0.2
          :cursor   :pointer
          :on-click #(store/dispatch
                       :set-app-mode :edit)}
    "Edit"]])


(defn AddDrumPanel
  "Panel for adding new drum"
  []
  (into

    [:g
     [:rect {:x       0
             :y       0
             :width   10
             :height  0.6
             :opacity 0.2
             :fill    "#FFF"}]]

    (mapv
      (fn [[drum-id drum] i]
        [Text {:x        i
               :y        0.4
               :cursor   :pointer
               :on-click #(store/dispatch
                            :add-drum drum-id)}
         (:name drum)])
      storage/drums
      (iterate inc 1))))


(defn ControlPanel
  "Controls to manage app"
  [{:keys [app-mode add-dialog-visible?]}]
  [:g
   (if (= app-mode :edit)
     [EditControls]
     [PlayControls])
   (when add-dialog-visible?
     [AddDrumPanel])])


(defn Drum
  "Returns markup for single drum"
  [[drum-id {:keys [x y]}] play-mode?]
  (let [drum (get storage/drums drum-id)
        size (:size drum)
        type (:type drum)]

    [:g {:stroke       "#000"
         :stroke-width 0.01
         :className    (str "drum_type_"
                            (name type))
         :fill         (if (= type :cymbal)
                         "#FFF9C4"
                         "#FAFAFA")
         :style        {:transform-origin "50% 50%"
                        :user-select      "none"}
         :on-click     (when play-mode?
                         #(play drum-id))}

     [:circle {:r  size
               :cx x
               :cy y}]

     (when-not (= type :cymbal)
       [:circle {:r  (- size 0.05)
                 :cx x
                 :cy y}])

     [Text {:x x
            :y y}
      (:name drum)]

     (when play-mode?
       drum-animation)]))


(defn App
  "Returns app markup"
  [state]
  (let [app-state  @state
        kit        (:kit app-state)
        play-mode? (= (:app-mode app-state) :play)]

    [:div.drum-kit

     (into
       [:svg.canvas {:view-box "0 0 10 6"
                     :overflow "visible"}
        [ControlPanel app-state]]

       (for [drum kit]
         [Drum drum play-mode?]))]))

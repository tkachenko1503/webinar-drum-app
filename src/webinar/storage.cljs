(ns webinar.storage)


(def sounds
  {:snare     (js/Audio. "/sounds/snare.mp3")
   :kick      (js/Audio. "/sounds/kick.mp3")
   :small-tom (js/Audio. "/sounds/small-tom.mp3")
   :rack-tom  (js/Audio. "/sounds/rack-tom.mp3")
   :floor-tom (js/Audio. "/sounds/floor-tom.mp3")
   :hi-hat    (js/Audio. "/sounds/hi-hat.mp3")
   :crash     (js/Audio. "/sounds/crash.mp3")})


(def drums
  {:snare     {:name "Snare" :name-position 0.7 :size 1.2 :type :drum}
   :kick      {:name "Kick" :name-position 1.8 :size 1.5 :type :drum}
   :small-tom {:name "smallTom" :name-position 3.1 :size 1 :type :drum}
   :rack-tom  {:name "rackTom" :name-position 4.7 :size 1.1 :type :drum}
   :floor-tom {:name "floorTom" :name-position 6.4 :size 1.35 :type :drum}
   :hi-hat    {:name "hiHat" :name-position 7.9 :size 1 :type :cymbal}
   :crash     {:name "Crash" :name-position 9.2 :size 1.6 :type :cymbal}})

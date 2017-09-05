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
  {:snare     {:name "Snare" :size 1.2 :type :drum}
   :kick      {:name "Kick" :size 1.5 :type :drum}
   :small-tom {:name "sTom" :size 1 :type :drum}
   :rack-tom  {:name "rTom" :size 1.1 :type :drum}
   :floor-tom {:name "fTom" :size 1.35 :type :drum}
   :hi-hat    {:name "hHat" :size 1 :type :cymbal}
   :crash     {:name "Crash" :size 2 :type :cymbal}})

# CLJS webinar 

Simple drum app for my webinar 


## Run project

For development i'm using Cursive
and running `clojure.main` in normal JVM process,
see the setup guide for figwheel - https://github.com/bhauman/lein-figwheel/wiki/Running-figwheel-in-a-Cursive-Clojure-REPL   

To create a production build run:

    lein do clean, cljsbuild once min

And open your browser in `resources/public/index.html`.

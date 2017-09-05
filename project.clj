(defproject webinar "0.1.0-SNAPSHOT"
  :description "Simple drum app for my webinar"

  :min-lein-version "2.7.1"

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.854"]
                 [figwheel-sidecar "0.5.12"]
                 [reagent "0.7.0"]]

  :plugins [[lein-cljsbuild "1.1.7" :exclusions [[org.clojure/clojure]]]]

  :source-paths ["src"]

  :cljsbuild {:builds
              [{:id "dev"
                :source-paths ["src"]

                :figwheel {:on-jsload "webinar.core/on-js-reload"
                           :open-urls ["http://localhost:3449/index.html"]}

                :compiler {:main webinar.core
                           :asset-path "js/compiled/out"
                           :output-to "resources/public/js/compiled/webinar.js"
                           :output-dir "resources/public/js/compiled/out"
                           :source-map-timestamp true
                           :preloads [devtools.preload]}}

               {:id "min"
                :source-paths ["src"]
                :compiler {:output-to "resources/public/js/compiled/webinar.js"
                           :main webinar.core
                           :optimizations :advanced
                           :pretty-print false}}]}

  :figwheel {:css-dirs ["resources/public/css"]}

  :profiles {:dev {:dependencies [[binaryage/devtools "0.9.4"]
                                  [figwheel-sidecar "0.5.12"]
                                  [com.cemerick/piggieback "0.2.2"]]

                   :source-paths ["src" "dev"]

                   :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}

                   :clean-targets ^{:protect false} ["resources/public/js/compiled" :target-path]}})

(defproject web-app-crux "0.1.0-SNAPSHOT"
  :description "TODO"
  :url "TODO"
  :license {:name "TODO: Choose a license"
            :url "http://choosealicense.com/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [com.stuartsierra/component "1.1.0"]
                 [pro.juxt.crux/crux-core "1.18.1"]
                 [org.clojure/core.async "1.5.648"]
                 [io.pedestal/pedestal.service       "0.5.10"]
                 [io.pedestal/pedestal.route         "0.5.10"]
                 [io.pedestal/pedestal.jetty         "0.5.10"]]
  :profiles {:dev {:dependencies [[org.clojure/tools.namespace "1.2.0"]
                                  [com.stuartsierra/component.repl "1.0.0"]]
                   :source-paths ["dev"]}})

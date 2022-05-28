(defproject web-app-crux "0.1.0-SNAPSHOT"
  :description "TODO"
  :url "TODO"
  :license {:name "TODO: Choose a license"
            :url "http://choosealicense.com/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.clojure/data.json "2.4.0"]
                 [com.stuartsierra/component "1.1.0"]

                 [environ "0.5.0"]

                 ;;kafka
                 [fundingcircle/jackdaw "0.6.0"]

                 ;;datomic
                 [com.datomic/client-pro "1.0.75"]
                 ;; in collection under :dependencies key
                 [com.datomic/datomic-pro "1.0.6397"]

                 ;; start DBs
                 [pro.juxt.crux/crux-core "1.18.1"]
                 [com.novemberain/monger "3.5.0"]
                 ;; END DBs

                 [org.clojure/core.async "1.5.648"]
                 [prismatic/schema "1.2.1"]
                 [io.pedestal/pedestal.service       "0.5.10"]
                 [io.pedestal/pedestal.route         "0.5.10"]
                 [io.pedestal/pedestal.interceptor   "0.5.10"]
                 [io.pedestal/pedestal.jetty         "0.5.10"]]
  :profiles {:dev {:dependencies [[org.clojure/tools.namespace "1.2.0"]
                                  [com.stuartsierra/component.repl "1.0.0"]
                                  [com.github.jpmonettas/flow-storm-dbg "2.0.38"]
                                  [com.github.jpmonettas/flow-storm-inst "2.0.38"]]
                   :source-paths ["dev"]}})

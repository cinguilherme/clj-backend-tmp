(ns dev
  "Tools for interactive development with the REPL. This file should
  not be included in a production build of the application.

  Call `(reset)` to reload modified code and (re)start the system.

  The system under development is `system`, referred from
  `com.stuartsierra.component.repl/system`.

  See also https://github.com/stuartsierra/component.repl"
  (:require
   [clojure.java.io :as io]
   [clojure.java.javadoc :refer [javadoc]]
   [clojure.pprint :refer [pprint]]
   [clojure.reflect :refer [reflect]]
   [clojure.repl :refer [apropos dir doc find-doc pst source]]
   [clojure.set :as set]
   [clojure.string :as string]
   [clojure.test :as test]
   [clojure.tools.namespace.repl :refer [refresh refresh-all clear]]
   [com.stuartsierra.component :as component]
   [com.stuartsierra.component.repl :refer [reset set-init start stop system]]
   [app.web-app-crux]
   [app.components.CruxDb :as CruxDb]
   [app.components.MemoryCache :as MemoryCache]
   [app.components.Cli :as Cli]
   [app.components.Server :as Server]
   [io.pedestal.http :as http]
   [io.pedestal.http.route :as route]))

;; Do not try to load source code from 'resources' directory
(clojure.tools.namespace.repl/set-refresh-dirs "dev" "src" "test")


(defn get-todo [_]
  {:status 200 :body {:well 101}})

(def routes
  (route/expand-routes
   #{["/todo" :get get-todo :route-name :list-todo]}))

(def service-map
  {::http/routes routes
   ::http/type :jetty
   ::http/port 8081
   ::http/join? false})

(defn dev-system
  "Constructs a system map suitable for interactive development."
  []
  (component/system-map
   :db-crux (CruxDb/new-db)
   :cache (MemoryCache/new-cache)
   :cli (Cli/new-cli)
   :server (Server/new-pedestal service-map)))

(set-init (fn [_] (dev-system)))


(comment

  (start)
  (stop)
  (reset)

  (refresh dev-system)

  (-> (dev-system))

  (let [{:keys [db-crux cache]} (dev-system)]
    (println cache)))
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
    [app.components.MongoDB :as MongoDb]
    [app.components.DatomicDB :as DatomicDB]
    [app.components.MemoryCache :as MemoryCache]
    [app.components.Cli :as Cli]
    [app.components.Server :as Server]
    [app.server :as app-server]
    [app.components.Config :as Config]))

;; Do not try to load source code from 'resources' directory
(clojure.tools.namespace.repl/set-refresh-dirs "dev" "src" "test")

(def mongo-uri (System/getenv "MONGO_ATLAS_URI"))

(def datomic-uri (str "datomic:mem://hello"))

(defn dev-system
  "Constructs a system map suitable for interactive development."
  []
  (component/system-map

    :config
    (Config/new-config "dev")

    :cli
    (Cli/new-cli)

    :cache
    (MemoryCache/new-cache)

    :datomic
    (component/using
      (DatomicDB/new-datomicdb datomic-uri)
      {:config :config})

    :mongo
    (component/using
      (MongoDb/new-mongodb mongo-uri)
      {:config :config})

    :service-map
    app.server/service-map

    :routes-maker
    app.server/make-routes

    :db-crux
    (component/using
      (CruxDb/new-db)
      {:config :config})

    :server
    (component/using
      (Server/new-pedestal)
      {:service-map  :service-map
       :routes-maker :routes-maker
       :db-crux      :db-crux
       :datomic      :datomic
       :cache        :cache
       :config       :config
       :mongo        :mongo})))

(set-init (fn [_] (dev-system)))

(comment

  (start)
  (stop)

  ;(reset)
  ;(refresh dev-system)

  ;(-> (dev-system) :server)


  (let [{:keys [db-crux cache]} (dev-system)]
    (println cache)))

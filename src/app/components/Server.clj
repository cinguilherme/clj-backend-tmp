(ns app.components.Server
  (:require [com.stuartsierra.component :as component]
            [io.pedestal.http :as http]
            [app.server :as server]))

(defn test?
  [service-map]
  (= :test (:env service-map)))

(defn make-components [components]
  {:name ::components
   :enter (fn [context]
            (update context :request assoc ::components components))})

(defrecord Pedestal [db-crux cache service-map service]
  component/Lifecycle

  (start [this]
    (if service
      this
      (let [routes (-> {:db db-crux :cache cache}
                       make-components
                       server/make-routes)]

        (cond-> (assoc service-map ::http/routes routes)
                true http/create-server
                (not (test? service-map)) http/start
                true ((partial assoc this :service))))))

  (stop [this]
    (when (and service (not (test? service-map)))
      (http/stop service))
    (assoc this :service nil)))


(defn new-pedestal []
  (map->Pedestal {}))

(ns app.components.Server
  (:require [com.stuartsierra.component :as component]
            [io.pedestal.http :as http]))

(defn test?
  [service-map]
  (= :test (:env service-map)))

(defn make-components [components]
  {:name  ::components
   :enter (fn [context]
            (update context :request assoc ::components components))})

(defn rich-routes [db-crux config mongo cache routes-maker]
  (-> {:db db-crux :cache cache :config config :mongo mongo}
      make-components
      routes-maker))

(defrecord Pedestal [db-crux config cache mongo service-map routes-maker service]
  component/Lifecycle

  (start [this]
    (if service
      this
      (let [routes (rich-routes db-crux config mongo cache routes-maker)
            service-map-with-routes (assoc service-map ::http/routes routes)]

        (cond-> service-map-with-routes
                true http/create-server
                (not (test? service-map)) http/start
                true ((partial assoc this :service))))))

  (stop [this]
    (when (and service (not (test? service-map)))
      (http/stop service))
    (assoc this :service nil)))


(defn new-pedestal []
  (map->Pedestal {}))

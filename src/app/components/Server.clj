(ns app.components.Server
  (:require [com.stuartsierra.component :as component]
            [io.pedestal.http :as http]
            [app.utils :refer [not-nil? tap]]))

(defn test?
  [service-map]
  (= :test (:env service-map)))

(defonce service-instance (atom nil))

(defn set-service! [service]
  (reset! service-instance service)
  service)

(defn stop-service! []
  (println "stopping current service")
  (some-> @service-instance http/stop println)
  (reset! service-instance nil))

(defrecord Pedestal [db cache service-map service]
  component/Lifecycle

  (start [this]
    (if service
      this
      (cond-> service-map
              true http/create-server
              (not (test? service-map)) http/start
              true ((partial assoc this :service)))))

  (stop [this]
    (when (and service (not (test? service-map)))
      (http/stop service))
    (assoc this :service nil)))


(defn new-pedestal []
  (map->Pedestal {}))

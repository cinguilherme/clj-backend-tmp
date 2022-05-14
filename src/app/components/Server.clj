(ns app.components.Server
  (:require [com.stuartsierra.component :as component]
            [io.pedestal.http :as http]
            [app.utils :as utils :refer [not-nil? tap]]))

(defn test?
  [service-map]
  (= :test (:env service-map)))

(defonce service-instance (atom nil))

(defn set-service! [service]
  (reset! service-instance service)
  service)

(defn stop-service! []
  (http/stop @service-instance)
  (reset! service-instance nil))

(defrecord Pedestal [service-map]
  component/Lifecycle

  (start [this]
    (let [_ (tap @service-instance)
          _ (tap this)
          service (tap (:service this))]
      (cond

        (not-nil? service)
        this

        :else
        (do
            (when (not-nil? @service-instance)
              (println "stoping http service")
              (stop-service!))
            (println "starting http service")
            (cond-> service-map
                    true http/create-server
                    (not (test? service-map)) http/start
                    true set-service!
                    true (partial assoc this :service))))))

  (stop [this]
    (let [_ (tap @service-instance)
          _ (tap this)
          service (tap (:service this))]
      (println "stoping http service")
      (if (not-nil? service)
        (do (stop-service!)
            (http/stop service)
            (assoc this :service nil))
        this))))


(defn new-pedestal [service-map]
  (map->Pedestal {:service-map service-map}))

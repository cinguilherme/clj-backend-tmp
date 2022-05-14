(ns app.components.Config
  (:require [com.stuartsierra.component :as component]))


(defrecord Config [config-map]
  component/Lifecycle

  (start [this]
    (if (-> this :config nil?)
      (assoc this :config {})
      this))

  (stop [this]
    (assoc this :config nil)))


(defn new-config []
  (map->Config {}))

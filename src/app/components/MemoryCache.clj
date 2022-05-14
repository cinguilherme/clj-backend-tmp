(ns app.components.MemoryCache
  (:require [com.stuartsierra.component :as component]))

(defrecord MemoryCache [m]
  component/Lifecycle

  (start [this]
    (assoc this :cache m))

  (stop [this]
    (assoc this :cache nil)))

(defn new-cache []
  (map->MemoryCache {:cache (atom {})}))
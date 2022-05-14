(ns app.components.MemoryCache
  (:require [com.stuartsierra.component :as component]))

(defrecord MemoryCache [cache]
  component/Lifecycle

  (start [this]
    (assoc this :cache cache))

  (stop [this]
    (assoc this :cache nil)))

(defn new-cache []
  (map->MemoryCache {:cache (atom {})}))
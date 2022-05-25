(ns app.components.MongoDB
  (:require [com.stuartsierra.component :as component]))

(defrecord MongoDB []
  component/Lifecycle

  (start [this] this)
  (stop [this] this))

(defn new-mongodb []
  (map->MongoDB {}))

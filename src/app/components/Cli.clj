(ns app.components.Cli
  (:require [com.stuartsierra.component :as component]))

(defrecord Cli []
 component/Lifecycle
  
  (start [this]
         (assoc this :cli {}))
  
  (stop [this]
        (assoc this :cli nil)))


(defn new-cli []
  (map->Cli {}))
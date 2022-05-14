(ns app.components.CruxDb
  (:require [com.stuartsierra.component :as component]
            [crux.api :as crux]))

(defrecord CruxDbComponent [_]
  component/Lifecycle

  (start [this]
    (let [node (crux/start-node {})]
      (assoc this :db-node node)))

  (stop [this]
        (dissoc this :db-node)))

(defn new-db []
  (map->CruxDbComponent {}))
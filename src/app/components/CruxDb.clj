(ns app.components.CruxDb
  (:require [com.stuartsierra.component :as component]
            [crux.api :as crux]))

(defrecord CruxDbComponent [node-config]
  component/Lifecycle

  (start [this]
    (let [node (:db-node this)]
      (if node
        this
        (let [new-node (crux/start-node node-config)]
          (assoc this :db-node new-node)))))

  (stop [this]
    (let [node (:db-node this)]
      (if node
        (assoc this :db-node nil)
        this))))

(defn new-db []
  (map->CruxDbComponent {:node-condig {}}))

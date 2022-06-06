(ns app.components.DatomicDB
  (:require [com.stuartsierra.component :as component]
            [datomic.api :as d]))


(defrecord DatomicDB [datomic-uri config]
  component/Lifecycle

  (start [this]
    (println datomic-uri)
    (let [conn (d/connect datomic-uri)]
      (assoc this :datomic {:conn conn})))

  (stop [this]
    (let [conn (-> this :datomic :conn)
          _ (d/shutdown conn)])
    (dissoc this :datomic)))

(defn new-datomicdb [uri]
  (map->DatomicDB {:datomic-uri uri}))

(ns app.components.DatomicDB
  (:require [com.stuartsierra.component :as component]
            [datomic.api :as d]))


(defrecord DatomicDB [datomic-uri config]
  component/Lifecycle

  (start [this]
    (println datomic-uri)
    (try (let [conn (d/connect datomic-uri)]
           (assoc this :datomic {:conn conn}))
      (catch Throwable t
        (println "DatomicDB: Failed to connect to datomic-uri")
        (println t)
        this)))

  (stop [this]
    (let [conn (-> this :datomic :conn)
          _ (d/shutdown conn)])
    (dissoc this :datomic)))

(defn new-datomicdb [uri]
  (map->DatomicDB {:datomic-uri uri}))

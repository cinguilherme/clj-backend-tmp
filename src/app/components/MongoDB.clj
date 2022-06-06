(ns app.components.MongoDB
  (:require [com.stuartsierra.component :as component]
            [monger.core :as mg]))

(defrecord MongoDB [mongo-uri]
  component/Lifecycle

  (start [this]
    this
    #_(let [{:keys [conn db]} (mg/connect-via-uri mongo-uri)]
      (assoc this :mongo {:conn conn :db db})))

  (stop [this]
    this
    #_(let [conn (-> this :mongo :conn)
          _ (mg/disconnect conn)])
    (dissoc this :mongo)))

(defn new-mongodb [uri]
  (map->MongoDB {:mongo-uri uri}))

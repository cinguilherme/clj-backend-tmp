(ns app.controllers.rollout
  (:require [schema.core :as s]
            [app.db.rollout :as db.rollout]
            [app.utils :as utils]))

(s/defn new-rollout!
  [rollout :- app.models.rollout/Rollout
   {:keys [db] :as components}]
  (println (:db-node db))
  (let [node (:db-node db)
        tx (db.rollout/insert! rollout node)]
    rollout))

(defn get-rollouts! [{:keys [db] :as components}]
  (db.rollout/get-all! (:db-node db)))

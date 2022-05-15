(ns app.db.rollout
  (:require [schema.core :as s]
            [crux.api :as crux]
            [app.models.rollout :as models.rollout]))


(s/defn insert!
  [rollout :- models.rollout/Rollout
   node]
  (let [tx (crux/submit-tx node [[:crux.tx/put rollout]])
        _ (println tx)]
    rollout))

(s/defn get-all! :- [s/Any]
  [node]
  (crux/q
    (crux/db node)
    '{:find  [(pull ?t1 [*])]
      :where [[?t1 :crux.db/id ?some-id]
              [?t1 :rollout/id ?some-other-id]]}))

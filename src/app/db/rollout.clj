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

(s/defn get-by-id! :- s/Any
  [id node]
  (let [query '{:find  [(pull r [*])]
                :where [[r :crux.db/id ?some-id]
                        [r :rollout/id id]]
                :id    [id]}]
    (crux/q (crux/db node) query id)))

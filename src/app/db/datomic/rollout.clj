(ns app.db.datomic.rollout
  (:require [schema.core :as s]
            [app.models.datomic.rollout :as m.d.rollout]))

(s/defn insert!
  [conn :- s/Any
   rollout :- m.d.rollout/Rollout]
  )

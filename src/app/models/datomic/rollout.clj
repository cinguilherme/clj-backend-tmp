(ns app.models.datomic.rollout
  (:require [schema.core :as s]))

(def skeleton
  {:id {}
   :name {}
   :desc {}})

(s/defschema Rollout
  {:id s/Uuid
   :name s/Str
   :desc s/Str})

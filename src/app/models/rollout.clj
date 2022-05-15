(ns app.models.rollout
  (:require [schema.core :as s]))

(s/defschema Rollout
             {:crux.db/id  s/Uuid
              :id          s/Uuid
              :name        s/Str
              :description s/Str
              :created-at  s/Str})

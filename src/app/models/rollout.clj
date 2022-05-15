(ns app.models.rollout
  (:require [schema.core :as s]))

(s/defschema Rollout
             {:crux.db/id  s/Uuid
              :rollout/id          s/Uuid
              :rollout/name        s/Str
              :rollout/description s/Str
              :rollout/created-at  s/Str})

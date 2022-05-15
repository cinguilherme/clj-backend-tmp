(ns app.wire.out.rollout
  (:require [schema.core :as s]))

(s/defschema OutRollout
             {:id s/Uuid
              :name s/Str
              :desc s/Str
              :created-at s/Str})

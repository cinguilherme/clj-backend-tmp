(ns app.wire.in.rollout
  (:require [schema.core :as s]))

(s/defschema InNewRollout
  {:name s/Str
   :desc s/Str
   (s/optional-key :id) s/Uuid})

(comment

  (s/explain InNewRollout)

  )

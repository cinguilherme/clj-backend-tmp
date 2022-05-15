(ns app.wire.in.rollout
  (:require [schema.core :as s]))

(s/defschema InNewRollout
             {:name s/Str :desc s/Str})

(comment

  (s/explain InNewRollout)

  )

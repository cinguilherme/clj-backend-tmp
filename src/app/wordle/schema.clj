(ns app.wordle.schema
  (:require [schema.core :as s]))

(s/defschema TestResult
  {:match?              s/Bool
   :found-in            [s/Str]
   :found-correct-place [s/Str]})

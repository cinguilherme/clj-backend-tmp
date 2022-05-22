(ns app.wire.out.wordle
  (:require [schema.core :as s]))

(s/defschema TestResultWire
  {:found-in [s/Str]
   :in-positions [s/Str]})

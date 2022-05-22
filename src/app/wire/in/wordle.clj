(ns app.wire.in.wordle
  (:require [schema.core :as s]))

(s/defschema WordleTestIn
  {:test  s/Str
   :word s/Str})

(s/defschema WordleTestMultIn
  {:test  s/Str
   :words [s/Str]})

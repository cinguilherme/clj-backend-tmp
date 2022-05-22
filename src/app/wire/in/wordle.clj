(ns app.wire.in.wordle
  (:require [schema.core :as s]))

(s/defschema WordleTestIn
  {:try  s/Str
   :word s/Str})

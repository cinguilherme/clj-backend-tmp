(ns app.mongo-doc.logic
  (:require [schema.core :as s]))

(s/defschema DocumentSchema
  {:name s/Str
   :age s/Num})



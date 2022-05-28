(ns app.transaction.schemata
  (:require [schema.core :as s]))

(s/defschema Transaction
  {:id     s/Uuid
   :amount s/Num})

(s/defschema Account
  {:id s/Uuid
   :ballance s/Num
   :transactions [Transaction]})

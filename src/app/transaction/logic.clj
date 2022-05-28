(ns app.transaction.logic
  (:require [schema.core :as s]
            [app.transaction.schemata :as s.transaction]))

(s/defn validate-transaction-amount
  [transaction :- s.transaction/Transaction
   account :- s.transaction/Account]
  (if (> (get transaction :amount) (get account :ballance))
    {:invalid-transaction "Transaction amount is greater than account balance"}
    {:valid-transaction "Transaction amount is less or equal than account balance"}))

(s/defn add-transaction-to-account
  [transaction :- s.transaction/Transaction
   account :- s.transaction/Account]
  (-> account
      (assoc :transactions (conj (-> account :transactions) transaction))
      (assoc :ballance (- (-> account :ballance) (-> transaction :amount)))))

(s/defn add-transaction
  [transaction :- s.transaction/Transaction
   account :- s.transaction/Account]
  (let [validation-result (validate-transaction-amount transaction account)]
    (if (:valid-transaction validation-result)
      (add-transaction-to-account transaction account)
      validation-result)))

(comment
  (add-transaction
    {:id "1" :amount 101}
    {:id "1" :ballance 100 :transactions []})

  (add-transaction-to-account
    {:id "1" :amount 100}
    {:id "1" :ballance 100 :transactions []}))

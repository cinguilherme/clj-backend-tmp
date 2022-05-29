(ns app.transaction.logic
  (:require [schema.core :as s]
            [clj-time.core :as time]
            [clj-time.coerce :as tc]
            [app.utils :refer [tap]]
            [app.transaction.schemata :as s.transaction]))

;; define a function that validates that accounts the latest transaction is at least one minute older than the transaction
(defn validate-transaction-timestamp [transaction account]
  "Validates that the transaction timestamp is at least one minute older than the latest transaction timestamp"
  (let [last-timestamp (-> account :transaction last :timestamp)
        diff (tap (- last-timestamp (:timestamp transaction)))]
    (if (not (nil? last-timestamp))
      (if (> diff 60000)
        true
        false)
      true)))

(time/now)

(tc/from-date #inst "2022-10-10T10:10:10.000Z")
(tc/to-date-time (tc/from-date #inst "2022-10-10T10:10:10.000Z"))

;; test the function validate-transaction-timestamp
(validate-transaction-timestamp
  {:id "123" :amount 100 :timestamp #inst "2020-10-10T10:10:10.000Z"}
  {:id "123" :ballance 100 :transactions [{:id "123" :amount 100 :timestamp #inst "2020-10-10T10:10:10.000Z"}]})

(s/defn validate-transaction-at-least-1-min-older-than-last :- s/Bool
  [transaction :- s.transaction/Transaction
   account :- s.transaction/Account]
  )

(s/defn validate-transaction-amount :- s/Bool
  [transaction :- s.transaction/Transaction
   account :- s.transaction/Account]
  (> (get transaction :amount) (get account :ballance)))

(s/defn add-transaction-to-account :- s.transaction/Account
  [transaction :- s.transaction/Transaction
   account :- s.transaction/Account]
  (-> account
      (assoc :transactions (conj (-> account :transactions) transaction))
      (assoc :ballance (- (-> account :ballance) (-> transaction :amount)))))

(s/defn add-transaction :- {:valid? s/Bool :account s.transaction/Account}
  [transaction :- s.transaction/Transaction
   account :- s.transaction/Account]
  (let [validation-result (validate-transaction-amount transaction account)]
    (if (:valid-transaction validation-result)
      {:valid? validation-result :account (add-transaction-to-account transaction account)}
      {:valid? validation-result :account account})))

(comment
  (add-transaction
    {:id "1" :amount 100}
    {:id "1" :ballance 100 :transactions []})

  (add-transaction-to-account
    {:id "1" :amount 50 :timestamp #inst "2022-01-01T00:00:00.000Z"}
    {:id "1" :ballance 100 :transactions []})

  )

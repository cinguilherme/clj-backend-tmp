(ns app.kafka-franzy-playground
  (:require [franzy.clients.consumer.client :as c]))

(c/make-consumer
  {:topic     "input"
   :partition 1
   :offset    0
   :key       1
   :value     1})

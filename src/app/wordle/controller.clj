(ns app.wordle.controller
  (:require [schema.core :as s]
            [app.wordle.logic :as wordle.logic]
            [app.wordle.schema :as s.wordle]))

(s/defn test-wordle :- s.wordle/TestResult
  [{:keys [test word]}]
  (wordle.logic/complete test word))

(s/defn test-mult-wordle :- [s.wordle/TestResult]
  [{:keys [test words]}]
  (wordle.logic/complete-multiple test words))

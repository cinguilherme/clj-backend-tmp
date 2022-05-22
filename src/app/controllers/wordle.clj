(ns app.controllers.wordle
  (:require [app.wordle.logic :as wordle.logic]))

(defn test-wordle [{:keys [test word]}]
  (wordle.logic/complete test word))

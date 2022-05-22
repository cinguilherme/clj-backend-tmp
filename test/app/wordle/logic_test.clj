(ns app.wordle.logic-test
  (:require [clojure.test :refer :all])
  (:require [app.wordle.logic :refer [complete]]))

(deftest complete-test
  (testing "should match completely a word and a test"
    (is (true?
          (-> (complete "bar" "bar") :match?)))
    (is (= ["b" "a" "r"]
           (-> (complete "bar" "bar") :found-correct-place))))

  (testing "should partially match a word and a test"
    (is (false?
          (-> (complete "car" "bar") :match?)))
    (is (= ["_" "a" "r"]
           (-> (complete "car" "bar") :found-correct-place)))
    (is (= ["a" "r"]
           (-> (complete "car" "bar") :found-in))))

  (testing "should not match any in word and a test"
    (is (false?
          (-> (complete "zum" "bar") :match?)))
    (is (= ["_" "_" "_"]
           (-> (complete "zum" "bar") :found-correct-place)))
    (is (= []
           (-> (complete "zum" "bar") :found-in)))))
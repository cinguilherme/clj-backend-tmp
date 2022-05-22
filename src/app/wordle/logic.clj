(ns app.wordle.logic
  (:require [schema.core :as s]
            [app.wordle.schema :as s.wordle]))

(def not-empty? (comp not empty?))

(def pool-size-5 ["root" "boot" "coat" "tree" "bark"])

(defn- char-in-word? [c word]
  (clojure.string/includes? word c))

(defn- str->vec-char [string]
  (->> string vec (mapv #(str %))))

(defn- vec-char->pos-char [coll]
  (loop [m [] c coll i 0]
    (if (empty? c)
      m
      (recur (conj m {:pos i :char (first c)}) (rest c) (inc i)))))

(defn- all-indexes-of [word c]
  (loop [index 0 col []]
    (let [pos (clojure.string/index-of word c index)]
      (if (nil? pos)
        col
        (recur (inc pos) (conj col pos))))))

(defn- ->index-found [word c pos]
  (let [indexes (all-indexes-of word c)]
    {:char    c
     :pos     pos
     :indexes indexes}))

(defn- char-check->char-check-rich [char-check]
  (let [pos (:pos char-check)
        indexes (:indexes char-check)]
    (-> char-check
        (assoc :found? (not-empty? indexes))
        (assoc :correct-position? (->> indexes
                                       (filterv #(= pos %))
                                       not-empty?)))))

(defn- index-found->vec-char-check-rich [test word]
  (->> test
       str->vec-char
       vec-char->pos-char
       (mapv #(->index-found word (:char %) (:pos %)))
       (mapv char-check->char-check-rich)))

(defn- get-found-in [coll]
  (reduce (fn [acc nex] (if (:found? nex)
                          (conj acc (:char nex))
                          acc)) [] coll))

(defn- get-correct-places [coll]
  (reduce (fn [acc nex] (if (:correct-position? nex)
                          (conj acc (:char nex))
                          (conj acc "_"))) [] coll))

(s/defn complete :- s.wordle/TestResult
  [test word]
  (let [checks (index-found->vec-char-check-rich test word)
        found-in (-> checks get-found-in)
        correct-places (-> checks get-correct-places)
        match? (= test word)]
    {:match?              match?
     :found-in            found-in
     :found-correct-place correct-places}))

(s/defn complete-multiple :- [s.wordle/TestResult]
  [test words]
  (mapv #(complete test %) words))

(defn sample-word []
  (-> pool-size-5 shuffle first))


(comment

  (def wor "car")
  (def bor "bar")

  (index-found->vec-char-check-rich "car" "bar")

  (complete "car" "bar")
  (complete "bar" "bar")

  (check-against bor wor)

  (vec-char->pos-char (-> "abracadabra" str->vec-char))

  (sample-word)
  (str->vec-char "car")
  (clojure.string/index-of "abacate" "a")
  (all-indexes-of "abacate" "a")
  (clojure.string/index-of "abacate" "z")
  (char-in-word? "c" "car")
  (char-in-word? "x" "car")
  )

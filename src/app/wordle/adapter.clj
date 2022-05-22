(ns app.wordle.adapter
  (:require [schema.core :as s]
            [app.wire.in.wordle :as wire.in]
            [app.wire.out.wordle :as wire.out]))

(s/defn edn-params->wire-in :- wire.in/WordleTestIn
  [{:keys [test word]}]
  {:test test :word word})

(s/defn edn-params->mult-wire-in :- wire.in/WordleTestMultIn
  [{:keys [test words]}]
  {:test test :words words})

(s/defn test-check->wire-out-result :- wire.out/TestResultWire
  [{:keys [found-in found-correct-place]} ]
  {:found-in         found-in
   :correct-position found-correct-place})

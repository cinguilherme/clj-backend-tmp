(ns app.wordle.adapter
  (:require [schema.core :as s]
            [app.wire.in.wordle :as wire.in]
            [app.wire.out.wordle :as wire.out]))

(s/defn edn-params->wire-in :- wire.in/WordleTestIn
  [{:keys [test word]}]
  {:test test :word word})

(s/defn test-check->wire-out-result :- app.wire.out.wordle/TestResultWire
  [{:keys [found-in found-correct-place]} ]
  {:found-in         found-in
   :correct-position found-correct-place})

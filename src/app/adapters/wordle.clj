(ns app.adapters.wordle)

(defn edn-params->test-word [edn-params]
  {:test (:test edn-params) :word (:word edn-params)})

(defn test-check->wire [{:keys [found-in found-correct-place]}]
  {:found-in         found-in
   :correct-position found-correct-place})

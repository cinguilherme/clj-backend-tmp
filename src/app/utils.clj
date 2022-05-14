(ns app.utils)

(defn tap [v] (do (println v) v))

(def not-nil? (comp not nil?))

(comment

  (not-nil? nil)
  (not-nil? true)

  )

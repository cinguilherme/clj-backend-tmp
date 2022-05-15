(ns app.utils
  (:import (java.util Date)))

(defn tap [v] (do (println v) v))

(def not-nil? (comp not nil?))

(defn now []
  (new Date))

(comment

  (now)

  (not-nil? nil)
  (not-nil? true)

  )

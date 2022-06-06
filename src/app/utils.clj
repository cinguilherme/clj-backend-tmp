(ns app.utils
  (:require [clojure.string :as string]) 
  (:import (java.util Date)))

(defn tap [v] (do (println v) v))

(def not-nil? (comp not nil?))

(defn now []
  (new Date))


(comment

  (now)

  (not-nil? nil)
  (not-nil? true))

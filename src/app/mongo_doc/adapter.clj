(ns app.mongo-doc.adapter
  (:import (org.bson.types ObjectId)))

(defn wire-in->new-doc [{:keys [name age ]}]
  {:_id (ObjectId.) :name name :age age})

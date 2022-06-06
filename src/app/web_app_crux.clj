(ns app.web-app-crux
  (:require [crux.api :as crux]
            [app.components.CruxDb :as CruxDb]
            [monger.core :as mg]
            [monger.collection :as mc]
            [com.stuartsierra.component :as component]
            [jackdaw.admin :as ja]
            [jackdaw.serdes.edn :as jse]
            [jackdaw.client :as jc]
            [jackdaw.client.log :as jcl]
            [datomic.api :as d]
            [datomic.client.api :as q])
  (:import (org.bson.types ObjectId))
  )

;;crux
(def node (crux/start-node {}))

(def nodex (component/start (CruxDb/new-db)))

(:db-node nodex)
;;end crux

;;mongo
(def mongo-uri (System/getenv "MONGO_ATLAS_URI"))

(let [{:keys [conn db]} (mg/connect-via-uri mongo-uri)]
  (do
    (mc/insert db "documents" {:_id (ObjectId.) :name "gui" :age 35})
    (mg/disconnect conn)))

(let [{:keys [conn db]} (mg/connect-via-uri mongo-uri)]
  (do
    (println (mc/find-maps db "documents"))
    (mg/disconnect conn)))

;;end mongo


;;datomic
(def datomic-str "datomic:dev://localhost:4334/hello")

(def db-uri "datomic:mem://hello")

(d/create-database db-uri)
(def conn (d/connect db-uri))
(println conn)

@(d/transact conn [{:db/doc "hello doc Hermanation"}])
(def db (d/db conn))
(d/q '{:find  [(pull ?bla [*])]
       :in    [$]
       :where [[?bla :db/doc ?x]
               [(clojure.string/includes? ?x "Hermanation")]]
       } db)

;; movies
;;end datomic



(comment

  (def thing {:crux.db/id 1
              :thing/name "thing"})
  (def thing2 {:crux.db/id 2
               :thing/name "thing2"})

  (def more-complex-thing {:crux.db/id (random-uuid)
                           :thing/name "thingx"})

  (crux/submit-tx node [[:crux.tx/put thing]])
  (crux/submit-tx node [[:crux.tx/put thing2]])
  (crux/submit-tx node [[:crux.tx/put more-complex-thing]])
  (crux/submit-tx (:db-node nodex) [[:crux.tx/put thing]])



  (crux/q
    (crux/db node)
    '{:find  [(pull ?t1 [:thing/name])]
      :where [[?t1 :crux.db/id ?some-id]
              [?t1 :thing/name ?with-name]]})

  (crux/q
    (crux/db (:db-node nodex))
    '{:find  [(pull ?t1 [*])]
      :where [[?t1 :crux.db/id 1]]})

  )





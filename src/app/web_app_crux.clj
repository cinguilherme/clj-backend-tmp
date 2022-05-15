(ns app.web-app-crux
  (:require [crux.api :as crux]
            [app.components.CruxDb :as CruxDb]
            [com.stuartsierra.component :as component]))

(def node (crux/start-node {}))

(def nodex (component/start (CruxDb/new-db)))

(:db-node nodex)

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
   '{:find [(pull ?t1 [:thing/name])]
     :where [[?t1 :crux.db/id ?some-id]
             [?t1 :thing/name ?with-name]]})

  (crux/q
   (crux/db (:db-node nodex))
   '{:find [(pull ?t1 [*])]
     :where [[?t1 :crux.db/id 1]]})


  )





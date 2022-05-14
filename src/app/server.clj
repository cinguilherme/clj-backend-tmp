(ns app.server
  (:require [io.pedestal.http.route :as route]
            [io.pedestal.http :as http]))


(defn get-todo [_]
  {:status 200 :body {:well 101}})

(def routes
  (route/expand-routes
   #{["/todo" :get get-todo :route-name :list-todo]}))

(def service-map
  {::http/routes routes
   ::http/type :jetty
   ::http/port 8081
   ::http/join? false})

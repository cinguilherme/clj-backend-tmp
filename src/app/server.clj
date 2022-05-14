(ns app.server
  (:require [io.pedestal.http.route :as route]
            [io.pedestal.http :as http]))


(defn get-todo [_]
  {:status 200 :body {:well 101}})

(defn get-rollouts [_]
  {:status 200 :body [{:name "x" :desc "x is cool" :active? false}]})

(def routes
  (route/expand-routes
    #{["/todo" :get get-todo :route-name :list-todo]
      ["/rollouts" :get get-rollouts :route-name :list-rollouts]}))

(def service-map
  {::http/routes routes
   ::http/type   :jetty
   ::http/port   8081
   ::http/join?  false})

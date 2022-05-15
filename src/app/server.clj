(ns app.server
  (:require [io.pedestal.http.route :as route]
            [io.pedestal.http :as http]))


(defn get-todo [_]
  {:status 200 :body {:well 101}})

(defn get-rollouts [_]
  {:status 200 :body [{:name "x" :desc "x is cool" :active? false}]})

(defn get-stuff [request]
  (println request)
  {:status 200 :body [{:name "x" :desc "XXX is cool" :active? false}
                      {:request request}]})

(defn get-routes [_]
  {:status 200 :body []})


(defn make-routes [component-interceptor]
  (route/expand-routes
   #{["/todo" :get get-todo :route-name :list-todo]
     ["/rollouts" :get get-rollouts :route-name :list-rollouts]
     ["/stuff" :get get-stuff :route-name :list-stuff]
     ["/stuffx" :get [component-interceptor get-stuff] :route-name :list-stuffs]
     ["/routes" :get get-routes :route-name :list-routes]}))

(def service-map
  {:env          :dev
   ;::http/routes routes
   ::http/type   :jetty
   ::http/port   8081
   ::http/join?  false})

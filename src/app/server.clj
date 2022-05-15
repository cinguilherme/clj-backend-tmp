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
(def db-interceptor
  {:name ::db-interceptor
   :enter (fn [context]
            (update context :request assoc ::database (atom {})))})

(def add-components
  {:name ::components
   :enter (fn [context]
            (update context :request assoc ::components {:db {} :cache {} :config {}}))})

(def routes
  (route/expand-routes
    #{["/todo" :get get-todo :route-name :list-todo]
      ["/rollouts" :get get-rollouts :route-name :list-rollouts]
      ["/stuff" :get get-stuff :route-name :list-stuff]
      ["/stuffx" :get [db-interceptor add-components get-stuff] :route-name :list-stuffs]}))

(defn make-routes [component-interceptor]
  (route/expand-routes
    #{["/todo" :get get-todo :route-name :list-todo]
      ["/rollouts" :get get-rollouts :route-name :list-rollouts]
      ["/stuff" :get get-stuff :route-name :list-stuff]
      ["/stuffx" :get [component-interceptor get-stuff] :route-name :list-stuffs]}))

(def service-map
  {:env          :dev
   ::http/routes routes
   ::http/type   :jetty
   ::http/port   8081
   ::http/join?  false})

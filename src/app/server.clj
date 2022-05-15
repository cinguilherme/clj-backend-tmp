(ns app.server
  (:require [io.pedestal.http.route :as route]
            [io.pedestal.http :as http]
            [io.pedestal.http.body-params :as body-params]))


(defn get-todo [_]
  {:status 200 :body {:well 101}})

(defn get-rollouts [_]
  {:status 200 :body [{:name "x" :desc "x is cool" :active? false}]})

(defn get-stuff [request]
  (println request)
  {:status 200 :body [{:name "x" :desc "XXX is cool" :active? false}
                      {:request request}]})

(defn new-rollout
  [{:keys [components edn-params] :as request}]
  (println request)
  (println edn-params)
  (let [id (random-uuid)]
    {:status 201 :body {:created :ok :rollout (assoc edn-params :id id)}})
  )

(defn make-routes [component-interceptor]
  (route/expand-routes
    #{["/todo" :get get-todo :route-name :list-todo]
      ["/rollouts" :get get-rollouts :route-name :list-rollouts]
      ["/stuff" :get get-stuff :route-name :list-stuff]
      ["/stuffx" :get [component-interceptor get-stuff] :route-name :list-stuffs]

      ["/rollout" :post [component-interceptor (body-params/body-params) new-rollout] :route-name :new-rollout]
      }))

(def service-map
  {:env         :dev
   ;::http/routes routes
   ::http/type  :jetty
   ::http/port  8081
   ::http/join? false})

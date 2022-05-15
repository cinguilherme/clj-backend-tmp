(ns app.server
  (:require [io.pedestal.http.route :as route]
            [io.pedestal.http :as http]
            [io.pedestal.http.body-params :as body-params]
            [app.controllers.rollout :as controllers.rollout]
            [app.adapters.rollout :as adapters.rollout]
            [app.utils :as utils]))

(defn with-components [request]
  (:app.components.Server/components request))

(defn get-todo [_]
  {:status 200 :body {:well 101}})

(defn get-rollouts [_]
  {:status 200 :body [{:name "x" :desc "x is cool" :active? false}]})

(defn get-stuff [request]
  {:status 200 :body [{:name "x" :desc "XXX is cool" :active? false}
                      {:request request}]})

(defn new-rollout
  [{:keys [edn-params] :as request}]
  (let [components (with-components request)]
    (let [rollout-wire-out (-> edn-params
                               adapters.rollout/wire-in->model
                               (controllers.rollout/new-rollout! components)
                               adapters.rollout/model->wire)]
      {:status 201 :body rollout-wire-out}))
  )

(defn get-rollout [request]
  (let [components (with-components request)
        into->wire (partial map adapters.rollout/model->wire)
        rollouts (-> components
                     controllers.rollout/get-rollouts!
                     into->wire
                     vec)]
    {:status 200 :body {:rollouts rollouts}}))

(defn make-routes [component-interceptor]
  (route/expand-routes
    #{["/todo" :get get-todo :route-name :list-todo]
      ["/rollouts" :get get-rollouts :route-name :list-rollouts]
      ["/stuff" :get get-stuff :route-name :list-stuff]
      ["/stuffx" :get [component-interceptor get-stuff] :route-name :list-stuffs]

      ["/rollout" :post [component-interceptor (body-params/body-params) new-rollout] :route-name :new-rollout]
      ["/rollout" :get [component-interceptor (body-params/body-params) get-rollout] :route-name :get-rollout]
      }))

(def service-map
  {:env         :dev
   ;::http/routes routes
   ::http/type  :jetty
   ::http/port  8081
   ::http/join? false})

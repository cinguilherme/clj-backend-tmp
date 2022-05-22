(ns app.server
  (:require [io.pedestal.http.route :as route]
            [io.pedestal.http :as http]
            [io.pedestal.http.body-params :as body-params]
            [app.controllers.rollout :as controllers.rollout]
            [app.wordle.controller :as controllers.wordle]
            [app.adapters.rollout :as adapters.rollout]
            [app.wordle.adapter :as adapters.wordle]
            [app.utils :as utils :refer [tap]]))

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

(defn get-rollout-by-id [request]
  (let [components (with-components request)
        id (-> request :path-params :id)
        into->wire (partial map adapters.rollout/model->wire)
        _ (println id)
        rollout (-> (controllers.rollout/get-rollouts-by-id! id components)
                    tap
                    into->wire
                    vec)]
    {:status 200 :body {:rollout rollout}}))

(defn test-wordle-handler [{:keys [edn-params]}]
  (let [body (-> edn-params
                 adapters.wordle/edn-params->wire-in
                 tap
                 controllers.wordle/test-wordle
                 tap
                 adapters.wordle/test-check->wire-out-result)]
    {:status 200 :body body}))

(defn test-mult-wordle-handler [{:keys [edn-params]}]
  (let [mult-parser (partial mapv #(adapters.wordle/test-check->wire-out-result %))
        body (-> edn-params
                 adapters.wordle/edn-params->mult-wire-in
                 controllers.wordle/test-mult-wordle
                 mult-parser)]
    {:status 200 :body body}))

(defn make-routes [component-interceptor]
  (route/expand-routes
    #{["/todo" :get get-todo :route-name :list-todo]
      ["/rollouts" :get get-rollouts :route-name :list-rollouts]
      ["/stuff" :get get-stuff :route-name :list-stuff]
      ["/stuffx" :get [component-interceptor get-stuff] :route-name :list-stuffs]

      ["/rollout" :post [component-interceptor (body-params/body-params) new-rollout] :route-name :new-rollout]
      ["/rollout" :get [component-interceptor (body-params/body-params) get-rollout] :route-name :get-rollout]
      ["/rollout/:id" :get [component-interceptor (body-params/body-params) get-rollout-by-id] :route-name :get-rollout-by-id]

      ["/wordle/test" :post [component-interceptor (body-params/body-params) test-wordle-handler] :route-name :wordle-test]
      ["/wordle/test-mult" :post [component-interceptor (body-params/body-params) test-mult-wordle-handler] :route-name :wordle-test-mult]
      }))

(def service-map
  {:env         :dev
   ;::http/routes routes
   ::http/type  :jetty
   ::http/port  8081
   ::http/join? false})

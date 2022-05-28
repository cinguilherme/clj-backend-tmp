(ns app.server
  (:require [clojure.data.json :as json]
            [io.pedestal.http.route :as route]
            [io.pedestal.http :as http]
            [io.pedestal.http.body-params :as body-params]
            [io.pedestal.interceptor.helpers :as interceptor]
            [io.pedestal.http.content-negotiation :as conneg]
            [app.controllers.rollout :as controllers.rollout]
            [app.wordle.controller :as controllers.wordle]
            [app.adapters.rollout :as adapters.rollout]
            [app.mongo-doc.adapter :as mongo-doc.adapter]
            [app.wordle.adapter :as adapters.wordle]
            [app.utils :as utils :refer [tap]]
            [monger.core :as mg]
            [monger.collection :as mc]))

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
                 controllers.wordle/test-wordle
                 adapters.wordle/test-check->wire-out-result)]
    {:status 200 :body body}))

(defn test-mult-wordle-handler [{:keys [edn-params]}]
  (let [mult-parser (partial mapv #(adapters.wordle/test-check->wire-out-result %))
        body (-> edn-params
                 adapters.wordle/edn-params->mult-wire-in
                 controllers.wordle/test-mult-wordle
                 mult-parser)]
    {:status 200 :body body}))

(defn new-wordle-handler [_]
  {:status 200 :body {:word (controllers.wordle/new-word)}})

(defn mongo-get-docs [request]
  (let [{:keys [mongo]} (-> request with-components)
        docs (vec (mc/find-maps (-> mongo :mongo :db) "documents"))]

    {:status 200 :body docs}))

(defn mongo-new-docs [{:keys [edn-params] :as request}]
  (let [{:keys [mongo]} (-> request with-components)
        doc (tap (-> edn-params -> mongo-doc.adapter/wire-in->new-doc))
        docs (tap (mc/insert (-> mongo :mongo :db) "documents" doc))]
    {:status 200 :body docs}))


(def supported-types ["text/html" "application/edn" "application/json" "text/plain"])

(def content-neg-intc (conneg/negotiate-content supported-types))

(def coerce-body
  {:name ::coerce-body
   :leave
   (fn [context]
     (let [accepted (get-in context [:request :accept :field] "text/plain")
           response (get context :response)
           body (get response :body)
           coerced-body (case accepted
                          "text/html" body
                          "text/plain" body
                          "application/edn" (pr-str body)
                          "application/json" (json/write-str body))
           updated-response (assoc response
                              :headers {"Content-Type" accepted}
                              :body coerced-body)]
       (assoc context :response updated-response)))})

(defn make-routes [component-interceptor]
  (route/expand-routes
    #{["/todo" :get get-todo :route-name :list-todo]
      ["/rollouts" :get get-rollouts :route-name :list-rollouts]
      ["/stuff" :get get-stuff :route-name :list-stuff]
      ["/stuffx" :get [component-interceptor get-stuff] :route-name :list-stuffs]


      ["/rollout" :post [component-interceptor (body-params/body-params) new-rollout] :route-name :new-rollout]
      ["/rollout" :get [component-interceptor (body-params/body-params) get-rollout] :route-name :get-rollout]
      ["/rollout/:id" :get [component-interceptor (body-params/body-params) get-rollout-by-id] :route-name :get-rollout-by-id]


      ["/wordle/new" :get [component-interceptor (body-params/body-params) content-neg-intc coerce-body new-wordle-handler] :route-name :wordle-new]
      ["/wordle/test" :post [component-interceptor (body-params/body-params) test-wordle-handler] :route-name :wordle-test]
      ["/wordle/test-mult" :post [component-interceptor (body-params/body-params) test-mult-wordle-handler] :route-name :wordle-test-mult]

      ["/mongo/docs" :get [component-interceptor (body-params/body-params) content-neg-intc coerce-body mongo-get-docs] :route-name :mongo-docs]
      ["/mongo/docs" :post [component-interceptor (body-params/body-params) content-neg-intc coerce-body mongo-new-docs] :route-name :mongo-new-doc]
      }))

(def service-map
  {:env                   :dev
   ;::http/routes routes
   ::http/allowed-origins {:creds true :allowed-origins (constantly true)}
   ::http/type            :jetty
   ::http/port            8081
   ::http/join?           false})

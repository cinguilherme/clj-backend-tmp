(ns app.components.Server
  (:require [com.stuartsierra.component :as component]
            [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]))

(defn response [status body & {:as headers}]
  {:status status :body body :headers headers})

(def ok       (partial response 200))

(def echo
  {:name :echo
   :enter
   (fn [context]
     (let [request (:request context)
           response (ok context)]
       (assoc context :response response)))})

(def routes
  (route/expand-routes
   #{["/todo" :get echo :route-name :list-todo]}))

(defn create-server [port]
  (http/create-server
   {::http/routes routes
    ::http/type :jetty
    ::http/port port
    ::http/join? false}))

(defn start [port]
  (http/start (create-server port)))

(defrecord Pedestal [routes port]
  component/Lifecycle

  (start [this]
    (assoc this :server (start port)))

  (stop [this]
    (assoc this :server nil)))


(defn new-pedestal [port]
  (map->Pedestal {:port port}))
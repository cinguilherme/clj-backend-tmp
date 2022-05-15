(ns app.components.Config
  (:require [com.stuartsierra.component :as component]))

(defn read-config [env]
  (let [config-name (str "resources/" env ".configs.edn")
        conf (slurp config-name)]
    (println env)
    (println conf)
    conf))


(defrecord Config [env config]
  component/Lifecycle

  (start [this]
    (if (-> this :config nil?)
      (assoc this :config (read-config env))
      this))

  (stop [this]
    (assoc this :config nil)))


(defn new-config [env]
  (map->Config {:env env}))

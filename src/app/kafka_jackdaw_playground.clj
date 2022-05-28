(ns app.kafka-jackdaw-playground
  (:require [jackdaw.admin :as ja]
            [jackdaw.serdes.edn :as jse]
            [jackdaw.client :as jc]
            [jackdaw.streams :as j]
            [jackdaw.client.log :as jcl]
            [app.utils :refer [tap]]))

;; kafka jackdaw

(def admin-client-config
  {"bootstrap.servers" "localhost:9092"})

(def producer-config
  {"bootstrap.servers" "localhost:9092"})

(def consumer-config
  {"bootstrap.servers"  "localhost:9092"
   "auto.offset.reset"  "earliest"
   "enable.auto.commit" "false"})

(defn topic-config
  "Takes a topic name and returns a topic configuration map, which may
  be used to create a topic or produce/consume records."
  [topic-name]
  {:topic-name         topic-name
   :partition-count    1
   :replication-factor 1
   :key-serde          (jse/serde)
   :value-serde        (jse/serde)})

(defn app-config
  "Returns the application config."
  []
  {"application.id" "pipe"
   "bootstrap.servers" "localhost:9092"
   "cache.max.bytes.buffering" "0"})

(defn build-topology
  [builder]
  (-> builder
      (j/kstream (topic-config "input"))
      (j/peek (fn [[k v]]
                (tap (str {:key k :value (clojure.string/upper-case v)}))))
      (j/to (topic-config "output")))
  builder)

(defn start-app
  "Starts the stream processing application."
  [app-config]
  (let [builder (tap (j/streams-builder))
        topology (tap (build-topology builder))
        app (j/kafka-streams topology app-config)]
    (j/start app)
    (tap "pipe is up")
    app))

(defn stop-app
  "Stops the stream processing application."
  [app]
  (j/close app)
  (tap "pipe is down"))

(defn create-topic
  "Takes a topic config and creates a Kafka topic."
  [topic-config]
  (with-open [client (ja/->AdminClient admin-client-config)]
    (ja/create-topics! client [topic-config])))

(defn list-topics
  "Returns a list of Kafka topics."
  []
  (with-open [client (ja/->AdminClient admin-client-config)]
    (ja/list-topics client)))

(defn topic-exists?
  "Takes a topic name and returns true if the topic exists."
  [topic-config]
  (with-open [client (ja/->AdminClient admin-client-config)]
    (ja/topic-exists? client topic-config)))

(defn publish
  ([topic-config value]
   (with-open [client (jc/producer producer-config topic-config)]
     @(jc/produce! client topic-config value))
   nil)
  ([topic-config key value]
   (with-open [client (jc/producer producer-config topic-config)]
     @(jc/produce! client topic-config key value))
   nil)
  ([topic-config partition key value]
   (with-open [client (jc/producer producer-config topic-config)]
     @(jc/produce! client topic-config partition key value))
   nil))

(defn get-records
  "Takes a topic config, consumes from a Kafka topic, and returns a
  seq of maps."
  ([topic-config]
   (get-records topic-config 200))
  ([topic-config polling-interval-ms]
   (let [client-config (assoc consumer-config
                         "group.id"
                         (str "5f72dcac-120c-488c-89c0-011b7109fd32"))]
     (with-open [client (tap (jc/subscribed-consumer client-config [topic-config]))]
       (doall (jcl/log client polling-interval-ms seq))))))

(defn get-keyvals
  "Takes a topic config, consumes from a Kafka topic, and returns a
  seq of key-value pairs."
  ([topic-config]
   (get-keyvals topic-config 20))

  ([topic-config polling-interval-ms]
   (map (juxt :key :value) (get-records topic-config polling-interval-ms))))

(defn counted-messages [topic-config]
  (with-open [client (jc/consumer consumer-config topic-config)]
    (jcl/count-messages client topic-config)))


(def test-topic-config (topic-config "test"))
(def input-topic-config (topic-config "input"))
(def output-topic-config (topic-config "output"))

(comment
  (create-topic test-topic-config)
  (create-topic input-topic-config)
  (create-topic output-topic-config)
  (topic-exists? test-topic-config)
  (list-topics)

  (def app (start-app (app-config)))
  (stop-app app)


  (publish input-topic-config nil "hellox")

  (get-keyvals output-topic-config)

  (publish test-topic-config nil "hello")
  (publish test-topic-config 0 :name {:name "bob"})

  (get-records test-topic-config 1000)
  (get-keyvals test-topic-config)
  (counted-messages test-topic-config))


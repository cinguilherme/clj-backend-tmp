(ns app.adapters.rollout
  (:require [schema.core :as s]
            [app.utils :as utils]
            [app.models.rollout :as models.rollout]
            [app.wire.in.rollout :as wire.in.rollout]
            [app.wire.out.rollout :as wire.out.rollout]))

(s/defn wire-in->model :- models.rollout/Rollout
  [{:keys [name desc]} :- wire.in.rollout/InNewRollout]
  {:crux.db/id  (random-uuid)
   :rollout/id          (random-uuid)
   :rollout/name        name
   :rollout/description desc
   :rollout/created-at  (utils/now)})

(s/defn model->wire :- wire.out.rollout/OutRollout
  [rollout :- models.rollout/Rollout]
  {:id         (:rollout/id rollout)
   :name       (:rollout/name rollout)
   :desc       (:rollout/description rollout)
   :created-at (:rollout/created-at rollout)})

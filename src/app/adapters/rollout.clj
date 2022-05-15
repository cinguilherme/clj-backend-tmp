(ns app.adapters.rollout
  (:require [schema.core :as s]
            [app.utils :as utils]
            [app.models.rollout :as models.rollout]
            [app.wire.in.rollout :as wire.in.rollout]
            [app.wire.out.rollout :as wire.out.rollout]))

(s/defn wire-in->model :- models.rollout/Rollout
        [{:keys [name desc]} :- wire.in.rollout/InNewRollout]
        {:crux.db/id  (random-uuid)
         :id          (random-uuid)
         :name        name
         :description desc
         :created-at  (utils/now)})

(s/defn model->wire :- wire.out.rollout/OutRollout
        [rollout :- models.rollout/Rollout]
        {:id         (:id rollout)
         :name       (:name rollout)
         :desc       (:description rollout)
         :created-at (:created-at rollout)})

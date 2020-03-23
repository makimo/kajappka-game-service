(ns games.delivery.api.update
  (:require [games.delivery.api.utils :as u]
            [games.delivery.use-cases :refer [update-game]]
            [games.delivery.api.json :refer [game->json]]
            [games.core.entity.game :as game]
            [games.core.action :as action]
            [games.core.entity.owner :refer [make-owner]]
            [clojure.spec.alpha :as s]
            [games.delivery.api.spec :as a]))


(defn- action->response
  "Converts an action to a response."
  [{:keys [::action/error ::action/payload]}]
  (case error
    :authorization
    {:status 403
     :body   {:error "Access denied"}}
    :storage
    {:status 500
     :body   {:error "There was an error updating the game"}}
    nil
    {:status 200
     :body   {:data (game->json (:result payload))}}))


(defn- update
  "Updates new game based on the request."
  [id game caller]
  (->> game
       u/clean-game-data
       game/qualify-update-map
       (update-game id caller)
       action->response))


(defn dispatch
  "Handles a request to update the game."
  [id {:keys [body owner] :as request}]
  (if (s/valid? ::a/update-request body)
    (update id body (make-owner owner))
    (u/bad-request (s/explain-data ::a/update-request body))))

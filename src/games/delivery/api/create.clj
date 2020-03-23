(ns games.delivery.api.create
  (:require [clojure.spec.alpha :as s]
            [games.core.entity.game :as game]
            [games.delivery.use-cases :refer [create-game]]
            [games.delivery.api.json :refer [game->json]]
            [games.delivery.api.spec :as a]
            [games.core.action :as action]
            [games.delivery.api.utils :refer [bad-request clean-game-data]]))


(defn- action->response
  "Converts an action to a response."
  [{:keys [::action/error ::action/payload]}]
  (case error
    :storage
    {:status 500
     :body   {:error "There was an error creating the game"}}
    nil
    {:status 201
     :body   {:data (game->json (:result payload))}}))


(defn- create
  "Creates new game from the request."
  [{:keys [owner] :as game}]
  (->> game
       clean-game-data
       (merge {:owner owner})
       game/make-game
       create-game
       action->response))


(defn dispatch
  "Handles a request to create new game."
  [{:keys [body owner] :as request}]
  (if (s/valid? ::a/create-request body)
    (create (assoc body :owner owner))
    (bad-request (s/explain-data ::a/create-request body))))

(ns games.delivery.api.list
  (:require [clojure.spec.alpha :as s]
            [games.core.entity.game :as game]
            [games.core.entity :as entity]
            [games.delivery.use-cases :refer [list-games]]
            [games.delivery.api.json :refer [game->json]]
            [games.core.action :as action]))


(defn- action->response
  "Converts an action to a response."
  [{:keys [::action/error? ::action/payload]}]
  (if error?
    {:status 500
     :body   {:error "There was an error fetching the games"}}
    {:status 200
     :body   (map game->json (:result payload))}))


(defn dispatch
  "Handles a request to list games."
  []
  (action->response (list-games)))

(ns games.delivery.api.delete
  (:require [games.core.entity.game :as game]
            [games.core.entity :as entity]
            [games.delivery.use-cases :refer [delete-game]]
            [games.core.action :as action]
            [games.core.entity.owner :refer [make-owner]]
            [games.delivery.api.utils :refer [bad-request]]))


(defn- action->response
  "Returns HTTP response on action result."
  [{:keys [::action/error ::action/payload] :as action}]
  (case error
        :authorization
        {:status 403
         :body   {:error "Access denied"}}
        :storage
        {:status 500
         :body   {:error "There was an error deleting the game"}}
        nil
        {:status 200}))


(defn dispatch
  "Handles a request to delete a game."
  [{:keys [params owner] :as request}]
  (try
    (->> params
         :id
         entity/string->uuid
         (delete-game (make-owner owner))
         action->response)
  (catch java.lang.IllegalArgumentException e (bad-request "Invalid request"))))

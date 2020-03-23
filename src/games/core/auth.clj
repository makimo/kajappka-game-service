(ns games.core.auth
  (:require [games.core.entity.game :as game]
            [games.core.entity.owner :as owner]
            [games.core.action :as action]
            [games.delivery.storage :refer [store]]
            [mount.core :refer [defstate]]))


;; Check if request caller has access to the game.
(defstate can-modify? :start
  (fn [id caller]
    (let [game          (game/fetch store id)
          caller-id     (::owner/id caller)
          game-owner-id (get-in game [::game/owner ::owner/id])]
      (= caller-id game-owner-id))))

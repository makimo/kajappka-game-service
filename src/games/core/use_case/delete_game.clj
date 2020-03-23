(ns games.core.use-case.delete-game
  (:require [games.core.entity.game :as game]
            [games.core.entity.owner :as owner]
            [games.core.action :as action]
            [games.core.use-case :as uc]
            [games.core.auth :refer [can-modify?]]))


(defn delete-game [{:keys [storage]}]
  (fn [caller id]
    (if (can-modify? id caller)
      (->> (game/delete storage id)
           (uc/result->action :game/delete))
      (action/make-error :game/delete :authorization))))

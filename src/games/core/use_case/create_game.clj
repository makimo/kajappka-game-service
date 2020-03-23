(ns games.core.use-case.create-game
  (:require [games.core.entity.game :as game]
            [games.core.use-case :as uc]))


(defn create-game [{:keys [storage]}]
  (fn [entity]
    (->> (game/save storage entity)
        (uc/result->action :game/create))))

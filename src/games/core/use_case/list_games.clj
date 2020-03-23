(ns games.core.use-case.list-games
  (:require [games.core.entity.game :as game]
            [games.core.use-case :as uc]))


(defn list-games [{:keys [storage]}]
  (fn []
    (->> (game/all storage)
         (uc/result->action :game/list))))

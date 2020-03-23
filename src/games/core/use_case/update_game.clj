(ns games.core.use-case.update-game
  (:require [games.core.entity :as entity]
            [games.core.entity.game :as game]
            [games.core.auth :refer [can-modify?]]
            [games.core.action :as action]
            [games.core.use-case :as uc]))


(defn update-game [{:keys [storage]}]
  (fn [id caller game]
    (if (can-modify? id caller)
      (let [current (game/fetch storage id)]
        (if (entity/storage-error? current)
          (uc/result->action :game/update current)
          (->> {::entity/id id}
               ;; Assign ID to the map.
               (merge game)
               ;; Remove nils.
               (filter (comp some? val))
               (into {})
               ;; Merge new values into data from storage.
               (merge current)
               (game/save storage)
               (uc/result->action :game/update))))
      (action/make-error :game/delete :authorization))))

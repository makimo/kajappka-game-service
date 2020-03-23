(ns games.delivery.api.json
  (:require [games.core.entity :as entity]
            [games.core.entity.game :as game]
            [games.core.entity.owner :as owner]))


(defn owner->json
  "Converts an owner to JSON for humans (no namespaced keys)."
  [owner]
  {:id            (::owner/id owner)
   :email         (::owner/email owner)
   :nickname      (::owner/nickname owner)
   :profile_photo (::owner/nickname owner)})


(defn game->json
  "Converts a game to JSON for humans (no namespaced keys)."
  [game]
  {:id               (entity/uuid->string (::entity/id game))
   :title            (::game/title game)
   :description      (::game/description game)
   :manual_url       (::game/manual_url game)
   :video_manual_url (::game/video_manual_url game)
   :owner            (owner->json (::game/owner game))})

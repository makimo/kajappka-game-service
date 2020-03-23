(ns games.delivery.api.spec
  (:require [clojure.spec.alpha :as s]
            [games.core.entity :as entity]
            [games.core.entity.game :as game]
            [games.core.entity.spec]
            [games.core.entity.game.spec]))


(s/def ::id entity/uuid-string?)


(s/def ::create-request
  (s/keys :req-un [::game/title]
          :opt-un [::game/description
                   ::game/manual_url
                   ::game/video_manual_url]))


(s/def ::update-request
  (s/keys :opt-un [::game/title
                   ::game/description
                   ::game/manual_url
                   ::game/video_manual_url]))

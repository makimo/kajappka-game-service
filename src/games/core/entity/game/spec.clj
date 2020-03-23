(ns games.core.entity.game.spec
  (:require [games.core.entity.game :as game]
            [games.core.entity.spec :as es]
            [games.core.entity.owner.spec :as os]
            [clojure.spec.alpha :as s]
            [games.core.entity.owner :as owner]
            [games.core.entity :as entity]))


(s/def ::game/title string?)
(s/def ::game/description (s/or :s string? :n nil?))
(s/def ::game/manual_url (s/or :s string? :n nil?))
(s/def ::game/video_manual_url (s/or :s string? :n nil?))
(s/def ::game/owner ::os/owner)


(s/def ::game
  (s/merge ::es/entity
           (s/keys :req [::game/title
                         ::game/owner
                         ::game/description
                         ::game/manual_url
                         ::game/video_manual_url])))


(s/def ::storage #(satisfies? game/GameStorage %))


(s/fdef game/save
  :args (s/cat :storage ::storage :game ::game)
  :ret  ::es/storage-result)


(s/fdef all
  :args (s/cat :storage ::storage)
  :ret  ::es/storage-result)


(s/fdef delete
  :args (s/cat :store ::storage :id ::es/id)
  :ret  boolean?)


(s/fdef fetch
  :args (s/cat :storage ::storage :id ::es/id)
  :ret ::es/storage-result)


;; Unqualified owner map, needed by below.
(s/def ::owner (s/keys :req-un [::owner/id
                                ::owner/email
                                ::owner/nickname
                                ::owner/profile_photo]))


(s/def ::uuid-or-string (s/or :uuid ::entity/id :s string?))

(s/fdef game/make-game
  :args (s/cat :game (s/keys :req-un [::game/title ::owner]
                             :opt-un [::game/description
                                      ::game/manual_url
                                      ::game/video_manual_url]
                             :opt-un [::uuid-or-string]))
  :ret ::game)


(s/def ::qualified-update-map
  (s/keys :opt [::game/title
                ::game/description
                ::game/manual_url
                ::game/video_manual_url]))


(s/fdef game/qualify-update-map
  :args (s/cat :game (s/keys :opt-un [::game/title
                                      ::game/description
                                      ::game/manual_url
                                      ::game/video_manual_url]))
  :ret ::qualified-update-map)

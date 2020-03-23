(ns games.core.entity.owner.spec
  (:require [games.core.entity.owner :as owner]
            [clojure.spec.alpha :as s]))


(s/def ::owner/id string?)
(s/def ::owner/email string?)
(s/def ::owner/nickname string?)
(s/def ::owner/profile_photo string?)

(s/def ::owner
  (s/keys :req [::owner/id ::owner/email ::owner/nickname ::owner/profile_photo]))

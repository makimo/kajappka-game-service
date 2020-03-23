(ns games.core.entity.owner
  (:require [games.core.entity :as entity]))



(defn make-owner
  "Make new owner from unqualified map."
  [{id            :id
    email         :email
    nickname      :nickname
    profile_photo :profile_photo}]
  {::id            id
   ::email         email
   ::nickname      nickname
   ::profile_photo profile_photo})

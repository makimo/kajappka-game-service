(ns games.core.entity.game
  (:require [games.core.entity :as entity]
            [games.core.entity.owner :as owner]))


(defn make-game
  "Make new game."
  [{id               :id
    title            :title
    description      :description
    manual_url       :manual_url
    video_manual_url :video_manual_url
    owner :owner}]
  {::entity/id        (if (string? id)
                        (entity/string->uuid id)
                        (or id (entity/make-uuid)))
   ::title            title
   ::description      description
   ::manual_url       manual_url
   ::video_manual_url video_manual_url
   ::owner            (owner/make-owner owner)})


(defn qualify-update-map
  [{title            :title
    description      :description
    manual_url       :manual_url
    video_manual_url :video_manual_url}]
  (->> {::title            title
        ::description      description
        ::manual_url       manual_url
        ::video_manual_url video_manual_url}
       (remove (comp nil? second))
       (into {})))


(defn update-game
  "Update existing game."
  [game fields]
  (merge (make-game fields) game))


(defprotocol GameStorage
  (-fetch [this id] "Fetch a game")
  (-save [this game] "Save a game")
  (-all [this] "Return a seq of all games")
  (-delete [this id] "Removes a game from storage"))


(defn fetch
  "Fetch a game from storage."
  [storage id]
  (-fetch storage id))


(defn save
  "Save a game to storage. Inserts if new, otherwise updates
   an existing record."
  [storage game]
  (-save storage game))


(defn all
  "Return a seq of all games."
  [storage]
  (-all storage))


(defn delete
  "Removes a game from storage by ID."
  [storage id]
  (-delete storage id))

(ns games.storage.sqlite
  "A sqlite powered storage for games."
  (:require [clojure.java.jdbc :as j]
            [clojure.string :as string]
            [honeysql.core :as sql]
            [games.core.entity :as entity]
            [games.core.entity.game :as game]
            [games.core.entity.owner :as owner]))


(defn- row->owner
  [result]
  (if result
    {::owner/id            (:id result)
     ::owner/email         (:email result)
     ::owner/nickname      (:nickname result)
     ::owner/profile_photo (:profile_photo_url result)}
    :not-found))


(defn- owner->row
  [owner]
  {:id (::owner/id owner)
   :email (::owner/email owner)
   :nickname (::owner/nickname owner)
   :profile_photo (::owner/profile_photo owner)})


(defn- fetch-owner
  [db id]
  (->> {:select [:*]
        :from   [:owners]
        :where  [:= :id id]}
    sql/format
    (j/query db)
    first
    row->owner))


(defn- insert-owner
  [db owner]
  (let [current (fetch-owner db (::owner/id owner))
        new?    (= :not-found current)]
    (if new?
      (j/insert! db :owners (owner->row owner)))))


(defn- row->game
  [db result]
  (if result
    {::entity/id             (entity/string->uuid (:id result))
     ::game/title            (:title result)
     ::game/description      (:description result)
     ::game/manual_url       (:manual_url result)
     ::game/video_manual_url (:video_manual_url result)
     ::game/owner            (fetch-owner db (:owner_id result))}
    :not-found))


(defn- game->row
  [game]
  {:id               (::entity/id game)
   :title            (::game/title game)
   :description      (::game/description game)
   :manual_url       (::game/manual_url game)
   :video_manual_url (::game/video_manual_url game)
   :owner_id         (get-in game [::game/owner ::owner/id])})


(defn- update-game
  [db game]
  (let [id       (::entity/id game)
        data     (dissoc (game->row game) :id)]
    (try
      (j/update! db :games data ["id = ?" id]) game
      (catch Exception e :not-updated))))


(defn- fetch
  [db id]
  (->> {:select [:*]
        :from   [:games]
        :where  [:= :id id]}
    sql/format
    (j/query db)
    first
    (row->game db)))


(defn- insert-game
  [db game]
  (try
    (insert-owner db (::game/owner game))
    (j/insert! db :games (game->row game))
    (fetch db (::entity/id game))
    (catch Exception e :not-inserted)))


(defn- save
  [db game]
  (let [current (fetch db (::entity/id game))
        new?    (= :not-found current)]
    (if new?
      (insert-game db game)
      (update-game db (merge current game)))))


(defn- all
  [db]
  (->> {:select [:*]
        :from   [:games]}
    sql/format
    (j/query db)
    (map #(row->game db %))))


(defn- delete
  "Removes a game from the database by id."
  [db id]
  (try
    (-> (j/delete! db :games ["id = ?" id])
        first
        (= 1))
    (catch Exception e false)))


(defrecord SqliteStorage [db]
  game/GameStorage
  (-fetch [_ id] (fetch db id))
  (-save [_ game] (save db game))
  (-all [_] (all db))
  (-delete [_ id] (delete db id)))


(def table-spec
  [(j/create-table-ddl :owners
    [[:id :text "PRIMARY_KEY"]
     [:email :text]
     [:nickname :text]
     [:profile_photo :text]])
   (j/create-table-ddl :games
    [[:id :text "PRIMARY KEY"]
     [:title :text]
     [:description :text]
     [:manual_url :text]
     [:video_manual_url :text]
     [:owner_id :text]
     ["FOREIGN KEY(owner_id) REFERENCES owner(id)"]])])


(defn- db-do-commands
  "Helper that swaps argument order of jdbc/db-do-commands."
  [db commands]
  (j/db-do-commands db commands))


(defn- create-table-if-not-exists
  "Creates a games table if it does not exist."
  [db]
  (->> table-spec
    (map #(string/replace % "CREATE TABLE" "CREATE TABLE IF NOT EXISTS"))
    (db-do-commands db)))


(defn make-db-spec
  "Creates a sqlite friendly db spec for use with jdbc."
  [file]
  {:classname   "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname     file})


(defn make-storage
  "Given a db spec - create a new Jdbc backed storage for games."
  [db]
  (create-table-if-not-exists db)
  (->SqliteStorage db))

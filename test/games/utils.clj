(ns games.utils
  (:require [clj-json.core :as json]
            [clojure.spec.alpha :as s]
            [clojure.test.check.generators :as gen]
            [clojure.walk :refer [keywordize-keys]]
            [games.core.entity :as entity]
            [games.core.entity.game :as game]
            [games.core.entity.game.spec :as g]
            [games.delivery.api.core :refer [app]]
            [games.delivery.api.json :refer [game->json]]
            [ring.mock.request :as mock]))

;; Mock GameStorage, expects fully-qualified games.
(defrecord TestStorage [db]
  game/GameStorage
  (-fetch [_ id] (@db (.toString id)))
  (-save [_ game] (swap! db assoc (.toString (::entity/id game)) game) (@db (.toString (::entity/id game))))
  (-all [_] (vals @db))
  (-delete [_ id] (swap! db dissoc (.toString id))))


(defn get-owner
  [request]
  (:owner request))


(defn post-request
  [json owner]
  (-> (mock/request :post "/")
      (mock/json-body json)
      (assoc :owner owner)
      app))


(defn patch-request
  [id json owner]
  (-> (mock/request :patch (str "/" id))
      (mock/json-body (dissoc json :owner))
      (assoc :owner owner)
      app))


(defn delete-request
  [id owner]
  (-> (mock/request :delete (str "/" id))
      (assoc :owner owner)
      app))


(defn get-request
  []
  (-> (mock/request :get "/")
      (assoc :owner {})
      app))


(defn body->json
  [body]
  (-> body
      json/parse-string
      keywordize-keys
      :data))


(defn explain-body->game
  "Check if returned response body contains valid game."
  [body]
  (s/explain-data ::g/game (game/make-game (body->json body))))


(defn gen-game [] (s/gen ::g/game))


(defn gen-game-json
  []
  (gen/fmap #(game->json %) (gen-game)))


(defn save-to-db
  [game]
  (game/save games.delivery.storage/store game))


(defn save-from-json-to-db
  [json]
  (save-to-db (game/make-game json)))

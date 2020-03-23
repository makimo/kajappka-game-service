(ns games.delivery.api.utils)


(defn bad-request
  [error]
  {:status 400 :body {:error error}})


(defn clean-game-data
  [data]
  (select-keys data [:title :description :manual_url :video_manual_url]))

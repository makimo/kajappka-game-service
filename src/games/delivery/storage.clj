(ns games.delivery.storage
  (:require [mount.core :refer [defstate]]
            [games.storage.sqlite.spec :as s]
            [environ.core :refer [env]]
            [games.storage.sqlite :refer [make-storage make-db-spec]]))


(defstate db :start (make-db-spec (env :db-path)))

(defstate store :start (make-storage db))

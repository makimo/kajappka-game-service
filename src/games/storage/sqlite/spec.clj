(ns games.storage.sqlite.spec
  (:require [clojure.spec.alpha :as s]
            [clojure.java.jdbc.spec :as js]
            [games.core.entity.game.spec :as game]
            [games.storage.sqlite :as sqlite]))


(s/fdef sqlite/make-db-spec
  :args (s/cat :file string?)
  :ret  ::js/db-spec-driver-manager)


(s/fdef sqlite/make-storage
  :args (s/cat :db ::js/db-spec-driver-manager)
  :ret  ::game/storage)

(ns games.delivery.use-cases
  (:require [mount.core :refer [defstate]]
            [games.core.use-case.create-game :as cg]
            [games.core.use-case.list-games :as lg]
            [games.core.use-case.update-game :as ug]
            [games.core.use-case.delete-game :as dg]
            [games.delivery.storage :refer [store]]))


(defstate create-game :start (cg/create-game {:storage store}))

(defstate list-games :start (lg/list-games {:storage store}))

(defstate update-game :start (ug/update-game {:storage store}))

(defstate delete-game :start (dg/delete-game {:storage store}))

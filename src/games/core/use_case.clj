(ns games.core.use-case
  (:require [games.core.entity :as entity]
            [games.core.action :as action]))


(defn result->action
  "Creates an action for the result of creating a new game."
  [type result]
  (let [payload {:result result}]
    (if (entity/storage-error? result)
      (action/make-error type :storage payload)
      (action/make-action type payload))))

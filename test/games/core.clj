(ns games.core
  (:require [clojure.spec.test.alpha :as st]
            [clojure.test :refer :all]
            [games.core.entity.game :as game]))


(deftest test-make-game
  (let [output (-> `game/make-game st/check first)]
    (is
     (true? (-> output :clojure.spec.test.check/ret :result))
     (-> output :clojure.spec.test.check/ret :result-data str))))


(deftest test-qualify-update-map
  (let [output (-> `game/qualify-update-map st/check first)]
    (is
     (true? (-> output :clojure.spec.test.check/ret :result))
     (-> output :clojure.spec.test.check/ret :result-data str))))


;; TODO: Test the rest of the core module.

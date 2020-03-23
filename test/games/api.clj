(ns games.api
  (:require [clj-json.core :as json]
            [clojure.test :refer :all]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.properties :as prop]
            [games.core.entity :as entity]
            [games.core.entity.game :as game]
            [games.utils :refer :all]
            [mount.core :as mount]))

(use-fixtures :each
  (fn
    [f]
    (mount/stop)
    (mount/start-with
     {#'games.delivery.storage/store (->TestStorage (atom {}))
      #'games.delivery.api.middleware/owner-service get-owner})
    (f)))


(defspec test-valid-post-request 100
  (prop/for-all [json (gen-game-json)]
    (let [{:keys [status body]} (post-request json (:owner json))]
      (is (= status 201))
      ;; Check if returned JSON is a valid game.
      (is (= nil (explain-body->game body))))))


(deftest test-invalid-post-request
  (doseq [data [nil
                {}
                "foo"
                1
                {:title nil}
                {:title 1}
                {:title "foo" :description 1}]]
    (is (= 400 (:status (post-request data {}))))))


(deftest test-invalid-patch-request
  (let [game (game/make-game {:title "foo" :owner {:id "1"}})
        uuid (::entity/id game)]
    (game/save games.delivery.storage/store game)
    (is (= 403 (:status (patch-request uuid {:title "foo"} {:id "2"}))))))


(defspec test-valid-patch-request 100
  (prop/for-all [json (gen-game-json)]
    (save-from-json-to-db json)
    (let [update   {:title "foo"}
          owner    (:owner json)
          response (patch-request (:id json) update owner)
          game     (body->json (:body response))]
      (is (= 200 (:status response)))
      (is (= game (merge json update))))))


(defspec test-valid-delete-request 100
  (prop/for-all [json (gen-game-json)]
    (save-from-json-to-db json)
    (let [owner (:owner json)]
      (is (= 200 (:status (delete-request (:id json) owner)))))))


(deftest test-invalid-delete-request
  (let [json {:title "foo" :owner {:id "1"}}
        game (save-from-json-to-db json)]
    (is (= 403 (:status (delete-request (::entity/id game) {:id "2"}))))))


(deftest test-valid-get-request
  (do
    (dotimes [_ 2] (save-from-json-to-db (first (gen-game-json))))
    (let [response (get-request)]
      (is (= 200 (:status response)))
      (is (= 2 (-> response :body json/parse-string count)))))
  (do
    (dotimes [_ 3] (save-from-json-to-db (first (gen-game-json))))
    (let [response (get-request)]
      (is (= 200 (:status response)))
      (is (= 5 (-> response :body json/parse-string count))))))

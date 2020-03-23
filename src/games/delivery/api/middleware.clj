(ns games.delivery.api.middleware
  (:require
   [ring.util.response :refer [response status]]
   [clj-http.client :as client]
   [environ.core :refer [env]]
   [mount.core :refer [defstate]]
   [clj-json.core :as json]
   [clojure.walk :refer [keywordize-keys]]))


(defn- get-owner
  [request]
  (->
   (client/get (env :verifier-uri)
               {:headers {"Authorization" (get-in request [:headers "authorization"])}
                :throw-exceptions false})
   :body
   json/parse-string
   keywordize-keys))


(defstate owner-service :start get-owner)


(defn wrap-owner
  "Append owner object to the request based on
   the value of the Authorization header."
  [handler]
  (fn [request]
    (if-let [owner (owner-service request)]
      (handler (assoc request :owner owner))
      (-> (response "Access denied, invalid Authorization header")
          (status 403)))))

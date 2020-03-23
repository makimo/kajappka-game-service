(ns games.delivery.api.core
  (:require [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.defaults :refer [api-defaults wrap-defaults]]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.util.response :refer [response status]]
            [compojure.core :refer [defroutes GET POST DELETE PATCH]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [games.delivery.api.create :as create-game]
            [games.delivery.api.delete :as delete-game]
            [games.delivery.api.list :as list-games]
            [games.delivery.api.update :as update-game]
            [games.delivery.api.middleware :refer [wrap-owner]]))


(defroutes routes
  (GET    "/"    []               (list-games/dispatch))
  (POST   "/"    request          (create-game/dispatch request))
  (DELETE "/:id" request          (delete-game/dispatch request))
  (PATCH  "/:id" [id :as request] (update-game/dispatch id request))
  (route/not-found {:status 404 :body {:error "Not found"}}))


(def app
  (-> routes
      wrap-params
      (wrap-json-body {:keywords? true})
      wrap-json-response
      (wrap-defaults api-defaults)
      wrap-owner))

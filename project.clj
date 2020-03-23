(defproject kajappka-game-service "0.1.0-SNAPSHOT"
  :description "Game CRUD service"

  :plugins [[lein-environ "1.1.0"]]

  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/java.jdbc "0.7.0"]
                 [honeysql "0.9.0"]
                 [mount "0.1.11"]
                 [environ "0.5.0"]
                 [org.xerial/sqlite-jdbc "3.19.3"]
                 [org.clojure/test.check "1.0.0"]]

  :aliases {"api" ["with-profile" "api" "ring" "server-headless"]
            "build" ["with-profile" "api" "ring" "uberjar"]}

  :env {:verifier-uri "http://localhost:8000/success"
        :db-path ~(str (System/getProperty "user.home") "/.games")}

  :profiles
  ;; TODO: Figure out how to share dependencies. Cider
  ;; needs the same dependencies as :api.
  {:dev {:dependencies [[ring/ring-mock "0.4.0"]
                        [ring/ring-core "1.6.2"]
                        [ring/ring-jetty-adapter "1.6.2"]
                        [ring/ring-json "0.4.0"]
                        [ring/ring-defaults "0.3.1"]
                        [ring-cors "0.1.11"]
                        [compojure "1.6.0"]
                        [clj-http "3.10.0"]
                        [clj-json "0.5.3"]]}

   :api {:dependencies [[ring/ring-mock "0.4.0"]
                        [ring/ring-core "1.6.2"]
                        [ring/ring-jetty-adapter "1.6.2"]
                        [ring/ring-json "0.4.0"]
                        [ring/ring-defaults "0.3.1"]
                        [ring-cors "0.1.11"]
                        [compojure "1.6.0"]
                        [clj-http "3.10.0"]
                        [clj-json "0.5.3"]]

         :plugins [[lein-ring "0.12.5"]]
         :ring {:handler games.delivery.api.core/app
                :init    mount.core/start
                :port    9001}}})

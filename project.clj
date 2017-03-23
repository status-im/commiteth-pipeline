(defproject commiteth-pipeline "0.1.0-SNAPSHOT"
  :description "Continuous delivery pipeline for commiteth"
  :url "https://commiteth.com"
  :dependencies [[lambdacd "0.9.3"]
                 [ring-server "0.3.1"]
                 [org.clojure/clojure "1.7.0"]
                 [org.clojure/tools.logging "0.3.0"]
                 [org.slf4j/slf4j-api "1.7.5"]
                 [ch.qos.logback/logback-core "1.0.13"]
                 [ch.qos.logback/logback-classic "1.0.13"]
                 [environ "1.1.0"]
                 [lambdacd-git "0.2.0"]
                 [compojure "1.5.2"]
                 [mount "0.1.11"]
                 [lambdacd-artifacts "0.2.1"]
                 [clj-http "2.3.0"]
                 [org.clojure/data.json "0.2.6"]]
  :plugins [[lein-environ "1.1.0"]]
  :profiles {:uberjar {:aot :all}
             :dev {:env {:home-dir "./cd-pipeline-home"}}
             :prod {:env {:home-dir "/opt/commiteth/cd-pipeline-home"}}}
  :main commiteth-pipeline.core)

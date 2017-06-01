(ns commiteth-pipeline.core
  (:require
   [commiteth-pipeline.pipeline :as pipeline]
   [ring.server.standalone :as ring-server]
   [compojure.core :refer [routes]]
   [lambdacd.ui.ui-server :as ui]
   [lambdacd.runners :as runners]
   [lambdacd.util :as util]
   [lambdacd.core :as lambdacd]
   [lambdacd-git.core :as lambdacd-git]
   [environ.core :refer [env]]
   [clojure.java.io :as io]
   [mount.core :as mount]
   [clojure.tools.logging :as log])
  (:gen-class))


(def pipeline-atom (atom nil))

(defn trythy? [x]
  ())


(defn start []
  (let [home-dir (env :home-dir)
        develop-branch? (env :develop)
        config {:home-dir home-dir
                :name "commiteth pipeline"}
        pipeline-def (if develop-branch?
                       pipeline/pipeline-def-develop
                       pipeline/pipeline-def-master)
        pipeline (lambdacd/assemble-pipeline pipeline-def config)]
    (log/info "develop?" develop-branch?)
    (reset! pipeline-atom pipeline)
    (log/info "LambdaCD Home Directory is " home-dir)
    (runners/start-one-run-after-another pipeline)
    (ring-server/serve (routes
                        (ui/ui-for pipeline)
                        (lambdacd-git/notifications-for pipeline))
                       {:open-browser? false
                        :port (if develop-branch? 8082 8080)})))

(defn stop []
  (lambdacd/default-shutdown-sequence (:context @pipeline-atom)))

(mount/defstate app
  :start (start)
  :stop (stop))

(defn -main [& args]
  (lambdacd-git/init-ssh!)
  (mount/start))

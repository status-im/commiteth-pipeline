(ns commiteth-pipeline.steps
  (:require
   [lambdacd.steps.shell :as shell]
   [lambdacd-git.core :as git]
   [lambdacd-artifacts.core :as artifacts]
   [clojure.tools.logging :as log]))


(def repo-uri "https://github.com/status-im/commiteth.git")
(def repo-branch "master")

(defn wait-for-git [args ctx]
  (git/wait-for-git ctx repo-uri
                     :ref "refs/heads/master"
                     :ms-between-polls (* 30 1000)))

(defn git-clone [args ctx]
  (shell/bash ctx (:cwd args) "pwd")
  (git/clone ctx repo-uri repo-branch (:cwd args)))

(defn build-uberjar [args ctx]
  (shell/bash ctx (:cwd args) "lein less once && lein uberjar"))

(defn run-tests [args ctx]
  (shell/bash ctx (:cwd args) "echo no test running for now"))

(defn deploy-uberjar [args ctx]
  (shell/bash ctx
              (:cwd args)
              "sudo service commiteth stop && cp target/uberjar/commiteth.jar /opt/commiteth/commiteth.jar && sudo service commiteth start"))

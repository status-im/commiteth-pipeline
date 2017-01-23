(ns commiteth-pipeline.steps
  (:require
   [lambdacd.steps.shell :as shell]
   [lambdacd-git.core :as git]
   [lambdacd-git.artifacts :as artifacts]
   [clojure.tools.logging :as log]))


(def repo-uri "https://github.com/status-im/commiteth.git")
(def repo-branch "master")

(defn git-clone [args ctx]
  (shell/bash ctx (:cwd args) "pwd")
  (git/clone ctx repo-uri repo-branch (:cwd args)))

(defn build-uberjar [args ctx]
  (shell/bash ctx (:cwd args) "lein uberjar"))

(defn run-tests [args ctx]
  (shell/bash ctx "/" "echo no test running for now"))

(defn deploy-uberjar [args ctx]
  (shell/bash ctx "/" "sudo service commiteth stop && cp target/commieth.jar /opt/commiteth && service commiteth start"))

(ns commiteth-pipeline.steps
  (:require
   [lambdacd.steps.shell :as shell]
   [lambdacd-git.core :as git]
   [lambdacd-artifacts.core :as artifacts]
   [clojure.tools.logging :as log]))


(def repo-uri "https://github.com/status-im/commiteth.git")
(def repo-branch "master")

(defn git-clone [args ctx]
  (shell/bash ctx (:cwd args) "pwd")
  (git/clone ctx repo-uri repo-branch (:cwd args)))

(defn build-uberjar [args ctx]
  (shell/bash ctx (:cwd args) "lein uberjar"))

(defn run-tests [args ctx]
  (shell/bash ctx (:cwd args) "echo no test running for now"))

(defn deploy-uberjar [args ctx]
  (shell/bash ctx
              (:cwd args)
              "pwd; ls -l;sudo service commiteth stop && cp target/commiteth.jar /opt/commiteth/commiteth.jar && sudo service commiteth start"))

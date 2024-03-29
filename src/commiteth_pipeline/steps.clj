(ns commiteth-pipeline.steps
  (:require
   [lambdacd.steps.shell :as shell]
   [lambdacd-git.core :as git]
   [lambdacd-artifacts.core :as artifacts]
   [clojure.tools.logging :as log]
   [commiteth-pipeline.slack :as slack]))


(def repo-uri "https://github.com/status-im/open-bounty.git")

(defn wait-for-git-master [args ctx]
  (git/wait-for-git ctx repo-uri
                     :ref "refs/heads/master"
                     :ms-between-polls (* 30 1000)))

(defn wait-for-git-develop [args ctx]
  (git/wait-for-git ctx repo-uri
                     :ref "refs/heads/develop"
                     :ms-between-polls (* 30 1000)))

(defn git-clone-master [args ctx]
  (shell/bash ctx (:cwd args) "pwd")
  (git/clone ctx repo-uri "master" (:cwd args)))

(defn git-clone-develop [args ctx]
  (shell/bash ctx (:cwd args) "pwd")
  (git/clone ctx repo-uri "develop" (:cwd args)))

(defn build-uberjar [args ctx]
  (shell/bash ctx (:cwd args) {"LEIN_SNAPSHOTS_IN_RELEASE" "1"}  "lein less once && lein uberjar"))

(defn run-tests [args ctx]
  (shell/bash ctx (:cwd args) "echo no test running for now"))

(defn deploy-uberjar-master [args ctx]
  (shell/bash ctx
              (:cwd args)
              "sudo service commiteth stop && cp target/uberjar/commiteth.jar /opt/commiteth/commiteth.jar && sudo service commiteth start"))

(defn deploy-uberjar-develop [args ctx]
  (shell/bash ctx
              (:cwd args)
              "sudo service commiteth-test stop && cp target/uberjar/commiteth.jar /opt/commiteth-test/commiteth.jar && sudo service commiteth-test start"))

(defn slack-notify-master [args ctx]
  (let [rev (:revision args)
        sha  (if rev (subs rev 0 7) "")
        msg (format "Deployed revision <https://github.com/status-im/open-bounty/commit/%s|%s> (master branch) to https://openbounty.status.im" sha sha)]
    (println "Sending slack notification" msg)
    (slack/slack-notify msg)
    {:status :success}))

(defn slack-notify-develop [args ctx]
  (let [rev (:revision args)
        sha  (if rev (subs rev 0 7) "")
        msg (format "Deployed revision <https://github.com/status-im/open-bounty/commit/%s|%s> (develop branch) to https://openbounty.status.im:444" sha sha)]
    (println "Sending slack notification" msg)
    (slack/slack-notify msg)
    {:status :success}))


(defn slack-notify-failure-master [args ctx]
  (let [rev (:revision args)
        sha  (if rev (subs rev 0 7) "")
        msg (format "Failed to build revision <https://github.com/status-im/open-bounty/commit/%s|%s> (master branch)" sha sha)]
    (println "Sending slack notification" msg)
    (slack/slack-notify msg)
    {:status :success}))

(defn slack-notify-failure-develop [args ctx]
  (let [rev (:revision args)
        sha  (if rev (subs rev 0 7) "")
        msg (format "Failed to build revision <https://github.com/status-im/open-bounty/commit/%s|%s> (develop branch)" sha sha)]
    (println "Sending slack notification" msg)
    (slack/slack-notify msg)
    {:status :success}))

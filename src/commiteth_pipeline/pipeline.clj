(ns commiteth-pipeline.pipeline
  (:refer-clojure :exclude [alias])
  (:require
   [commiteth-pipeline.steps :refer :all]
   [lambdacd.steps.control-flow :refer :all]
   [lambdacd.steps.manualtrigger :as manualtrigger]
   [lambdacd-git.core :as git]))


(def ^:const pipeline-def-master
  `((either wait-for-git-master
            manualtrigger/wait-for-manual-trigger)
    (with-workspace
      git-clone-master
      git/list-changes
      build-uberjar
      run-tests
      deploy-uberjar-master
      slack-notify-master)))

(def ^:const pipeline-def-develop
  `((either wait-for-git-develop
            manualtrigger/wait-for-manual-trigger)
    (with-workspace
      git-clone-develop
      git/list-changes
      build-uberjar
      run-tests
      deploy-uberjar-develop
      slack-notify-develop)))

(ns commiteth-pipeline.pipeline
  (:refer-clojure :exclude [alias])
  (:require
   [commiteth-pipeline.steps :refer :all]
   [lambdacd.steps.control-flow :refer :all]
   [lambdacd.steps.manualtrigger :as manualtrigger]
   [lambdacd-git.core :as git]))


(def ^:const pipeline-def
  `((either wait-for-git
            manualtrigger/wait-for-manual-trigger)
    (with-workspace
      git-clone
      git/list-changes
      build-uberjar
      run-tests
      deploy-uberjar
      slack-notify)))

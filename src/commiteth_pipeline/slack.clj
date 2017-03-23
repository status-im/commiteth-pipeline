(ns commiteth-pipeline.slack
  (:require
   [environ.core :refer [env]]
   [clj-http.client :as client]
   [clojure.data.json :as json]))

;; based on https://gist.github.com/mikebroberts/9604828
(defn slack-notify [text]
  (let [webhook-url (env :slack-webhook-url)]
    (client/post webhook-url {:form-params
                              {:payload (json/write-str
                                         {:username "commiteth-cd-pipeline"
                                          :text text})}})))

(ns sampleapi.handler.login
  (:require [buddy.sign.jwt :as jwt]
            [ataraxy.response :as response]
            [integrant.core :as ig]))

(defn find-user [_db username password]
  (when (and (= username "foo")
             (= password "bar"))
    {:id 998}))

(defmethod ig/init-key :sampleapi.handler/login [_ {:keys [db secret]}]
  (do (println "HERE")
      (fn [{[_kw username password] :ataraxy/result}]
        (do (println username password)
            (if-let [user (find-user db username password)]
              [::response/ok {:token (jwt/sign {:user (:id user)} secret)}]
              [::response/bad-request "Failed!"])))))
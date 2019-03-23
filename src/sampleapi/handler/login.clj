(ns sampleapi.handler.login
  (:require [buddy.sign.jwt :as jwt]
            [ataraxy.response :as response]
            [integrant.core :as ig]))

(defn find-user [_db body]
  (when (and (= (:username body) "foo")
             (= (:password body) "bar"))
    {:id 998}))

(defmethod ig/init-key :sampleapi.handler/login [_ {:keys [db secret]}]
  (do (println "HERE")
      (fn [{[_kw body] :ataraxy/result}]
        (do (println body)
            (if-let [user (find-user db body)]
              [::response/ok {:token (jwt/sign {:user (:id user)} secret)}]
              [::response/bad-request "Failed!"])))))
(ns sampleapi.handler.user
  (:require [buddy.sign.jwt :as jwt]
            [ataraxy.response :as response]
            [integrant.core :as ig]
            [sampleapi.boundary.user :as user]))

(defmethod ig/init-key ::create [_ {:keys [db]}]
  (fn [{[_ email username password] :ataraxy/result}]
    (let [id (user/create-user db email username password)]
      [::response/created (str "/users/" id)])))

(defmethod ig/init-key ::login [_ {:keys [db secret]}]
  (fn [{[_kw username password] :ataraxy/result}]
    (if-let [user (user/find-user db username password)]
      [::response/ok {:token (jwt/sign {:user-id (:id user)} secret)}]
      [::response/bad-request "Failed!"])))

(defmethod ig/init-key ::whoami
  [_ opts]
  (fn [{id :identity}]
    [::response/ok {:message (str "your user id: " (:user-id id))}]))

(ns sampleapi.handler.whoami
  (:require [ataraxy.core :as ataraxy]
            [ataraxy.response :as response]
            [integrant.core :as ig]))

(defmethod ig/init-key :sampleapi.handler/whoami
  [_ opts]
  (fn [{id :identity :as request}]
    (do (prn request)
        [::response/ok {:message (str "your user id: " id)}])))

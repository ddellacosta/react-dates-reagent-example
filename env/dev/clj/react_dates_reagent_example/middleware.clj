(ns react-dates-reagent-example.middleware
  (:require
    [prone.middleware :refer [wrap-exceptions]]
    [ring.middleware.cljsjs :refer [wrap-cljsjs]]
    [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
    [ring.middleware.reload :refer [wrap-reload]]))

(defn wrap-middleware [handler]
  (-> handler
      wrap-cljsjs
      (wrap-defaults site-defaults)
      wrap-exceptions
      wrap-reload))

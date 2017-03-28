(ns react-dates-reagent-example.prod
  (:require [react-dates-reagent-example.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)

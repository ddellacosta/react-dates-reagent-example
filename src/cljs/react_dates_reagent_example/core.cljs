(ns react-dates-reagent-example.core
  (:require
    cljsjs.moment
    cljsjs.react-dates
    [reagent.core :as reagent :refer [atom]]
    [reagent.session :as session]
    [secretary.core :as secretary :include-macros true]
    [accountant.core :as accountant]))

;; -------------------------
;; Views

(def DateRangePicker
  (reagent/adapt-react-class js/ReactDates.DateRangePicker))

(defn merge-in-dates
  [selected-dates current-dates]
  (->> (js->clj selected-dates)
       ;; convert Moment.js dates to vanilla JS Dates
       (reduce (fn [h [k v]] (if v (assoc h k (.toDate v)) h)) {})
       (merge current-dates)))

(defn inst->moment [inst]
  (some-> inst (js/moment)))

(defn home-page []
  (let [date-picker (atom {})]
    (fn []
      (let [start-date (get-in @date-picker [:dates "startDate"])
            end-date   (get-in @date-picker [:dates "endDate"])]
        [:div [:h2 "react-dates Reagent Example"]
         [:p "Check out "
          [:a {:href "https://github.com/airbnb/react-dates"} "the offical docs"]
          " for detailed documentation."]
         [:p "Selected start date: " (pr-str start-date)]
         [:p "Selected end date: " (pr-str end-date)]
         [:div.date-picker.wrapper
          [DateRangePicker
           {:start-date (inst->moment start-date)
            :end-date (inst->moment end-date)
            :on-focus-change #(swap! date-picker assoc :focused-input %)
            :on-dates-change #(swap! date-picker update :dates (partial merge-in-dates %))
            :focused-input (:focused-input @date-picker)}]]]))))

(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!
    {:nav-handler
     (fn [path]
       (secretary/dispatch! path))
     :path-exists?
     (fn [path]
       (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (mount-root))

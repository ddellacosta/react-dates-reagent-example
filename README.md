
# react-dates Reagent Example

Very basic example code to get you up and running with `react-dates` in a Reagent context.

This example uses the basic reagent template generated via `lein new reagent`, and includes a few other dependencies:

```clojure
[ring-cljsjs "0.1.0"] ; to load in CSS assets
[cljsjs/react-dates "8.2.1-2"]
```

Relevant links:
* [Official Repository](https://github.com/airbnb/react-dates)
* [cljsjs Sub-repository](https://github.com/cljsjs/packages/tree/master/react-dates)
* [ring-cljsjs](https://github.com/deraen/ring-cljsjs)

The important bits can be found in `src/cljs/react_dates_reagent_example/core.cljs`:

```clojure
(ns react-dates-reagent-example.core
  (:require
    cljsjs.react-dates
    [reagent.core :as reagent :refer [atom]]
    ;; ...
    ))

;; This reagent helper lets us use the widget in the hiccup form as
;; below (`[DateRangePicker ...]`)
(def DateRangePicker
  (reagent/adapt-react-class js/ReactDates.DateRangePicker))

(defn merge-in-dates
  [selected-dates current-dates]
  (->> (js->clj selected-dates)
       ;; Convert Moment.js (used by default in react-dates) dates to
       ;; vanilla JS Dates
       (reduce (fn [h [k v]] (if v (assoc h k (.toDate v)) h)) {})
       (merge current-dates)))

(defn home-page []
  (let [date-picker (atom {})]
    (fn []
      [:div [:h2 "react-dates Reagent Example"]
       [:p "Check out "
        [:a {:href "https://github.com/airbnb/react-dates"} "the offical docs"]
        " for detailed documentation."]
       [:p "Selected start date: " (pr-str (get-in @date-picker [:dates "startDate"]))]
       [:p "Selected end date: " (pr-str (get-in @date-picker [:dates "endDate"]))]
       [:div.date-picker.wrapper
        [DateRangePicker
         ;;
         ;; These two functions are required. `on-focus-change`
         ;; allows you to set the focused-input, which, when passed as
         ;; a prop value into the widget itself triggers opening of
         ;; the calendar date pickers.
         ;;
         ;; `on-dates-change` is called when a date is actually
         ;; selected, and you should use this to set your actual date
         ;; values, as is done in a primitive way in this example.
         ;; 
         {:on-focus-change #(swap! date-picker assoc :focused-input %)
          :on-dates-change #(swap! date-picker update :dates (partial merge-in-dates %))
          :focused-input (:focused-input @date-picker)}]]])))
```

Screenshot:

![screenshot](https://raw.githubusercontent.com/ddellacosta/react-dates-reagent-example/master/reagent-example-screenshot.png)

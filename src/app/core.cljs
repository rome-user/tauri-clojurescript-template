(ns app.core
  (:require ["@tauri-apps/api/tauri" :as tauri]
            [reagent.core :as r]
            [reagent.dom :as rdom]))

(defn root-view []
  (let [name    (r/atom "")
        message (r/atom "")
        greet!  (fn [name]
                  ;; Learn more about Tauri commands at
                  ;; https://tauri.app/v1/guides/features/command
                  (-> (.invoke tauri "greet" #js {:name name})
                      (.then #(reset! message %))))]
    (fn []
      [:div.container
       [:h1 "Welcome to Tauri!"]
       [:div.row
        [:a {:href "https://tauri.app" :target "_blank"}
         [:img {:src "/tauri.svg" :class "logo tauri" :alt "Tauri logo"}]]
        [:a {:href "https://clojurescript.org" :target "_blank"}
         [:img {:src "/cljs.svg" :class "logo tauri" :alt "ClojureScript logo"}]]]
       [:p "Click on the Tauri, ClojureScript logos to learn more."]
       [:div.row
        [:input {:type "text"
                 :id "greet-input"
                 :on-change #(reset! name (.. % -target -value))
                 :placeholder "Enter a name..."}]
        [:button {:type "button" :on-click #(greet! @name)} "Greet"]]
       [:p @message]])))

(defn ^:dev/after-load mount-ui []
  (rdom/render [root-view] (js/document.getElementById "root")))

(defn ^:export main [& args]
  (mount-ui))

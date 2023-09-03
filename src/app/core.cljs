(ns app.core
  (:require [app.lib :refer [defnc]]
            [helix.core :refer [$]]
            [helix.dom :as d]
            [helix.hooks :as hooks]
            ["react-dom/client" :as rdc]
            ["@tauri-apps/api/tauri" :as tauri]))

(defnc root-view []
  (let [[name set-name] (hooks/use-state "")
        [mesg set-mesg] (hooks/use-state "")
        handle-change   (hooks/use-callback :auto-deps
                          (fn [event]
                            (set-name (.. event -target -value))))
        handle-click    (hooks/use-callback :auto-deps
                          ;; Learn more about Tauri commands at
                          ;; https://tauri.app/v1/guides/features/command
                          (fn []
                            (-> (.invoke tauri "greet" #js {:name name})
                                (.then #(set-mesg %)))))]
    (d/div {:class "container"}
      (d/h1 "Welcome to Tauri!")
      (d/div {:class "row"}
        (d/a {:href "https://tauri.app" :target "_blank"}
          (d/img {:src "/tauri.svg" :class "logo tauri" :alt "Tauri logo"}))
        (d/a {:href "https://clojurescript.org" :target "_blank"}
          (d/img {:src "/cljs.svg" :class "logo tauri" :alt "ClojureScript logo"})))
      (d/p "Click on the Tauri, ClojureScript logos to learn more.")
      (d/div {:class "row"}
        (d/input
         {:type "text"
          :id "greet-input"
          :on-change handle-change
          :placeholder "Enter a name..."})
        (d/button {:type "button" :on-click handle-click} "Greet"))
      (d/p mesg))))

(defonce root (rdc/createRoot (js/document.getElementById "root")))

(defn ^:dev/after-load mount-ui []
  (.render root ($ root-view)))

(defn ^:export main []
  (mount-ui))

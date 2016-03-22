(ns tictactoe.core
  (:require [reagent.core :as reagent :refer [atom]]
            [cljs.test :refer-macros [deftest is]]))

(enable-console-print!)

(declare app-state)

(defn new-board
  "Create a new board with the passed size
   n=1 => [[0]]
   n=2 => [[0, 0], [0, 0]]"
  ([n]
   (new-board n :b))
  ([n default]
   (vec (repeat n (vec (repeat n default))))))

(defn full? [board]
  false)


(defn- possible-moves []
  [[0 0]])

(defn- computer-next-move []
  (rand-nth (possible-moves)))


; Event handlers
(defn start-new-game! []
  (swap! app-state assoc :board (new-board 3)))

(defn make-move! [y x player]
  (swap! app-state assoc-in [:board y x] player))

(defn handle-human-move! [y x]
  (make-move! y x :h)
  (let [[y x] (computer-next-move)]
    (make-move! y x :c)))


;; Drawing logic
(defn blank
  ([i j]
   (blank i j "#eee"))
  ([i j color]
   [:rect
    {:x (+ 0.05 i)
     :y (+ 0.05 j)
     :width 0.9 :height 0.9
     :fill color
     :on-click #(handle-human-move! i j)}]))

(defn circle [i j]
  [:circle
   {:cx (+ 0.5 i)
    :cy (+ 0.5 j)
    :fill "white"
    :stroke "green"
    :stroke-width 0.05
    :r 0.45 }])

(defn cross [i j]
  [:g
   [blank i j "none"]
   [:g
    {:stroke "black"
      :stroke-width 0.1
      :fill "none" }
    [:line {:x1  (+ 0.1 i) :y1 (+ 0.1 j) :x2  (+ 0.9 i) :y2 (+ 0.9 j)}]
    [:line {:x1  (+ 0.9 i) :y1 (+ 0.1 j) :x2  (+ 0.1 i) :y2 (+ 0.9 j)}]]])


(defonce app-state
  (atom {:text "Welcome to TicTacToe"
         :board (new-board 3)}))

; Game logic
(defn tictactoe []
  [:center
   [:h1 (:text @app-state)]
   [:p
    [:button {:on-click start-new-game!} "New Game"]]
   (into
     [:svg {:view-box "0 0 3 3" :width 500 :heigh 500}]
     (for [i (range (count (:board @app-state)))
           j (range (count (:board @app-state)))]
       (case (get-in @app-state [:board i j])
         :b [blank i j]
         :h [circle i j]
         :c [cross i j])
       ))])

(reagent/render-component
  [tictactoe]
  (. js/document (getElementById "app")))

; (defn on-js-reload []
;   (swap! app-state assoc-in [:text] "TicTacToe"))

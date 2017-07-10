(ns sequences
  (:require [clojure.string :refer [join]]))

;; Creating

(range 10)

(range 10 20)

(range 1 25 2)

(range 1/2 4 1)

(repeat 5 1)

(take 10 (iterate inc 1))

(def whole-numbers (iterate inc 1))

(take 10 whole-numbers)

(take 10 (cycle (range 3)))

(interleave whole-numbers ["A" "B" "C" "D" "E"])

(interpose "," ["apples" "bananas" "grapes"])

(apply str (interpose "," ["apples" "bananas" "grapes"]))

(join \, ["apples" "bananas" "grapes"])

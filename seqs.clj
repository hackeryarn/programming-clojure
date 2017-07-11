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

(take 10 (filter even? whole-numbers))

(def vowel? #{\a\e\i\o\u})
(def consonant? (complement vowel?))

(take-while consonant? "the-quick-brown-fox")

(drop-while consonant? "the-quick-brown-fox")

(split-at 5 (range 10))

(split-with #(<= % 10) (range 0 20 2))

(every? odd? [1 3 5])

(every? odd? [1 3 5 8])

(some even? [1 3 5])

(some even? [1 2 5])

(map #(format "<p>%s</p>" %) ["the" "quick" "brown" "fox"])

(map #(format "<%s>%s</%s>" %1 %2 %1)
     ["h1" "h2" "h3" "h1"] ["the" "quick" "brown" "fox"])

(reduce + (range 1 11))

(reduce * (range 1 11))

(sort [42 1 7 11])

(sort-by #(.toString %) [42 1 7 11])

(for [word ["the" "quick" "brown" "fox"]]
  (format "<p>%s</p>" word))

(for [file "ABCDEFGH"
      rank (range 1 9)]
  (format "%c%d" file rank))

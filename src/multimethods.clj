(ns multimethods
  (:require [clojure.string :as str]))

(defn my-print-vector [ob]
  (.write *out* "[")
  (.write *out* (str/join " " ob))
  (.write *out* "]"))

(defn my-print [ob]
  (cond
    (nil? ob) (.write *out* "nil")
    (vector? ob) (my-print-vector ob)
    (string? ob) (.write *out* ob)))

(defn my-println [ob]
  (my-print ob)
  (.write *out* "\n"))



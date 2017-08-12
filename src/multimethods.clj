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

(defmulti my-print class)

(defmethod my-print String [s]
  (.write *out* s))

(my-println "stu")

(defmethod my-print nil [s]
  (.write *out* "nil"))

(defmethod my-print Number [n]
  (.write *out* (.toString n)))

(my-println 42)

(defmethod my-print :default [s]
  (.write *out* "#<")
  (.write *out* (.toString s))
  (.write *out* ">"))

(my-println (java.sql.Date. 0))

(defmethod my-print java.util.Collection [c]
  (.write *out* "(")
  (.write *out* (str/join " " c))
  (.write *out* ")"))

(my-println (take 6 (cycle [1 2 3])))

(defmethod my-print clojure.lang.IPersistentVector [c]
  (.write *out* "[")
  (.write *out* (str/join " " c))
  (.write *out* "]"))

(prefer-method
 my-print clojure.lang.IPersistentVector java.util.Collection)

(my-println [1 2 3])

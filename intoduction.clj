(ns introduction
  (:require [clojure.string :as str]))

(defn blank? [str]
  (every? #(Character/isWhitespace %) str))

(defrecord Person [first-name last-name])

(def foo (->Person "Aaron" "Bedra"))

(:first-name foo)

(def visitors (atom #{}))

(defn hello
  "Writes hello message to *out*. Calls you by username.
  Knows ifyou have been here before."
  [username]
  (swap! visitors conj username)
  (str "Hello, ", username))

(defrecord Book [title author])

(->Book "title" "author")

(defn greeting
  "Returns a greeting of the form 'Hello, username.'
  Default username is 'world'."
  ([] (greeting "world"))
  ([username] (str "Hello, " username)))

(defn date [person-1 person-2 & chaperones]
  (println person-1 "and" person-2
           "went out with" (count chaperones) "chaperones."))

(filter #(> (count %) 2) (str/split "A fine day it is" #"\W+"))

(ns introduction)

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

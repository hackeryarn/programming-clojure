(ns spec
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [clojure.spec.test.alpha :as stest]))

(s/def ::ingredient (s/keys :req [::name ::quantity ::unit]))
(s/def ::name string?)
(s/def ::quantity number?)
(s/def ::unit keyword?)

(s/fdef scale-ingredient
  :args (s/cat :ingredient ::ingredient :factor number?)
  :ret ::ingredient)

(defn scale-ingredient [ingredient factor]
  (update ingredient :quantity * factor))

(s/def ::marble-color #{:red :green :blue})
(s/valid? ::marble-color :red)
(s/valid? ::marble-color :pink)

(s/def ::bowling-roll (s/int-in 0 11))
(s/valid? ::bowling-roll 10)

(s/def ::company-name-2 (s/nilable string?))

(s/valid? ::company-name-2 nil)

(s/def ::odd-int (s/and int? odd?))
(s/valid? ::odd-int 5)

(s/valid? ::odd-int 10)

(s/valid? ::odd-int 5.2)

(s/def ::odd-or-42 (s/or :odd ::odd-int :42 #{42}))

(s/conform ::odd-or-42 42)

(s/conform ::odd-or-42 19)

(s/explain ::odd-or-42 0)



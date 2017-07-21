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

(s/def ::names (s/coll-of string?))

(s/valid? ::names ["Alex" "Stu"])

(s/valid? ::names #{"Alex" "Stu"})

(s/valid? ::names '("Alex" "Stu"))

(s/def ::my-set (s/coll-of int? :kind set? :min-count 2))
(s/valid? ::my-set #{10 20})

(s/def ::scores (s/map-of string? int?))
(s/valid? ::scores {"Stu" 100, "Alex" 200})

(s/def ::point (s/tuple rational? rational?))

(s/conform ::point [1.3 2.7])

(s/def ::music-id uuid?)
(s/def ::music-artist string?)
(s/def ::music-title string?)
(s/def ::music-date inst?)

(s/def ::music-release
  (s/keys :req [::music-id]
          :opt [::music-artist
                ::musict-title
                ::music-date]))


;; unqualified are used for keys of any namespace
(s/def ::music-release-unqualified
  (s/keys :req-un [::music-id]
          :opt-un [::music-artist
                   ::music-title
                   ::music-date]))


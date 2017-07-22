(ns spec
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [clojure.spec.test.alpha :as stest]
            [clojure.string :as str]))

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

(s/def ::cat-example (s/cat :s string? :i int?))

(s/valid? ::cat-example ["abc" 100])

(s/def ::alt-example (s/alt :i int? :k keyword?))

(s/valid? ::alt-example [100])

(s/valid? ::alt-example [:foo])

(s/conform ::alt-example [:foo])

(s/def ::oe (s/cat :odds (s/+ odd?) :even (s/? even?)))

(s/conform ::oe [1 3 5 100])

(s/def ::odds (s/+ odd?))

(s/def ::optional-even (s/? even?))

(s/def ::oe2 (s/cat :odds ::odds :even ::optional-even))

(s/conform ::oe2 [1 3 5 10])

(s/def ::intersection-args
  (s/cat :s1 set?
         :sets (s/* set?)))

(s/conform ::intersection-args '[#{1 2} #{2 3} #{2 5}])

(s/def ::intersection-args-2 (s/+ set?))

(s/def ::meta map?)

(s/def ::validator ifn?)

(s/def ::atom-args
  (s/cat :x any? :options (s/keys* :opt-un [::meta ::validator])))

(s/conform ::atom-args [100 :meta {:foo 1} :validator int?])

(s/def ::repeat-args
  (s/cat :n (s/? int?) :x any?))

(s/conform ::repeat-args [100 "foo"])

(s/conform ::repeat-args ["foo"])

(s/def ::rand-args (s/cat :n (s/? number?)))

(s/def ::rand-ret double?)

(s/def ::rand-fn
  (fn [{:keys [args ret]}]
    (let [n (or (:n args) 1)]
      (cond (zero? n) (zero? ret)
            (pos? n) (and (>= ret 0) (< ret n))
            (neg? n) (and (<= ret 0) (> ret n))))))

(s/fdef clojure.core/rand
        :args ::rand-args
        :ret ::rand-ret
        :fn ::rand-fn)

(defn opoosite [pred]
  (comp not pred))

(s/def ::pred
  (s/fspec :args (s/cat :x any?)
           :ret boolean?))

(s/fdef opoosite
        :args (s/cat :pred ::pred)
        :ret ::pred)

(stest/instrument 'clojure.core/rand)

(s/fdef clojure.core/symbol
        :args (s/cat :ns (s/? string?) :name string?)
        :ret symbol?
        :fn (fn [{:keys [args ret]}]
              (and (= (name ret) (:name args))
                   (= (namespace ret) (:ns args)))))

(stest/check 'clojure.core/symbol)

(s/exercise (s/cat :ns (s/? string?) :name string?))

(defn big? [x] (> x 100))

(s/def ::big-odd-int (s/and int? odd? big?))

(s/exercise ::big-odd-int)

(s/def ::sku
  (s/with-gen (s/and string? #(str/starts-with? % "SKU-"))
    (fn [] (gen/fmap #(str "SKU-" %) (s/gen string?)))))

(s/exercise ::sku)

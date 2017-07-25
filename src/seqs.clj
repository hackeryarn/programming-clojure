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

(def vowel? #{\a \e \i \o \u})
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

(def x (for [i (range 1 3)] (do (println i) i)))

(doall x)

(first (.getBytes "hello"))

(first (System/getProperties))

(reverse "hello")

(apply str (reverse "hello"))

;; bad
(let [m (re-matcher #"\w+" "the quick brown fox")]
  (loop [match (re-find m)]
    (when match
      (println match)
      (recur (re-find m)))))

(sort (re-seq #"\w+" "the quick brown fox"))

(drop 2 (re-seq #"\w+" "the quick brown fox"))

(map clojure.string/upper-case (re-seq #"\w+" "the quick brown fox"))

(import 'java.io.File)

(seq (.listFiles (File. ".")))

(map #(.getName %) (seq (.listFiles (File. "."))))

(map #(.getName %) (.listFiles (File. ".")))

(defn minutes-to-millis [mins] (* mins 1000 60))

(defn recently-modified? [file]
  (> (.lastModified file)
     (- (System/currentTimeMillis) (minutes-to-millis 30))))

(filter recently-modified? (file-seq (File. ".")))

(require '[clojure.java.io :refer [reader]])

(take 2 (line-seq (reader "primes.clj")))

(with-open [rdr (reader "primes.clj")]
  (count (line-seq rdr)))

(with-open [rdr (reader "primes.clj")]
  (count (filter #(re-find #"\S" %) (line-seq rdr))))

(use '[clojure.java.io :only (reader)])
(use '[clojure.string :only (blank?)])
(defn non-blank? [line] (not (blank? line)))

(defn non-git? [file] (not (.contains (.toString file) ".git")))

(defn clojure-source? [file] (.endsWith (.toString file) ".clj"))

(defn clojure-loc [base-file]
  (reduce +
          (for [file (file-seq base-file)
                :when (and (clojure-source? file) (non-git? file))]
            (with-open [rdr (reader file)]
              (count (filter non-blank? (line-seq rdr)))))))

(clojure-loc (java.io.File. "."))

(peek '(1 2 3))

(pop '(1 2 3))

(peek [1 2 3])

(pop [1 2 3])

([:a :b :c] 1)

(assoc [0 1 2 3 4] 2 :two)

(take 2 (drop 1 [1 2 3 4 5]))

;; much faster
(subvec [1 2 3 4 5] 3)

(keys {:sundace "spaniel" :darwin "beagle"})

(vals {:sundace "spaniel" :darwin "beagle"})

(get {:sundace "spaniel" :darwin "beagle"} :darwin)

(get {:sundace "spaniel" :darwin "beagle"} :snoopy)

(:darwin {:sundace "spaniel" :darwin "beagle"})

(:snoopy {:sundace "spaniel" :darwin "beagle"})

(def score {:stu nil :joey 100})

(:stu score)

(contains? score :stu)

(get score :stu :score-not-found)

(get score :aaron :score-not-found)

(def song {:name "Angus Dei"
           :artist "Krzysztof Penderecki"
           :album "Polish Requiem"
           :genre "Classical"})

(assoc song :kind "MPEG Audio File")

(dissoc song :genre)

(select-keys song [:name :artist])

(merge song {:size 8118166 :time 507245})

(merge-with
 concat
 {:rubble ["Barney"], :flinstone ["Fred"]}
 {:rubble ["Betty"], :flinstone ["Wilma"]}
 {:rubble ["Bam-Bam"], :flinstone ["Pebbles"]})

(require '[clojure.set :as s])

(def languages #{"java" "c" "d" "clojure"})
(def beverages #{"java" "chai" "pop"})

(s/union languages beverages)

(s/difference languages beverages)

(s/intersection languages beverages)

(def compositions
  #{{:name "The Art of the Fugue" :composet "J. S. Bach"}
    {:name "Musical Offering" :composer "J. S. Bach"}
    {:name "Requiem" :composer "Giuseppe Verdi"}
    {:name "Requiem" :composer "W. A. Mozart"}})
(def composers
  #{{:composer "J. S. Bach" :country "Germany"}
    {:composer "W. A. Mozart" :country "Austria"}
    {:composer "Giuseppe Verdi" :country "Italy"}})
(def nations
  #{{:nation "Germany" :language "German"}
    {:nation "Austria" :language "German"}
    {:nation "Italy" :language "Italian"}})

(s/rename compositions {:name :title})

(s/select #(= (:name %) "Requiem") compositions)

(s/project compositions [:name])

(s/join compositions composers)

(s/project
 (s/join
  (s/select #(= (:name %) "Requiem") compositions)
  composers)
 [:country])

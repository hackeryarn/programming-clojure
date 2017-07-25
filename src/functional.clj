(defn stack-consuming-fibo [n]
  (cond
    (= n 0) 0
    (= n 1) 1
    :else (+ (stack-consuming-fibo (- n 1))
             (stack-consuming-fibo (- n 2)))))

;; Will blow the stack. No TCO on JVM
(defn tail-fibo [n]
  (letfn [(fib
            [current next n]
            (if (zero? n)
              current
              (fib next (+ current next) (dec n))))]
    (fib 0N 1N n)))

(defn recur-fibo [n]
  (letfn [(fib
            [current next n]
            (if (zero? n)
              current
              (recur next (+ current next) (dec n))))]
    (fib 0N 1N n)))

(defn lazy-seq-fibo
  ([]
   (concat [0 1] (lazy-seq-fibo 0N 1N)))
  ([a b]
   (let [n (+ a b)]
     (lazy-seq
      (cons n (lazy-seq-fibo b n))))))

(defn fibo []
  (map first (iterate (fn [[a b]] [b (+ a b)]) [0N 1N])))

(rem (nth (fibo) 100000) 1000)

;; runs out of memory because it holds on to the head
(def head-fibo (lazy-cat [0N 1N] (map + head-fibo (rest head-fibo))))

(defn by-pairs [coll]
  (let [take-pair (fn [c]
                    (when (next c) (take 2 c)))]
    (lazy-seq
     (when-let [pair (seq (take-pair coll))]
       (cons pair (by-pairs (rest coll)))))))

(def count-if (comp count filter))

(defn count-runs
  "Count runs of length n where pred is true in coll."
  [n pred coll]
  (count-if #(every? pred %) (partition n 1 coll)))

(def count-head-pairs (partial count-runs 2 #(= % :h)))

(declare my-odd? my-even?)

(defn my-odd? [n]
  (if (= n 0)
    false
    (my-even? (dec n))))

(defn my-even? [n]
  (if (= n 0)
    true
    (my-odd? (dec n))))

(defn parity [n]
  (loop [n n par 0]
    (if (= n 0)
      par
      (recur (dec n) (- 1 par)))))

(defn trampoline-fibo [n]
  (let [fib (fn fib [f-2 f-1 current]
              (let [f (+ f-2 f-1)]
                (if (= n current)
                  f
                  #(fib f-1 f (inc current)))))]
    (cond
      (= n 0) 0
      (= n 1) 1
      :else (fib 0N 1 2))))

(rem (trampoline trampoline-fibo 100000) 1000)

;; trampoline even odd
(declare my-odd? my-even?)

(defn my-odd? [n]
  (if (= n 0)
    false
    #(my-even? (dec n))))

(defn my-even? [n]
  (if (= n 0)
    true
    #(my-odd? (dec n))))

(trampoline my-even? 1000000)

(declare replace-symbol replace-symbol-expression)

(defn replace-symbol [coll oldsym newsym]
  (if (empty? coll)
    ()
    (cons (replace-symbol-expression
           (first coll) oldsym newsym)
          (replace-symbol
           (rest coll) oldsym newsym))))

(defn replace-symbol-expression [symbol-expr oldsym newsym]
  (if (symbol? symbol-expr)
    (if (= symbol-expr oldsym)
      newsym
      symbol-expr)
    (replace-symbol symbol-expr oldsym newsym)))

(defn- coll-or-scalar [x & _] (if (coll? x) :collection :scalar))

(defmulti replace-symbol coll-or-scalar)

(defmethod replace-symbol :collection [coll oldsym newsym]
  (lazy-seq
   (when (seq coll)
     (cons (replace-symbol (first coll) oldsym newsym)
           (replace-symbol (rest coll) oldsym newsym)))))

(defmethod replace-symbol :scalar [obj oldsym newsym]
  (if (= obj oldsym) newsym obj))


(declare m f)

(defn m [n]
  (if (zero? n)
    0
    (- n (f (m (dec n))))))

(defn f [n]
  (if (zero? n)
    1
    (- n (m (f (dec n))))))

(def m (memoize m))
(def f (memoize f))

(def m-seq (map m (iterate inc 0)))
(def f-seq (map f (iterate inc 0)))

(time (nth m-seq 10000))

(defn square [x] (* x x))

(defn sum-squares
  [n]
  (into [] (map square) (range n)))

(defn preds-seq []
  (->> (all-ns)
       (map ns-publics)
       (mapcat vals)
       (filter #(clojure.string/ends-with? % "?"))
       (map #(str (.-sym %)))
       vec))

(defn preds []
  (into []
        (comp (map ns-publics)
           (mapcat vals)
           (filter #(clojure.string/ends-with? % "?"))
           (map #(str (.-sym %))))
        (all-ns)))

(defn non-blank? [s]
  (not (clojure.string/blank? s)))

(defn non-blank-lines-seq [file-name]
  (let [reader (clojure.java.io/reader file-name)]
    (filter non-blank? (line-seq reader))))

(defn non-blank-lines [file-name]
  (with-open [reader (clojure.java.io/reader file-name)]
    (into [] (filter non-blank?) (line-seq reader))))

(defn non-blank-lines-eduction [reader]
  (eduction (filter non-blank?) (line-seq reader)))

(defn line-count [file-name]
  (with-open [reader (clojure.java.io/reader file-name)]
    (reduce (fn [cnt el] (inc cnt)) 0 (non-blank-lines-eduction reader))))

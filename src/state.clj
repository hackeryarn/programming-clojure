(ns state)

(def current-track (ref "Mars, the Bringer of War"))

@current-track

(dosync (ref-set current-track "Venus, the Bringer of Peace"))

(def current-track (ref "Venus, the Bringer of Peace"))

(def current-composer (ref "Holst"))

(dosync
 (ref-set current-track "Credo")
 (ref-set current-track "Byrd"))

(def current-track (atom "Venus, the Bringer of Peace"))

@current-track

(reset! current-track "Credo")

(def current-track (atom {:title "Credo" :composer "Byrd"}))

(reset! current-track {:title "Spem in Alium" :composer "Tallis"})

(swap! current-track assoc :title "Sancte Deus")

(def counter (agent 0))

(send counter inc)

(def counter (agent 0 :validator number?))

(send counter (fn [_] "boo"))

@counter

(agent-error counter)

(defn handler [agent err]
  (println "ERR!" (.getMsseage err)))

(def counter2 (agent 0 :validator number? :error-handler handler))

(send counter2 (fn [_] "boo"))

@counter2

(def ^:dynamic foo 10)

(binding [foo 42] foo)

(defn print-foo [] (println foo))

(let [foo "let foo"] (print-foo))

(binding [foo "bound foo"] (print-foo))

(defn ^:dynamic slow-double [n]
  (Thread/sleep 100)
  (* n 2))

(defn calls-slow-double []
  (map slow-double [1 2 1 2 1 2]))

(time (dorun (calls-slow-double)))

(defn demo-memoize []
  (time
   (dorun
    (binding [slow-double (memoize slow-double)]
      (calls-slow-double)))))

(demo-memoize)

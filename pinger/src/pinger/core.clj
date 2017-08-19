(ns pinger.core
  (:import [java.net URL HttpURLConnection])
  (:gen-class))

(defn response-code [address]
  (let [conn ^HttpURLConnection (.openConnection (URL. address))
        code (.getResponseCode conn)]
    (when (< code 400)
      (-> conn .getInputStream .close))
    code))

(defn available? [address]
  (= 200 (response-code address)))

(defn -main []
  (let [addresses '("https://google.com"
                    "https://clojure.org"
                    "http://google.com/badurl")]
    (while true
      (doseq [address addresses]
        (println address ":" (available? address)))
      (Thread/sleep (* 1000 60)))))


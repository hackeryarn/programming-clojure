(ns chat)

(defrecord Message [sender text])

(->Message "Aaron" "Hello")

(defn valid-message? [msg]
  (and (:sender msg) (:text msg)))

(def validate-message-list #(every? valid-message? %))

(def messages (ref () :validator validate-message-list))

(defn add-message [msg]
  (dosync (alter messages conj msg)))

(add-message (->Message "user 1" "hello"))

(add-message (->Message "user 2" "howdy"))

(def backup-agent (agent "output/messages-backup.clj"))

(defn add-message-with-backup [msg]
  (dosync
   (let [snapshot (commute messages conj msg)]
     (send-off backup-agent (fn [filename]
                              (spit filename snapshot)
                              filename))
     snapshot)))

(add-message-with-backup (->Message "John" "Message One"))

(add-message-with-backup (->Message "Jane" "Message Two"))

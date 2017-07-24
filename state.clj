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



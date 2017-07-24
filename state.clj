(ns state)

(def current-track (ref "Mars, the Bringer of War"))

@current-track

(dosync (ref-set current-track "Venus, the Bringer of Peace"))

(def current-track (ref "Venus, the Bringer of Peace"))

(def current-composer (ref "Holst"))

(dosync
 (ref-set current-track "Credo")
 (ref-set current-track "Byrd"))



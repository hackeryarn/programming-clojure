(ns java-interop
  (:import [java.io File FilenameFilter]
           [org.xml.sax InputSource]
           [org.xml.sax.helpers DefaultHandler]
           [java.io StringReader]
           [javax.xml.parsers SAXParserFactory]))

(defn say-hi []
  (println "Hello from thread" (.getName (Thread/currentThread))))

(dotimes [_ 3]
  (.start (Thread. say-hi)))

(defn suffix-filter [suffix]
  (reify FilenameFilter
    (accept [this dir name]
      (.endsWith name suffix))))

(defn list-files [dir suffix]
  (seq (.list (File. dir) (suffix-filter suffix))))

(list-files "." ".clj")

(defrecord Counter [n]
  Runnable
  (run [this] (println (range n))))

(def c (->Counter 5))

(.start (Thread. c))

(def print-element-handler
  (proxy [DefaultHandler] []
    (startElement [uri local qname atts]
      (println (format "Saw element: %s" qname)))))

(defn demo-sax-parse [source handler]
  (.. SAXParserFactory newInstance newSAXParser
      (parse (InputSource. (StringReader. source)) handler)))

(demo-sax-parse "<foo>
<bar> Body of bar</bar>
</foo>" print-element-handler)

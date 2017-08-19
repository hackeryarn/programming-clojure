(ns java-interop
  (:import [java.io File FilenameFilter IOException StringReader]
           javax.xml.parsers.SAXParserFactory
           org.xml.sax.helpers.DefaultHandler
           org.xml.sax.InputSource))

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

(defn class-available? [class-name]
  (Class/forName class-name))

(defn class-available? [class-name]
  (try
    (Class/forName class-name) true
    (catch ClassNotFoundException _ false)))

(class-available? "borg.util.Assimilate")

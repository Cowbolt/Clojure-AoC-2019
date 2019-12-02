(ns advent-of-code.day1)
  
(defn calculate-fuel
  "calculates fuel required to launch a given mass.
  fuel required is mass//3 - 2."
  [mass]
  (- (quot (read-string mass) 3) 2))

(defn calculate-recursive-fuel
  "calculates fuel required to launch a given mass, including fuel required for fuel mass"
  [m]
  (loop [mass (read-string m)
         sum 0]
    (if (< mass 9); stop when mass would require <= 0 fuel. 
      sum
      (let [fuel (- (quot mass 3) 2)]
        (recur fuel (+ sum fuel))))))

(defn run
  []
  (with-open [rdr (clojure.java.io/reader "resources/1.txt")]
    (println (str "Required fuel is: " (reduce + (map calculate-fuel (line-seq rdr))))))
  (with-open [rdr (clojure.java.io/reader "resources/1.txt")]
    (println (str "Required recursive fuel is: " (reduce + (map calculate-recursive-fuel (line-seq rdr)))))))

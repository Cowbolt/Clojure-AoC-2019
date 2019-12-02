(ns advent-of-code.day2
  (:require [clojure.data.csv :as csv]
            [advent-of-code.utils :as utils]))

; CSV file of ints on a single line
; We parse 4 digits at a time
; Second, third, and fourth digits are pointers
; ex: 0,5,2,4,1,3,0. Here 5 points to the fifth digit, which is 3 (zero-indexed)
; First digit is an opcode.
; 99 means halt.
; 1 adds second and third pointer values, store result in fourth pointer
; 2 multiplies second and third pointer values, store result in fourth pointer
; All other opcodes are invalid, should exit.

(defn intcode-computer
  [input]
  (loop [mem input
         start 0]
    (let [expr (take 4 (drop start mem)) ; Should probably use subvec
          op (first expr)
          num1 (get mem (nth expr 1))
          num2 (get mem (nth expr 2))
          loc (last expr)]
      (cond
        (= op 1) (recur (assoc mem loc (+ num1 num2)) (+ start 4))
        (= op 2) (recur (assoc mem loc (* num1 num2)) (+ start 4))
        (= op 99) mem
        :else (throw (Exception. "Illegal opcode in input"))))))

(defn fix-input
  "Changes two values which are incorrect in the input file of task 1"
  []
  (with-open [rdr (clojure.java.io/reader "resources/2.txt")]
    (assoc (mapv utils/parse-int (flatten (csv/read-csv rdr))) 1 12 2 2)))

(defn find-input
  "Finds input values which yield a value of 19690720 at address 0."
  []
  (with-open [rdr (clojure.java.io/reader "resources/2.txt")]
    (let [mem (mapv utils/parse-int (flatten (csv/read-csv rdr)))]
      (loop [noun 0
             verb 0]
        ; FIXME: Catch potential nullpointer on invalid input and continue
        (let [output (intcode-computer (assoc mem 1 noun 2 verb))]
          (cond
            (= (first output) 19690720) (vec [noun verb])
            (=  verb 99) (recur (+ noun 1) 0)
            (=  noun verb 99) (throw (Exception. "No solution available"))
            :else (recur noun (+ verb 1))))))))

(defn run
  []
  (println (str "First task yields: " (intcode-computer (fix-input))))
  (println (str "Second task yields: " (find-input))))

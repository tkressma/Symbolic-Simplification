(ns cljdev.core)

;; This file contains some commentary on my thoughts while writing this code, as well as how it works.

;; Function provided by Professor
(defn lookup
  [i m]
  (get m i i))

;; This function is what allows us to substitute any value with its assigned value. So if '{x true},
;; then (not x) will be turned into (not true).
(defn bind-values [bindings exp]
  (map (fn [i]
         (if (seq? i)
           (bind-values bindings i)
           (lookup i bindings)))
       exp))


;; The and-simplify function is exact code used in the micro project, so in essence this is where the project was started. I began to
;; tackle this project by working on the or-simplify function, followed by the simplify and applyFn functions, then finished
;; with what was presumably the most challenging function, not-simplify.

;; While working on the and-simplify and or-simplify functions, I constantly referred to the list of simplification equivalencies
;; found on the assignment 2 web page (https://danielschlegel.org/wp/teaching/csc344-spring-2021/assignment-2/).
;; I first worked on getting the length 1 patterns to work, and went down the list until I was able to successfully get all
;; of the simplified forms to come out correct.

(defn and-simplify [expList]
  (cond
    (every? true? (rest expList)) true                      ;; (and true) => true
    (some false? expList) false                             ;; For cases where false exists, always return false!
    (and (= (count (distinct expList)) 3) (some true? (distinct expList))) (nth (remove true? (distinct expList)) 1) ;; For the cases (and x true) => x and (and true x) => x
    (some true? (distinct expList)) (remove true? (distinct expList)) ;; This was written for (and x true y) => (and x y)
    (= (count (distinct expList)) 2) (nth (distinct expList) 1) ;; For the case where '(and x||y||z), return just x||y||z.
    ::else (distinct expList)))

(defn or-simplify [expList]
  (cond
    (every? true? (rest expList)) true
    (some true? (rest expList)) true
    (and (= (count (distinct expList)) 3) (some false? (distinct expList))) (nth (remove false? (distinct expList)) 1) ;; (or x false) => x, (or false x) => x
    (some false? (distinct expList)) (remove false? (distinct expList)) ;; (or x false y) => (or x y)
    ::else (distinct expList)))

(defn not-simplify [expList]
  (cond
    (every? true? (rest expList)) false ;; (not true) => false
    (every? false? (rest expList)) true ;; (not false) => true
    (= (count (flatten expList)) 2) expList ;; For '(not x) => (not x)
    (= (first (second expList)) 'not) (second (second expList)) ;; For (not (not x)) => x
    :else (map (fn [i]
                 (cond
                   (= i 'and) 'or
                   (= i 'or) 'and
                   (seq? i) (cond
                              (= (nth i 0) 'not) (nth i 1)) :else (conj (list i) 'not)))
               (nth expList 1)))) ;; This is an anonymous function to deal with the pesky 
                            ;; DeMorgan's law simplification cases. This took a while to think
                            ;; about, but started to come together once I began to take care of
                            ;; the cases where and => or, or => and. Once those were dealth with,
                            ;; adding 'not to the right areas was addressed, therefore the case where
                            ;; (not (and x y)) => (or (not x) (not y)) can be derived.

;; Iterate through expression lists and apply simplification functions
;; This is fairly straightforward. It just checks the first value of a list and determines which function to send it to.
(defn applyFn [expList]
  (let [firstExp (first expList)]
    (cond
      (= firstExp 'not) (not-simplify expList)
      (= firstExp 'or) (or-simplify expList)
      (= firstExp 'and) (and-simplify expList)
      :else (distinct expList))))

(defn simplify [expList]
  (applyFn
   (map
    (fn [i] (if (seq? i) (do (simplify i)) i))
    (distinct expList))))

;; Test cases
(def p1 '(and x (or x (and y (not z)))))                    ;; {x false, z true} -> false
(def p2 '(and (and z false) (or x true)))
(def p3 '(not (not x)))
(def p4 '(and x (or x (and y (not z)))))                    ;; {z false} -> (and x (or x y))
(def p5 '(not (and y (not x))))                             ;; {y true} -> x
(def p6 '(not (and x y (not z))))                           ;; {x true} -> (or (not y) z)

;; Main function
(defn evalexp [exp bindings] (simplify (bind-values bindings exp)))

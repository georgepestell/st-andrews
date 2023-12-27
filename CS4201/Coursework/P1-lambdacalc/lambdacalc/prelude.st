putStrLn "natural numbers"
-- natural numbers  
assume (n :: *)
-- zero element
assume (z :: n)
-- successor element
assume (s :: n -> n)


let id = z :: n
-- churn numerals
let zero = (\f x -> x)    :: (n -> n) -> (n -> n)
let one = (\f x -> (f x)) :: (n -> n) -> (n -> n)
let two = (\f x -> (f (f x))) :: (n -> n) -> (n -> n)
-- just numbers
let n0 = zero s z
let n1 = one s z
let n2 = two s z
-- church addition
let add = (\m n -> ((\f x -> m f (n f x)) :: (n -> n) -> (n -> n))) 
	:: ((n -> n) -> (n -> n)) -> ((n -> n) -> (n -> n)) -> ((n -> n) -> (n -> n))

let three = add one two
let n3 = three s z

putStrLn "booleans"
assume (bool :: *)
assume (true_n :: bool)
assume (false_n :: bool)

let true_n = (\x y -> x) 	::  n -> n -> n
let false_n = (\x y -> y) 	:: n -> n -> n

let cond_n = (\b x y -> b x y) :: (n -> n -> n) -> n -> n -> n

-- Should return n1 and n2 respectively with type n
let cond_true = cond_n true_n n1 n2
let cond_false = cond_n false_n n1 n2
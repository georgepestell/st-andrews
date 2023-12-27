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

assume (true  :: bool)
assume (false :: bool)

assume (t :: bool)

let true = (\x y -> x) 	::  @ a ( @ b ( a -> b -> a ))
let false = (\x y -> y) 	:: @ a ( @ b ( a -> b -> b ))

-- On the fly, input types can be labelled for conditional it's values

let tn = true ! n
let tnb = tn ! bool

let fn = false ! n
let fnb = fn ! bool

let cond = (\b x y -> b x y) :: @ a ( @ b ( @ c ((a -> b -> c) -> a -> b -> c)))
let cond_b = cond ! n
let cond_nb = cond_b ! bool

-- What's left is the cond_nb with just the output type to label depending on the output given

-- You can then define the output type on-the-fly before application
let output_true = (cond_nb ! n) tnb n1 t
let output_false = (cond_nb ! bool) fnb n1 t

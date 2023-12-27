-- Create a data type
data Food = Apple Double Double
          | Orange Double Double
          | Pear Double Double
    deriving Show

maxV :: (Double, Double) -> Double
maxV (x, y) = if x >= y then x else y

minV :: (Double, Double) -> Double
minV (x, y) = if x <= y then x else y

price :: Food -> Double
price (Apple size daysTillOff) = (size * 0.35) * (minV(5.0,daysTillOff)/5)**2
price (Orange size daysTillOff) = (size * 1.35) * (minV(5.0,daysTillOff)/5)**2
price (Pear size daysTillOff) = (size * 0.65) * (minV(5.0,daysTillOff)/5)**2

prodList :: [Int] -> Int
prodList [] = 1
prodList (x : xs) = x * prodList xs

type Name = String
type Store = [(Name, Int)]

test :: Name
test = "george"

testStore :: Store
testStore = [(test, 5)]

addToStore :: (Name, Int) -> Store -> [(Name, Int)]
addToStore (name, int) store = store ++ [(name, int)]


test2 :: Store
test2 = testStore ++ [("george", 3),("test", 2)]


data Expr = Val Int
          | Var String
          | Add Expr Expr
          | Mult Expr Expr
          | Sub Expr Expr

eval :: Store -> Expr -> Maybe Int
eval store (Val int) = Just int
eval store (Var var) = lookup var store
eval store (Add x y) =
    do xValue <- eval store x 
       yValue <- eval store y
       Just (xValue + yValue)
eval store (Sub x y) =
    do xValue <- eval store x 
       yValue <- eval store y
       Just (xValue - yValue)
eval store (Mult x y) =
    case (eval store x, eval store y) of 
        (Just xValue, Just yValue) -> Just (xValue * yValue)
        _ -> Nothing

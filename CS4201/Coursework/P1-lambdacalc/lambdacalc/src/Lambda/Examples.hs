module Lambda.Examples where

import           Common
import           Lambda.AST
import           Lambda.Check
import           Lambda.Eval
import           Lambda.Quote

-- id' = (\x . x)
id' = Lam (Inf (Bound 0))

-- (\x.\y. x)
const' = Lam (Lam (Inf (Bound 1)))

tfree a = TFree (Global a)

free x = Inf (Free (Global x))

-- represents an annotated lambda applied to a term
-- (id :: a -> a)  y
term1 = Ann id' (Fun (tfree "a") (tfree "a")) :@: free "y"

-- (const :: (b -> b) -> a -> (b -> b)) y
term2 = (Ann
           const'
           (Fun
              (Fun (tfree "b") (tfree "b"))
              (Fun (tfree "a") (Fun (tfree "b") (tfree "b"))))
         :@: id')
  :@: free "y"

term3 = Fun (tfree "a") (tfree "b")

term4 = (Ann (Lam (Lam (Inf (Bound 1)))) (tfree "test") :@: free "x")
  :@: free "y"

-- an example environment with y :: a, a :: *
env1 = [(Global "y", HasType (tfree "a")), (Global "a", HasKind Star)]

-- b :: * U env1
env2 = (Global "b", HasKind Star):env1

test_eval1 = quote0 (iEval term1 ([], []))

test_eval2 = quote0 (iEval term2 ([], []))

test_eval3 = quote0 (cEval const' ([], []))

test_eval4 = quote0 (iEval term4 ([], []))

test_type1 = iType0 env1 term1

test_type2 = iType0 env2 term2
{-# OPTIONS_GHC -Wno-unrecognised-pragmas #-}

{-# HLINT ignore "Use camelCase" #-}
module IR.Defun where

type Name = String

data Op = Plus
        | Minus
        | Times
        | Divide
        | Eq
        | Lt
        | Gt
  deriving Eq

data Expr = Var Name
          | Val Int
          | Let Name Expr Expr
          | Call Expr [Expr]
          | Con Name [Expr]
          | If Expr Expr Expr
          | BinOp Op Expr Expr
          | Case Expr [CaseAlt]
          | Funs Name -- The name of the real function
                 Int -- The number of arguments for the real function
                 [Expr] -- The current list of arguments 
          | Apply Expr -- The function to call
                  [Expr] -- The arguments to call with
  deriving Eq

data CaseAlt = IfCon Name [Name] Expr
  deriving Eq

data Function = MkFun Name -- function name
                      [Name] -- argument names
                      Expr -- function body

data Program = MkProg [Function] -- all the function definitions
                      Expr -- expression to evaluate

instance Show Function where
  show (MkFun name args body) =
    "(MkFun " ++ name ++ " " ++ show args ++ " " ++ show body ++ ")"

instance Show Program where
  show (MkProg fs exp) = "(MkProg " ++ show exp ++ " " ++ show fs ++ ")"

instance Show Expr where
  show (Val i) = "(Val " ++ show i ++ ")"
  show (Var name) = "(Var " ++ name ++ ")"
  show (Let name val body) =
    "(Let " ++ name ++ " " ++ show val ++ " " ++ show body ++ ")"
  show (Call exp args) = "(Call " ++ show exp ++ " " ++ show args ++ ")"
  show (Con name vals) = "(Con " ++ name ++ " " ++ show vals ++ ")"
  show (If p c a) = "(If " ++ show p ++ " " ++ show c ++ " " ++ show a ++ ")"
  show (BinOp op a b) =
    "(BinOp " ++ show a ++ " " ++ show op ++ " " ++ show b ++ ")"
  show (Case val cases) = "(Case " ++ show val ++ " " ++ "TODO " ++ ")"
  show (Funs f i args) =
    "(Funs " ++ f ++ " " ++ show i ++ " " ++ show args ++ ")"
  show (Apply exp args) = "(Apply " ++ show exp ++ " " ++ show args ++ ")"

instance Show Op where
  show Plus = "+"
  show Times = "*"
  show Minus = "-"
  show Divide = "/"
  show Eq = "=="
  show Gt = ">"
  show Lt = "<"

defunArgs :: [Expr] -> [Function] -> [Expr]
defunArgs (v:vs) fs = let v_defun = defunExpr v fs
                          vs_defun = defunArgs vs fs
                      in v_defun:vs_defun
defunArgs [] fs = []

defunCases :: [CaseAlt] -> [Function] -> [CaseAlt]
defunCases ((IfCon name vs exp):cs) fs =
  let exp_defun = defunExpr exp fs
      cs_defun = defunCases cs fs
  in IfCon name vs exp_defun:cs_defun
defunCases [] fs = []

-- Checks whether the function is already defined in fs
-- then checks whether the arguments match
-- underapplied = 
-- correctly applied = do nothing
-- over-applied = 
defunCall :: Expr -> [Function] -> Expr
defunCall (Call (Var n) args) fs = case filter (\(MkFun f _ _) -> n == f) fs of
  ((MkFun n argNames body):_)
    -> let l_args = length args
           l_argNames = length argNames
       in if l_args == l_argNames
          then Call (Var n) args
          else if l_args < l_argNames -- Under-application
               then Funs n l_argNames args
               else 
                     -- Over-application
                 let fun_args = take l_argNames args
                     extra_args = take (l_args - l_argNames) (reverse args)
                 in Apply (Call (Var n) fun_args) extra_args
  _ -> error "Function not defined"
defunCall (Call (Call x2 a2) a1) fs = Apply (defunExpr (Call x2 a2) fs) a1
defunCall (Call (Funs f l_args curr_args) args) fs =
  Apply (Funs f l_args curr_args) args
defunCall exp fs = error ("TODO: defunCall for " ++ show exp)

defunExpr :: Expr -> [Function] -> Expr
defunExpr (Var x) fs = case filter (\(MkFun f _ _) -> x == f) fs of
  ((MkFun f argNames body):_) -> if (length argNames == 0)
                                 then Apply (Funs x (length argNames) []) []
                                 else Funs x (length argNames) []
  _ -> Var x
defunExpr (Val i) fs = Val i
defunExpr (BinOp op x y) fs = let x_defun = defunExpr x fs
                                  y_defun = defunExpr y fs
                              in BinOp op x_defun y_defun
defunExpr (Let n v b) fs = let v_defun = defunExpr v fs
                               b_defun = defunExpr b fs
                           in Let n v_defun b_defun
defunExpr (Call exp args) fs = let exp_defun = defunExpr exp fs
                                   args_defun = defunArgs args fs
                               in defunCall (Call exp_defun args_defun) fs
defunExpr (Con n vs) fs = let vs_defun = defunArgs vs fs
                          in Con n vs_defun
defunExpr (If p c a) fs = let p_defun = defunExpr p fs
                              c_defun = defunExpr c fs
                              a_defun = defunExpr a fs
                          in If p_defun c_defun a_defun
defunExpr (Case exp cs) fs = let exp_defun = defunExpr exp fs
                                 cs_defun = defunCases cs fs
                             in Case exp_defun cs_defun
defunExpr exp fs = error ("TODO: defun for Expr " ++ show exp)

defunFun :: Function -> [Function] -> Function
defunFun (MkFun name args body) fs = let defun_body = defunExpr body fs
                                     in MkFun name args defun_body

defunFuns :: [Function] -> [Function] -> [Function]
defunFuns xs fs = map (`defunFun` fs) xs

-- Defunctionalize program
defun :: Program -> Program
defun (MkProg fs exp) =
  let fs_f = defunFuns fs fs
      defun_exp = defunExpr exp fs_f
  in
      -- apply = MkFun "APPLY" ["f"] ()
     MkProg fs defun_exp

-- Function definitions, written as syntax trees
{-
double(val) = val * 2
-}
double_def = MkFun "double2" ["val"] (BinOp Times (Var "val") (Val 2))

{-
factorial(x) = if x == 0
                  then 1
                  else x * factorial(x-1)
-}
factorial_def = MkFun
  "factorial2"
  ["x"]
  (If
     (BinOp Eq (Var "x") (Val 0))
     (Val 1)
     (BinOp
        Times
        (Var "x")
        (Call (Var "factorial2") [BinOp Minus (Var "x") (Val 1)])))

{-
fst(p) = case p of
              MkPair(x,y) -> x
-}
fst_def =
  MkFun "fst" ["p"] (Case (Var "p") [IfCon "MkPair" ["x", "y"] (Var "x")])

{-
snd(p) = case p of
              MkPair(x,y) -> y
-}
snd_def =
  MkFun "snd" ["p"] (Case (Var "p") [IfCon "MkPair" ["x", "y"] (Var "y")])

{-
sum(xs) = case xs of
               Nil -> 0
               Cons(y, ys) -> y + sum(ys)
-}
sum_def = MkFun
  "sum"
  ["xs"]
  (Case
     (Var "xs")
     [ IfCon "Nil" [] (Val 0)
     , IfCon
         "Cons"
         ["y", "ys"]
         (BinOp Plus (Var "y") (Call (Var "sum") [Var "ys"]))])

{-
testlist() = Cons 1 (Cons 2 Nil)
-}
testlist_def =
  MkFun "testlist" [] (Con "Cons" [Val 1, Con "Cons" [Val 2, Con "Nil" []]])

{-
map(f, xs) = case xs of
                  Nil -> Nil
                  Cons(y,ys) -> Cons(f(y), map(f,ys))
-}
map_def = MkFun
  "map"
  ["f", "xs"]
  (Case
     (Var "xs")
     [ IfCon "Nil" [] (Con "Nil" [])
     , IfCon
         "Cons"
         ["y", "ys"]
         (Con
            "Cons"
            [Call (Var "f") [Var "y"], Call (Var "map") [Var "f", Var "ys"]])])

allDefs =
  [double_def, factorial_def, fst_def, snd_def, sum_def, testlist_def, map_def]

-- Should evaluate to 6
testProg1 = MkProg allDefs (Call (Var "double2") [Val 3])

testProgSimple =
  MkProg [double_def] (Call (Var "double2") [BinOp Plus (Val 3) (Val 9)])

testProgSimple2 =
  MkProg [factorial_def] (Call (Var "factorial2") [BinOp Plus (Val 3) (Val 9)])

-- Should evaluate to 120
testProg2 = MkProg allDefs (Call (Var "factorial2") [Val 5])

-- Should evaluate to 1
testProg3 = MkProg allDefs (Call (Var "fst") [Con "MkPair" [Val 1, Val 2]])

-- Should evaluate to 3
testProg4 = MkProg allDefs (Call (Var "sum") [Var "testlist"])

-- Should evaluate to 6
testProg5 = MkProg
  allDefs
  (Call (Var "sum") [Call (Var "map") [Var "double2", Var "testlist"]])

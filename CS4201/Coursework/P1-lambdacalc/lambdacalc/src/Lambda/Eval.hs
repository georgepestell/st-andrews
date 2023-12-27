module Lambda.Eval where

import           Common
import           Lambda.AST

{-
    Evaluates an (inferrable) term to a normal form.

    The result is a Value type.

    Inputs:
        ITerm : the term to evaluated
        (NameEnv Value, Env), the context in which we perform the evaluation.
            See Common.hs for the definition of NameEnv and Env.
-}
iEval :: ITerm -> (NameEnv Value, Env) -> Value
iEval (Ann exp t) d = cEval exp d
iEval (Bound i) (context, boundVars) = boundVars !! i
iEval (Free name) (context, boundVars) = case lookup name context of
  Just name -> name -- Use value if set in environment
  Nothing   -> VNeutral (NFree name)
iEval (iExp :@: cExp) d = do
  case iEval iExp d of
    VLam lam         -> lam v
    VNeutral neutral -> VNeutral (NApp neutral v)
  where
    v = cEval cExp d

{-
    Evaluates a (checkable) term to a normal form.

    The result is a Value type.

    Inputs:
        TermChk : the checkable term to be evaluated
        Env: the context in which we perform the evaluation.
            See Common.hs for the definition of Env.
-}
cEval :: CTerm -> (NameEnv Value, Env) -> Value
cEval (Inf exp) d = iEval exp d
cEval (Lam exp) d = VLam (lambdaFunc exp d) -- Turn lambda expression into function replacing (Bound idx) with the givn Value

lambdaFunc :: CTerm -> (NameEnv Value, Env) -> Value -> Value
lambdaFunc exp (context, boundVars) x = cEval exp (context, x:boundVars)
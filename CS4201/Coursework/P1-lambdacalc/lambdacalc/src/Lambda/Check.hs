module Lambda.Check where

import           Common
import           Control.Monad.Except
import           Lambda.AST

{-
  cKind checks a type can be formulated or is a type (or "kind")
  It returns Result () if the type is well formulated, or an error message otherwise.
    Inputs
      Context : The typing context or Gamma
      Type : the type to check formulation
      The typing Kind to check against

  Example: one may declare a new type:
      Nat :: *
    Where Nat is a Type. We need to check this is a valid formulation.
-}
cKind :: Context -> Type -> Kind -> Result ()
cKind g (TFree t) Star = case lookup t g of
  Just (HasKind Star) -> Right ()
  Just t -> error "Type invalid"
  _ -> error "Type not defined"
cKind g (Fun inType outType) Star = do
  cKind g inType Star -- Check input type is valid
  cKind g outType Star -- Check output type is valid

{-
  A simple wrapper function for iType, starting with 0 bindings
-}
iType0 :: Context -> ITerm -> Result Type
iType0 = iType 0

{-
   iType performs type "inference", where a type is inferred from a context
      -- It returns Result Type, where Type is the inferred Type, otherwise an error message.
   Result is defined in Common.hs as Either an error or a result.
   Inputs:
      Int : to keep track of the number of lambda bindings
      Int : to keep track of the number of polymorphic types
      Context : The typing context, or Gamma
      CTerm : The inferrable Term to infer

  Example:
    We can infer the types of some terms in the calculus
      (see the rules in the lecture slides for where this might be achieved)
-}
iType :: Int -> Context -> ITerm -> Result Type
iType ii g (Ann exp t) = do
  cKind g t Star
  case cType ii g exp t of
    Right ()   -> Right t
    Left error -> Left error
iType ii g (Free x) = case lookup x g of
  Just (HasType t) -> do
    cKind g t Star
    Right t
  _ -> error "Free variable type not defined"
iType ii g (exp1 :@: exp2) = case iType ii g exp1 of
  Right (Fun inType outType) -> do
    cKind g inType Star
    cKind g outType Star
    -- Check the applied expression matches the input type
    cType ii g exp2 inType
    Right outType -- Return function output type
  Right t -> error "Applications can only be made onto functions"
iType ii g (Bound i) = case lookup (Local (ii - i - 1)) g of
  Just (HasType t) -> do
    cKind g t Star
    Right t
  Nothing          -> error "Bound variable type not defined"

{-
   cType performs type "checking"
     it returns Result () if the type is correct (or "checks"),
             otherwise we return an error message.
     Result is defined in Common.hs as Either an error messsage or a result.

   Inputs:
    Int : to keep track of the number of lambda bindings
    Int : to keep track of the number of polymorphic types
    Context : The typing context, or Gamma
    CTerm : The checkable Term to type check
    Type : The type to check against

  We can check the following lambda term has the following annotated type (or not)
    (\x . \y . x y) :: (n -> n) -> n -> n
-}
cType :: Int -> Context -> CTerm -> Type -> Result ()
cType ii g (Inf exp) t = case iType ii g exp of
  Right expType -> if t == expType
                   then Right ()
                   else error "Inferred type doesn't match"
  Left error    -> Left error
cType ii g (Lam exp) (Fun inType outType) =
  cType (ii + 1) ((Local ii, HasType inType):g) exp outType
cType ii g (Lam exp) t = error "Lambda type must be a function"

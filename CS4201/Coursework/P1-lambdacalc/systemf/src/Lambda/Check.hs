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
cKind :: Int -> Context -> Type -> Kind -> Result ()
cKind u g (TUni t) Star = cKind (u + 1) g t Star
cKind u g (TFree (Uni i)) Star = case lookup (Uni (u - i - 1)) g of
  Just (HasType t) -> cKind u g t Star
  Just (HasKind Star) -> Right ()
  Nothing -> if (u - i) < 1
             then error "Universal type out of bounds"
             else Right ()
cKind u g (TFree t) Star = case lookup t g of
  Just (HasKind Star) -> Right ()
  Just (HasType (TUni t)) -> Right ()
  Just t -> error "Type invalid"
  _ -> error "Type not defined"
cKind u g (Fun inType outType) Star = do
  cKind u g inType Star -- Check input type is valid
  cKind u g outType Star -- Check output type is valid

{-
  A simple wrapper function for iType, starting with 0 bindings
-}
iType0 :: Context -> ITerm -> Result Type
iType0 = iType 0 0

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
updateContext :: Context -> Name -> Info -> Context
updateContext g x i = map
  (\v -> if fst v == x
         then (fst v, i)
         else v)
  g

iType :: Int -> Int -> Context -> ITerm -> Result Type
iType ii uu g (Ann exp t) = do
  if uu > 0
    then case forallReplace uu g t of
      Right new_t -> do
        cKind uu g new_t Star
        case cType ii uu g exp new_t of
          Right ()   -> Right new_t
          Left error -> Left error
    else do
      cKind uu g t Star
      case cType ii uu g exp t of
        Right ()   -> Right t
        Left error -> Left error
iType ii uu g (Free x) = case lookup x g of
  Just (HasType (TUni t)) -> case lookup (Uni uu) g of
    Just _  -> case updateContext g x (HasType t) of
      ug -> iType ii (uu + 1) ug (Free x)
    Nothing -> case forallReplace (uu + 1) g t of
      Right new_t -> Right (TUni new_t)
  Just (HasType (TFree (Uni i))) -> case lookup (Uni (uu - i - 1)) g of
    Just (HasType realType) -> do
      cKind uu g realType Star
      Right realType
    Nothing -> Right (TFree (Uni i))
  Just (HasType t) -> do
    case forallReplace uu g t of
      Right new_t -> do
        cKind uu g new_t Star
        Right new_t
  _ -> error "Free variable type not defined"
iType ii uu g (exp1 :@: exp2) = case iType ii uu g exp1 of
  Right (Fun inType outType)
    -> case cType ii uu g exp2 inType    -- Check input type matches function input type
     of
      Right () -> do
        if uu <= 0
          then Right outType -- Return function output type
          else forallReplace uu g outType
  Right t -> error "Lambda type"
iType ii uu g (exp :!: uType) = do
  cKind uu g uType Star
  iType ii uu ((Uni uu, HasType uType):g) exp
iType ii uu g (Bound i) = case lookup (Local (ii - i - 1)) g of
  Just (HasType t) -> do
    cKind uu g t Star
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
cType :: Int -> Int -> Context -> CTerm -> Type -> Result ()
cType ii u g e (TUni t) = cType ii (u + 1) g e t
cType ii u g (Inf exp) t = case iType ii u g exp of
  Right expType -> if t == expType
                   then Right ()
                   else error "Inferred type doesn't match"
  Left error    -> Left error
cType ii u g (Lam exp) (Fun inType outType) =
  cType (ii + 1) u ((Local ii, HasType inType):g) exp outType
cType ii u g (Lam exp) t = error "Lambda type must be a function"

forallReplace :: Int -> Context -> Type -> Result Type
forallReplace u g (TFree (Uni i)) = case lookup (Uni (u - i - 1)) g of
  Just (HasType t) -> Right t
  _ -> do
    Right (TFree (Uni i))
forallReplace u g (TUni t) = case forallReplace (u + 1) g t of
  Right tt -> Right (TUni tt)
forallReplace u g (Fun t1 t2) = case forallReplace u g t1 of
  Right t1p -> case forallReplace u g t2 of
    Right t2p -> Right (Fun t1p t2p)
  _         -> error "Failed to replace function uni vars"
forallReplace u g t = Right t

module Lambda.AST where

import           Common

-- inferable term
data ITerm =
    Ann CTerm Type
  | Bound Int -- The binding position of variables. (\x . x) = Bound 0. (\x.\y . x y) = (Bound 1) (Bound 0)
  | Free Name -- Free variables need to be recorded in an "environment"
  | ITerm :@: CTerm -- Applications
  | ITerm :!: Type
  deriving (Show, Eq)

-- checkable term
data CTerm = Inf ITerm
           | Lam CTerm
  deriving (Show, Eq)

{- Value and Neutral represents a normal form
      Normal forms can be lambdas (functions), variables or applications.
-}
data Value = VLam (Value -> Value)
           | VNeutral Neutral

data Neutral = NFree Name
             | NBound Int
             | NApp Neutral Value

data Kind = Star
  deriving (Show)

data Info = HasKind Kind
          | HasType Type
  deriving (Show)

type Context = [(Name, Info)]

type Env = [Value]

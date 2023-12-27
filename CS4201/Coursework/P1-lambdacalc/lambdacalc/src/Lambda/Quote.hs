module Lambda.Quote where

import           Common
import           Lambda.AST

quote0 :: Value -> CTerm
quote0 = quote 0

{-
    Converts a "Value" to a Checkable term
      This is mostly used for debugging and printing purposes.
-}
quote :: Int -> Value -> CTerm
quote boundCount (VLam lam) = Lam
  (quote (boundCount + 1) (lam (VNeutral (NBound boundCount)))) -- Convert the function to a lambda expression and increment count of bound vars
quote boundCount (VNeutral neutral) = Inf (neutralQuote boundCount neutral) -- Extract the neutral from a VNeutral

{-
    Converts a Neutral Value into an Inferrable term.
        This is mostly used for debugging and printing purposes.
-}
neutralQuote :: Int -> Neutral -> ITerm
neutralQuote
  boundCount
  (NFree name) = Free name -- Convert the non-value assigned free var to a
neutralQuote
  boundCount
  (NBound i) = Bound (boundCount - i - 1) -- Calculate the relative bound index (De Bujn Indexing)
neutralQuote boundCount (NApp neutral value) =
  neutralQuote boundCount neutral :@: quote boundCount value -- Convert unappliable application to an application statemnt
module QuoteSpec (spec) where

import           Common
import           Lambda.AST
import           Lambda.Quote
import           Test.Hspec

spec :: Spec
spec = do
  context "Unit Test"
    $ do
      describe "Neutral Quote"
        $ do
          it "converts neutral free variable "
            $ do
              neutralQuote 0 (NFree (Global "x")) `shouldBe` Free (Global "x")
          it "converts neutral bound variable to index with one bound var"
            $ do
              neutralQuote 1 (NBound 0) `shouldBe` Bound 0
          it
            "converts neutral bound variable to index with multiple bound vars"
            $ do
              neutralQuote 8 (NBound 2) `shouldBe` Bound 5
          it "converts neutral application"
            $ do
              neutralQuote
                0
                (NApp (NFree (Global "x")) (VNeutral (NFree (Global "y"))))
                `shouldBe` Free (Global "x")
                :@: Inf (Free (Global "y"))
      describe "Quote"
        $ do
          it "converts neutral value by passing it to neutralQuote"
            $ do
              quote0 (VNeutral (NFree (Global "x")))
                `shouldBe` Inf (Free (Global "x"))

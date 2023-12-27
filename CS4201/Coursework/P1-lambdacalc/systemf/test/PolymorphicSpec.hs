module PolymorphicSpec (spec) where

import Common
import Control.Exception
import Lambda.AST
import Lambda.Check
import Test.Hspec

spec :: Spec
spec = do
  describe "Test checking polymorphic type" $ do
    it "passes when single Uni type with empty context" $ do
      cKind 0 [] (TUni (TFree (Uni 0))) Star `shouldBe` Right ()
    it "fails when single Uni type references out of bounds type" $ do
      evaluate (cKind 0 [] (TUni (TFree (Uni 1))) Star)
        `shouldThrow` anyException
    it "passes with recursive TUni types" $ do
      cKind 0 [] (TUni (TUni (TFree (Uni 1)))) Star `shouldBe` Right ()
    it "fails with multiple recursive TUni types but Uni out of bounds" $ do
      evaluate (cKind 0 [] (TUni (TUni (TFree (Uni 2)))) Star)
        `shouldThrow` anyException
  describe "Test inferring polymorphic type from label" $ do
    it "Replaces single Uni variable type correctly" $ do
      iType 0 0 [(Global "x", HasType (TUni (TFree (Uni 0)))), (Global "n", HasKind Star)] (Free (Global "x") :!: TFree (Global "n"))
        `shouldBe` Right (TFree (Global "n"))
    it "Replaces multiple Uni variables types correctly" $ do
      iType 0 0 [(Global "x", HasType (TUni (Fun (TUni (TFree (Uni 0))) (TFree (Uni 0))))), (Global "n", HasKind Star), (Global "A", HasKind Star)] ((Free (Global "x") :!: TFree (Global "n")) :!: TFree (Global "A"))
        `shouldBe` Right (Fun (TUni (TFree (Uni 0))) (TFree (Global "n")))

-- it "throws error when free variable not in context"
-- it "returns bound variable type from context" $ do
--   iType 0 0 [(Local 0, HasType (TFree (Global "A")))]
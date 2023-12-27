module CheckSpec (spec) where

import           Common
import           Control.Exception
import           Lambda.AST
import           Lambda.Check
import           Test.Hspec
import           Test.Hspec.QuickCheck
import           Test.QuickCheck (Arbitrary, arbitrary, oneof)

prop_cKind_emptyContext :: Type -> IO ()
prop_cKind_emptyContext t = evaluate (cKind [] t Star)
  `shouldThrow` anyException

instance Arbitrary Name where
  arbitrary = do
    Global <$> arbitrary

instance Arbitrary Type where
  arbitrary = oneof [arbitraryTFree, arbitraryFunction]
    where
      arbitraryTFree = TFree <$> arbitrary

      arbitraryFunction = do
        inType <- arbitrary
        outType <- arbitrary
        return $ Fun inType outType

spec :: Spec
spec = do
  describe "Unit test"
    $ do
      describe "cKind type verification"
        $ do
          it "passes for variable types set in context"
            $ do
              cKind [(Global "A", HasKind Star)] (TFree (Global "A")) Star
                `shouldBe` Right ()
          it "fails for variable types not set in context"
            $ do
              evaluate (cKind [] (TFree (Global "A")) Star)
                `shouldThrow` anyException
          it
            "passes for function types with valid input and output variable types"
            $ do
              cKind
                [(Global "A", HasKind Star), (Global "B", HasKind Star)]
                (Fun (TFree (Global "A")) (TFree (Global "B")))
                Star
                `shouldBe` Right ()
          it "passes for recursive functions with valid variable node types"
            $ do
              cKind
                [(Global "A", HasKind Star), (Global "B", HasKind Star)]
                (Fun
                   (Fun (TFree (Global "A")) (TFree (Global "A")))
                   (TFree (Global "B")))
                Star
                `shouldBe` Right ()
          it "fails for function tpyes with invalid input type"
            $ do
              evaluate
                (cKind
                   [(Global "A", HasKind Star)]
                   (Fun (TFree (Global "A")) (TFree (Global "B")))
                   Star)
                `shouldThrow` anyException
          it "fails for function type with invalid output type"
            $ do
              evaluate
                (cKind
                   [(Global "B", HasKind Star)]
                   (Fun (TFree (Global "A")) (TFree (Global "B")))
                   Star)
                `shouldThrow` anyException
          it "fails for recursive functions with one invalid node"
            $ do
              evaluate
                (cKind
                   [(Global "A", HasKind Star), (Global "B", HasKind Star)]
                   (Fun
                      (Fun (TFree (Global "A")) (TFree (Global "C")))
                      (TFree (Global "B")))
                   Star)
                `shouldThrow` anyException
      describe "iEval type inferrence"
        $ do
          it "returns free variable type in context"
            $ do
              iType
                0
                [ (Global "A", HasKind Star)
                , (Global "x", HasType (TFree (Global "A")))]
                (Free (Global "x"))
                `shouldBe` Right (TFree (Global "A"))
          it "throws error if free variable type not set in context"
            $ do
              evaluate
                (iType
                   0
                   [(Global "x", HasType (TFree (Global "A")))]
                   (Free (Global "x")))
                `shouldThrow` anyException
          it
            "of annotated lambda expression returns output type if input type matches"
            $ do
              iType
                0
                [ (Global "x", HasType (TFree (Global "A")))
                , (Global "B", HasKind Star)
                , (Global "A", HasKind Star)]
                (Ann
                   (Lam (Inf (Free (Global "x"))))
                   (Fun (TFree (Global "A")) (TFree (Global "A"))))
                `shouldBe` Right
                  (Fun (TFree (Global "A")) (TFree (Global "A")))
          it
            "of annotated lambda expression throws exception if annotation type isn't a function"
            $ do
              evaluate
                (iType
                   0
                   [ (Global "x", HasType (TFree (Global "A")))
                   , (Global "B", HasKind Star)
                   , (Global "A", HasKind Star)]
                   (Ann (Lam (Inf (Free (Global "x")))) (TFree (Global "A"))))
                `shouldThrow` anyException
          it
            "of annotated lambda expression throws exception if input type doesn't match"
            $ do
              evaluate
                (iType
                   0
                   [ (Global "x", HasType (TFree (Global "B")))
                   , (Global "B", HasKind Star)
                   , (Global "A", HasKind Star)]
                   (Ann
                      (Lam (Inf (Free (Global "x"))))
                      (Fun (TFree (Global "A")) (TFree (Global "A")))))
                `shouldThrow` anyException
  describe "Property test"
    $ do
      describe "cKind type checking verification"
        $ do
          prop "fails when context is empty"
        $ prop_cKind_emptyContext
-- it "throws error when free variable not in context"
-- it "returns bound variable type from context" $ do
--   iType 0[(Local 0, HasType (TFree (Global "A")))]
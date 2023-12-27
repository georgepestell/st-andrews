module EvalSpec (spec) where

import           Common
import           Control.Exception (evaluate)
import           Lambda.AST
import           Lambda.Eval
import           Lambda.Quote
import           Test.Hspec
import           Test.Hspec.QuickCheck
import           Test.QuickCheck (Arbitrary, arbitrary, oneof, choose, Property)

-- exampleValue :: Value
-- exampleValue = VNeutral (NFree (Global "x"))
freeVar :: String -> ITerm
freeVar x = Free (Global x)

freeValue :: String -> Value
freeValue x = VNeutral (NFree (Global x))

emptyDomain :: (NameEnv Value, Env)
emptyDomain = ([], [])

domain1 :: (NameEnv Value, Env)
domain1 =
  ( [(Global "x", freeValue "t")]
  , [freeValue "z", freeValue "one", freeValue "two", freeValue "three"])

type1 :: Type
type1 = TFree (Global "A")

spec :: Spec
spec = do
  describe "Test evaluation"
    $ do
      it "returns correct bound value set in environment"
        $ do
          quote0 (iEval (Bound 2) domain1) `shouldBe` Inf (Free (Global "two"))
      it "throws an error when bound variable not wrapped in lambda expression"
        $ do
          evaluate (quote0 (iEval (Bound 0) emptyDomain))
            `shouldThrow` anyException
      it "returns correct free value set in environment"
        $ do
          quote0 (iEval (freeVar "x") domain1)
            `shouldBe` Inf (Free (Global "t"))
      it "for free vars not set in "
        $ do
          quote0 (iEval (freeVar "y") emptyDomain)
            `shouldBe` Inf (Free (Global "y"))
      it "Test Inferrable terms pass evaluation to iEval with same environment"
        $ do
          quote0 (cEval (Inf (freeVar "x")) domain1)
            `shouldBe` Inf (Free (Global "t"))
      it "Test Annotations passes evaluation onto cEval with same environment"
        $ do
          quote0 (iEval (Ann (Inf (freeVar "x")) type1) domain1)
            `shouldBe` Inf (Free (Global "t"))
      it "Test unappliable application evaluates to a neutral"
        $ do
          quote0 (iEval (freeVar "x" :@: Inf (freeVar "y")) emptyDomain)
            `shouldBe` Inf (Free (Global "x") :@: Inf (Free (Global "y")))
      it "Test quote lambda function value replaces with correct"
        $ do
          quote0 (VLam (lambdaFunc (Inf (Bound 0)) ([], [])))
            `shouldBe` Lam (Inf (Bound 0))
      it "Test embedded lamda function values replace correctly"
        $ do
          quote0 (VLam (lambdaFunc (Lam (Inf (Bound 1))) ([], [])))
            `shouldBe` Lam (Lam (Inf (Bound 1)))
      it "Test application of lambda function to value replaces bound instance"
        $ do
          quote0
            (iEval
               (Ann (Lam (Inf (Bound 0))) type1 :@: Inf (freeVar "y"))
               emptyDomain)
            `shouldBe` Inf (Free (Global "y"))
      it
        "Test application of multiple lambda functions to values applies correctly"
        $ do
          -- -- \x.\y.(x y) z = \y.(z y) =
          quote0
            (iEval
               (Ann (Lam (Lam (Inf (Bound 1 :@: Inf (Bound 0))))) type1
                :@: Inf (Free (Global "z")))
               emptyDomain)
            `shouldBe` Lam (Inf (Free (Global "z") :@: Inf (Bound 0)))
  describe "Property test"
    $ do
      prop "infer does not fail for valid expressions" prop_inferDoesNotCrash

prop_inferDoesNotCrash :: ITerm -> IO ()
prop_inferDoesNotCrash e = do
  -- If this fails, it will crash and not reach the trivial assertion
  evaluate (iEval (Ann (Lam (Inf e)) (TFree (Global "n"))) ([], []))
  1 `shouldBe` 1

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

instance Arbitrary ITerm where
  arbitrary = oneof [arbAnn, arbBound, arbFree]
    where
      arbAnn = do
        t <- arbitrary
        e <- arbitrary
        return $ Ann e t

      arbBound = do
        return $ Bound 0

      arbFree = do
        name <- arbitrary
        return $ Free name

instance Arbitrary CTerm where
  arbitrary = oneof [arbInf, arbLam]
    where
      arbInf = Inf <$> arbitrary

      arbLam = Lam <$> arbitrary
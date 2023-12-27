module DefunSpec (spec) where

import           IR.Defun
import           Test.Hspec

spec :: Spec
spec = do
  describe "DefunExpr Unit Test"
    $ do
      it "Does not change Vars"
        $ do
          defunExpr (Var "x") [] `shouldBe` Var "x"
      it "Does not change Vals"
        $ do
          defunExpr (Val 3) [] `shouldBe` Val 3
      it "Converts function calls to defined functions to applications on Funs"
        $ do
          defunExpr
            (Call (Var "double2") [BinOp Plus (Val 3) (Val 9)])
            [MkFun "double2" ["val"] (BinOp Times (Var "val") (Val 2))]
        `shouldBe` Apply (Funs "double2" 1 []) [BinOp Plus (Val 3) (Val 9)]
module ANFSpec (spec) where

import           IR.ANF
import           IR.Defun
import           Test.Hspec

spec :: Spec
spec = do
  describe "Unit Test ANF Expr Conversion"
    $ do
      it "converts Var to AVar"
        $ do
          exprToANF (Var "x") "ANF_" `shouldBe` AVar "x"
      it "converts Val to AVal"
        $ do
          exprToANF (Val 4) "ANF_" `shouldBe` AVal 4
      it "extracts two sides of BinOp to correct variable names"
        $ do
          exprToANF (BinOp Plus (Var "x") (Val 3)) "ANF_"
            `shouldBe` ALet
              "ANF_l"
              (AVar "x")
              (ALet "ANF_r" (AVal 3) (ABinOp Plus "ANF_l" "ANF_r"))
      it "extracts Call arguments into let expressions correctly"
        $ let f_name = "test"
          in exprToANF (Call (Var f_name) [Var "y", Val 6]) "ANF_"
             `shouldBe` ALet
               "ANF_fa"
               (AVar "y")
               (ALet "ANF_faa" (AVal 6) (ACall f_name ["ANF_fa", "ANF_faa"]))
      it "extracts value of Let expression correctly"
        $ do
          exprToANF (Let "x" (Val 3) (Var "x")) "ANF_"
        `shouldBe` ALet "ANF_lv" (AVal 3) (ALet "x" (AVar "ANF_lv") (AVar "x"))
      it "names ANF vars in Let body uniquely"
        $ do
          exprToANF (Let "x" (Val 3) (BinOp Times (Var "x") (Val 2))) "ANF_"
        `shouldBe` ALet
          "ANF_lv"
          (AVal 3)
          (ALet
             "x"
             (AVar "ANF_lv")
             (ALet
                "ANF_lbl"
                (AVar "x")
                (ALet "ANF_lbr" (AVal 2) (ABinOp Times "ANF_lbl" "ANF_lbr"))))
      it "extracts the premise of an If statement into a let expression"
        $ do
          exprToANF (If (Val 3) (Val 2) (Val 1)) "ANF_"
        `shouldBe` ALet "ANF_i" (AVal 3) (AIf "ANF_i" (AVal 2) (AVal 1))
      it "names ANF vars in if conclusion and alternative correctly"
        $ do
          exprToANF
            (If
               (Val 3)
               (BinOp Divide (Val 10) (Val 2))
               (BinOp Times (Val 10) (Val 2)))
            "ANF_"
        `shouldBe` ALet
          "ANF_i"
          (AVal 3)
          (AIf
             "ANF_i"
             (ALet
                "ANF_itl"
                (AVal 10)
                (ALet "ANF_itr" (AVal 2) (ABinOp Divide "ANF_itl" "ANF_itr")))
             (ALet
                "ANF_iel"
                (AVal 10)
                (ALet "ANF_ier" (AVal 2) (ABinOp Times "ANF_iel" "ANF_ier"))))

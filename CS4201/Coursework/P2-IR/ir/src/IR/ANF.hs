{-# OPTIONS_GHC -Wno-overlapping-patterns #-}

module IR.ANF where

import           IR.Defun
import           Data.List

data ANFExpr = AVar Name
             | AVal Int
             | ALet Name ANFExpr ANFExpr
             | ACall Name [Name]
             | ACon Name [Name]
             | AIf Name ANFExpr ANFExpr
             | ABinOp Op Name Name
             | ACase Name [ACaseAlt]
             | AFuns Name Int [Name]
             | AApply Name [Name]
  deriving Eq

data ACaseAlt = AIfCon Name [Name] ANFExpr
  deriving Eq

data ANFFunction =
  MkAFun { fnName :: Name, argNames :: [Name], definition :: ANFExpr }

data ANFProgram = MkAProg { defs :: [ANFFunction], mainExpr :: ANFExpr }

instance Show ACaseAlt where
  show (AIfCon t v exp) =
    "(AIfCon " ++ t ++ " " ++ show v ++ " " ++ show exp ++ ")"

instance Show ANFExpr where
  show (AVal i) = "(AVal " ++ show i ++ ")"
  show (AVar name) = "(AVar " ++ name ++ ")"
  show (ALet name val body) =
    "(ALet " ++ name ++ " " ++ show val ++ " in " ++ show body ++ ")"
  show (ACall name args) = "(ACall " ++ name ++ " " ++ show args ++ ")"
  show (ACon name vals) = "ACon " ++ name ++ " " ++ show vals ++ ")"
  show (AIf p c a) = "(AIf " ++ p ++ ", " ++ show c ++ ", " ++ show a ++ ")"
  show (ABinOp op a b) = "(ABinOp " ++ show op ++ " " ++ a ++ " " ++ b ++ ")"
  show (ACase val cases) = "(ACase " ++ val ++ " { CASES })"
  show (AFuns f l_args args) =
    "(AFuns " ++ f ++ " " ++ show l_args ++ " " ++ show args ++ ")"
  show (AApply f args) = "(AApply " ++ f ++ " " ++ show args ++ ")"

instance Show ANFFunction where
  show (MkAFun name args body) =
    "(MkAFun " ++ name ++ " " ++ show args ++ " " ++ show body ++ ")"

instance Show ANFProgram where
  show (MkAProg e exp) = "(MkAProg " ++ show exp ++ " " ++ show e ++ ")"

genArgNames :: [Expr] -> Name -> [Name]
genArgNames (arg:args) name = let t_a = name ++ "a"
                              in t_a:genArgNames args t_a
genArgNames [] name = []

argsToANF :: Expr -> [Expr] -> Name -> Name -> ANFExpr
argsToANF exp (arg:args) name baseName =
  let t_a = name ++ "a"
  in ALet t_a (exprToANF arg t_a) (argsToANF exp args t_a baseName)
argsToANF (Call (Var f) args) [] name baseName =
  ACall f (genArgNames args baseName)
argsToANF (Con id vals) [] name baseName = ACon id (genArgNames vals baseName)
argsToANF (Funs f args_l args) [] name baseName =
  AFuns f args_l (genArgNames args baseName)
argsToANF (Apply (Funs f f_args args) new_args) [] name baseName =
  AApply baseName (genArgNames (args ++ new_args) baseName)

conToANF :: Expr -> Expr -> Name -> Name -> ANFExpr
conToANF (Con t vals) (Con "Nil" cs) name baseName =
  ACon t (genArgNames (vals) baseName)
conToANF base (Con t (v:cs)) name baseName =
  let t_a = name ++ "a"
  in ALet t_a (exprToANF v t_a) (conToANF base (head cs) t_a baseName)
conToANF (Con t vals) exp name baseName =
  let t_a = name ++ "a"
  in ALet t_a (exprToANF exp t_a) (ACon t (genArgNames vals baseName))

casesToANF :: [CaseAlt] -> Name -> [ACaseAlt]
casesToANF ((IfCon t vs exp):cs) name =
  let t_s = name ++ "s"
  in AIfCon t vs (exprToANF exp t_s):casesToANF cs t_s
casesToANF [] name = []

exprToANF :: Expr -> Name -> ANFExpr
exprToANF (Var x) name = AVar x
exprToANF (Val i) name = AVal i
exprToANF (BinOp op x y) name =
  let t_x = name ++ "l"
      t_y = name ++ "r"
  in ALet
       t_x
       (exprToANF x t_x)
       (ALet t_y (exprToANF y t_y) (ABinOp op t_x t_y))
exprToANF (Call f args) name = let t_c = name ++ "f"
                               in argsToANF (Call f args) args t_c t_c
exprToANF (If p c a) name =
  let t_p = name ++ "i"
      t_c = name ++ "it"
      t_a = name ++ "ie"
  in ALet t_p (exprToANF p t_p) (AIf t_p (exprToANF c t_c) (exprToANF a t_a))
exprToANF (Let x val body) name =
  let t_v = name ++ "lv"
      t_b = name ++ "lb"
  in ALet t_v (exprToANF val t_v) (ALet x (AVar t_v) (exprToANF body t_b))
exprToANF (Con t vals) name = let t_c = name ++ "c"
                              in conToANF (Con t vals) (Con t vals) t_c t_c
exprToANF (Case exp cases) name =
  let t_c = name ++ "C"
  in ALet t_c (exprToANF exp t_c) (ACase t_c (casesToANF cases t_c))
exprToANF (Funs f args_l args) name =
  let t_f = name ++ "f"
  in argsToANF (Funs f args_l args) args t_f t_f
exprToANF (Apply exp vals) name =
  let t_a = name ++ "A"
  in ALet t_a (exprToANF exp t_a) (argsToANF (Apply exp vals) vals t_a t_a)
exprToANF exp name = error ("TODO: anf for " ++ show exp)

-- Convert an input program to ANF, by converting an Expr to an ANFExpr
-- Note that you will need another intermedaite step, converting an Expr
-- to a defunctionalised Expr (that is, an Expr where every function is
-- applied to exactly the right number of arguments)
progToANF :: Program -> ANFProgram
progToANF p = case defun p of
  (MkProg funs exp)
    -> MkAProg { defs = map funToANF funs, mainExpr = exprToANF exp "ANFM" }

funToANF :: Function -> ANFFunction
funToANF (MkFun f args body) = MkAFun f args (exprToANF body "ANFF_")

checkVars :: [Name] -> String
checkVars (n:ns) = "Object " ++ n ++ " = "
checkVars [] = ""

nextJavaLine :: ANFExpr -> [ANFFunction] -> [Name] -> String
nextJavaLine (AIf p c a) fs names = exprToJava (AIf p c a) fs names
nextJavaLine (ALet x val body) fs names = exprToJava (ALet x val body) fs names
nextJavaLine (ACase val cases) fs names = exprToJava (ACase val cases) fs names
nextJavaLine (ACon con vals) fs (name:names) =
  "Con " ++ name ++ " = " ++ exprToJava (ACon con vals) fs names
nextJavaLine exp fs (name:names) =
  "Object " ++ name ++ " = " ++ exprToJava exp fs names
nextJavaLine exp fs [] = "return " ++ exprToJava exp fs []

casesToJava :: [ACaseAlt] -> [ANFFunction] -> [Name] -> Name -> String
casesToJava ((AIfCon "Nil" vs exp):cs) fs names name = "case \"Nil\":\n"
  ++ nextJavaLine exp fs names
  ++ casesToJava cs fs names name
casesToJava ((AIfCon t vs exp):cs) fs names name = "case \""
  ++ t
  ++ "\":\n  Object "
  ++ head vs
  ++ " = ((Con) "
  ++ name
  ++ ").x;\n  Object "
  ++ vs !! 1
  ++ " = ((Con) "
  ++ name
  ++ ").xs;\n"
  ++ nextJavaLine exp fs names
  ++ casesToJava cs fs names name
casesToJava [] fs names name = "default:\n  return null;\n"

conToJava :: Name -> [Name] -> String
conToJava name (v:vs) =
  "new Con(" ++ show name ++ "," ++ v ++ ", " ++ conToJava name vs ++ ")"
conToJava name [] = "new Con ( \"Nil\", null, null)"

exprToJava :: ANFExpr -> [ANFFunction] -> [Name] -> String
exprToJava (AVar x) fs names = x ++ ";\n"
exprToJava (AVal i) fs names = show i ++ ";\n"
exprToJava (ACall f args) fs names =
  case filter (\(MkAFun n _ _) -> f == n) fs of
    ((MkAFun n argNames body):_) -> f ++ "(" ++ intercalate "," args ++ ");\n"
    _ -> "apply ((Fun) "
      ++ f
      ++ ", new Object[]{"
      ++ intercalate "," args
      ++ "});\n"
exprToJava (ABinOp Eq x y) fs names =
  "(" ++ x ++ show Eq ++ y ++ ") ? 1 : 0" ++ ";\n"
exprToJava (ABinOp op x y) fs names =
  "(int) " ++ x ++ " " ++ show op ++ " " ++ "(int) " ++ y ++ ";\n"
exprToJava (ALet name val body) fs names = nextJavaLine val fs (name:names)
  ++ nextJavaLine body fs names
exprToJava (ACon name vs) fs names = conToJava name vs ++ ";\n"
exprToJava (AIf p c a) fs names = "if ((int) "
  ++ p
  ++ " != 0) {\n"
  ++ nextJavaLine c fs names
  ++ "} else {\n"
  ++ nextJavaLine a fs names
  ++ "}\n"
exprToJava (ACase name cases) fs names = "switch(((Con) "
  ++ name
  ++ ").type) {\n"
  ++ casesToJava cases fs names name
  ++ "}\n"
exprToJava (AFuns n l_args args) fs names = "new Fun("
  ++ show n
  ++ ", "
  ++ show l_args
  ++ ", new Object[]{"
  ++ intercalate "," args
  ++ "});\n"
exprToJava (AApply f args) fs names =
  "apply((Fun) " ++ f ++ ", new Object[]{" ++ intercalate "," args ++ "});\n"
exprToJava exp fs names = error ("TODO exprToJAVA for " ++ show exp)

argsToJava :: [Name] -> Int -> String
argsToJava (a:as) i =
  "Object " ++ a ++ " = args[" ++ show i ++ "];\n" ++ argsToJava as (i + 1)
argsToJava [] i = "\n"

funToJava :: ANFFunction -> [ANFFunction] -> String
funToJava (MkAFun fnName argNames definition) fs = "public static Object "
  ++ fnName
  ++ "(Object... args){\n"
  ++ argsToJava argNames 0
  ++ nextJavaLine definition fs []
  ++ "\n}\n"

anfToJava :: ANFProgram -> String -> String
anfToJava (MkAProg defs mainExpr) className =
  "import java.lang.reflect.Method;\n\
  \import java.util.Arrays;\n\
  \import java.util.stream.Stream;\n\
  \class Fun { \n\
    \   Class<?> c = "
  ++ className
  ++ ".class; \n\
    \   String fn; \n\
    \   int fn_args; \n\
    \   Object[] args; \n\n\
    \   Fun(String fn, int fn_args, Object[] args) { \n\
    \     this.fn = fn; \n\
    \     this.fn_args = fn_args; \n\
    \     this.args = args; \n\
    \   } \n\
    \} \n\n\
    \class Con {\n\
    \   String type;\n\
    \   Object x;\n\
    \   Con xs;\n\
    \   public Con(String type, Object x, Con xs) {\n\
    \     this.type = type;\n\
    \     this.x = x;\n\
    \     this.xs = xs;\n\
    \   }\n\
    \}\n\n\
    \public class "
  ++ className
  ++ " {\n"
  ++ "public static Object apply(Fun fun, Object[] args) {\n\
      \   Object[] new_args = Stream.concat(Arrays.stream(fun.args), Arrays.stream(args))\n\
      \                       .toArray();\n\
      \   fun.args = new_args;\n\
      \   Method method;\n\
      \   try {\n\
      \     method = fun.c.getDeclaredMethod(fun.fn, Object[].class);\n\
      \   } catch (Exception e ) {\n\
      \     e.printStackTrace();\n\
      \     return null;\n\
      \   }\n\n\
      \   if (fun.args.length < fun.fn_args) {\n\
      \     return fun;\n\
      \   } else if (fun.args.length > fun.fn_args) {\n\
      \     try { \n\
      \       Object[] extra_args = Arrays.copyOfRange(fun.args, fun.fn_args, fun.args.length);\n\
      \       fun.args = Arrays.copyOfRange(fun.args, 0, fun.fn_args);\n\
      \       return apply((Fun) method.invoke(null, (Object) Arrays.copyOfRange(fun.args, 0, fun.fn_args)), extra_args);\n\
      \     } catch (Exception e) { \n\
      \       System.err.println(\"Apply error: Function called with invalid arguments\");\n\
      \       return null;\n\
      \     }\n\
      \   } else {\n\
      \     try {\n\
      \       return method.invoke(null, (Object) fun.args);\n\
      \     } catch (Exception e) {\n\
      \       System.err.println(e.getMessage());\n\
      \       e.printStackTrace();\n\
      \       return null;\n\
      \     }\n\
      \   }\n\
      \}\n"
  ++ concatMap (`funToJava` defs) defs
  ++ "public static Object mainFunction(){\n"
  ++ nextJavaLine mainExpr defs []
  ++ "}\npublic static void main(String[] args){\nSystem.out.println(mainFunction());\n} \n}\n"

toJava :: Program -> String -> String
toJava p = anfToJava (progToANF p)

-- All being well, this will print out a java program which, when compiled,
-- will print the result of evaluating the expression in testProg1
main :: IO ()
main = putStrLn (toJava testProg3 "Test")

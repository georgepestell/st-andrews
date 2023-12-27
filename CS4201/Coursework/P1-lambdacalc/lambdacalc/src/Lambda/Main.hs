module Lambda.Main where

import           Common
import           Lambda.AST
import           Lambda.Check
import           Lambda.Eval
import           Lambda.Parser
import           Lambda.Printer
import           Lambda.Quote
import           REPL

st :: Interpreter ITerm CTerm Value Type Info Info
st = I { iname = "the simply typed lambda calculus"
       , iprompt = "ST> "
       , iitype = \v c -> iType 0 c
       , iquote = quote0
       , ieval = \e x -> iEval x (e, [])
       , ihastype = HasType
       , icprint = cPrint 0 0
       , itprint = tPrint 0
       , iiparse = parseITerm 0 []
       , isparse = parseStmt []
       , iassume = \s (x, t) -> stassume s x t
       }

stassume state@(out, ve, te) x t = return (out, ve, (Global x, t):te)

repST :: IO ()
repST = readevalprint st ([], [], [])

main :: IO ()
main = repST

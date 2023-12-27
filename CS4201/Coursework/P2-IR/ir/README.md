# Additional Information

This program takes in a defined source language syntax tree, of the form given in the specification and starter code.

It's output is a Java class which, by default, is called `Test`. Therefore, it is advised to pipe the output into a file named `Test.java` for compilation.

This Class name and program syntax tree to conveert can be changed by changing the arguments given to the function `toJava` in the main method of `ANF.hs`.

# Running Instructions

To run this program, just run

```bash
cabal run ir
```

Alternatively, using ghci you can run

```bash
$ cd src

$ ghci IR/ANF.hs

gchci> main
```

# Running tests

To run the full suite of tests, run

```bash
$ cabal test
```

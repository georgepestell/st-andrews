- Student ID: 20007413

# Pre-Requisites

- java-sdk (tested using version 17.0.8.1 on the lab machines)
- maven

# Compilation Instructions

To compile everything, run

```shell
make
```

This will run the test suite, and then compile the java. You can just compile using

```shell
make compile
```

# Running the solver

> Before you can run the solver, you must compile the source code ([see. Compilation Instructions](#compilation-instructions)).

```shell
java -cp ./target/classes com.example.cspsolver.BinarySolver <file.csp>
```

# Running the problem class generators

To run the problem class generators, make sure the project is compiled, then run

```shell
java -cp ./target/classes com.example.generators.<Generator> <options>
```

# Running the tests

To run all the tests in one go, just run

```shell
make test
```

This command is just a wrapper for the maven command

```shell
mvn clean test
```

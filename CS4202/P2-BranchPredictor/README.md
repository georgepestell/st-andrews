# Pre-Requisites

Unzip the branch_raEnsure you have the `traces` directory in the root of the project.

Ensure you have g++ (tested on v.11.4.1), cmake (tested on version 3.20.2), and ninja (tested on version 1.10.2)

# Compilation Instructions

```bash
$ mkdir build && cd build
```

```bash
$ cmake .. -G Ninja
```

To compile the library, run

```bash
$ ninja
```

# Running

## Tests
To run the unit tests, run:

```bash
$ ninja test
```
## Evaluation
To run the empirical evaluation, outputting to stdout CSV results, run:

```bash
$ ninja evaluate
```
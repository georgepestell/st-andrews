- This project uses cmake to generate the makefile used to compile the c++ - code.

# Compilation Instructions

- First, create and cd into the `build` folder in the root of the project by running:

```bash
mkdir build
cd build
```

- Then run cmake using the command

```bash
cmake ..
```

- Then, to compile the executable run either:

```bash
make cacheSim
```

# Running Instructions

- After compiling the `cacheSim` target, you can run the program from the `build` directory

```bash
./cacheSim <config-file> <trace-file>
```

- This will output JSON to stdout, and debug information to stderr

# Packaging Instructions

- To create the .tar file of the necessary source code run

```bash
make cacheSim
```

# para-pat

Parallel library implementing Farm and Pipe skeleton patterns using pthreads. Based on a Node network design philosophy.

## Compilation Instructions

First, make sure to generate the Ninja makefiles. This is done with cmake in the build directory:

```bash
$ mkdir build && cd build
$ cmake .. -G Ninja
```

Then to build everything, run:

```bash
$ ninja
```

Alternatively, to build just the library (without tests), run:

```bash
$ ninja para-pat
```

## Running Tests

To run all tests, execute the following command (will build the library and tests if not already built):

```bash
$ ninja test
```

Alternatively, run the tests directly from the build directory:

```bash
$ ./test_parapat --log_level=test_suite
```

## Running Examples

Examples are built from the build directory, creating an exampples subdirectory containing executables:

```bash
$ ninja example
```

The `convolution` executables require generating images in the `PROJECT_ROOT/examples/images/` folder. Generate these by running:

```bash
$ cd examples/images
$ ./create_inputs <count>
```

Then run the examples from the build directory:

```bash
$ cd build
$ ./examples/convolution
$ ./examples/convolution_parapat_farm
$ ./examples/convolution_parapat_stream
$ ./examples/convolution_parapat_pipe
$ ./examples/convolution_parapat_nest
$ ./examples/convolution_fastFlow_farm
$ ./examples/fib
$ ./examples/fib_parapat_pipe
```

## Documentation

Doxygen was used to generate documentation for this project. This can be reached via the `doxygen/html/index.html` file.
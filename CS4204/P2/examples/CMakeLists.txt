project(examples
    VERSION "0.0.1"
    DESCRIPTION "Example parallel applications"
    LANGUAGES CXX C
)


link_libraries(png)
link_libraries(pthread)
link_libraries(para-pat)

add_executable(fib fib.cpp)
add_executable(fib_parapat_pipe fib_parapat_pipe.cpp)

add_executable(convolution convolution.cpp)
add_executable(convolution_parapat_farm convolution_parapat_farm.cpp)
add_executable(convolution_parapat_stream convolution_parapat_stream.cpp)
add_executable(convolution_parapat_nest convolution_parapat_nest.cpp)
add_executable(convolution_parapat_pipe convolution_parapat_pipe.cpp)
add_executable(convolution_fastFlow_farm convolution_fastFlow_farm.cpp)

add_compile_options(convolution_fastFlow_farm -O3 -finline-functions)

target_include_directories(convolution_fastFlow_farm PUBLIC include/fastflow)

add_custom_target(
  example
  DEPENDS fib fib_parapat_pipe convolution convolution_parapat_farm convolution_parapat_stream convolution_parapat_pipe convolution_parapat_nest convolution_fastFlow_farm
)
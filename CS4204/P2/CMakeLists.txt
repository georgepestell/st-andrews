## GLOBAL

cmake_minimum_required(VERSION 3.20)
set(CMAKE_CXX_STANDARD 17)


### PROJECT

project(ParaPat
    VERSION "0.0.1"
    DESCRIPTION "Parallel pattern library para-pat"
    LANGUAGES CXX
)



add_subdirectory(src)

# target_compile_definitions(para-pat PRIVATE )
# target_compile_options(para-pat PRIVATE )

# set_target_properties(para-pat PROPERTIES RUNTIME_OUTPUT_DIRECTORY "bin")


#
# Compile test suite
#
ADD_LIBRARY(boost_unit_test_framework STATIC IMPORTED)
add_executable(test_parapat
    test/tests.cpp
    test/functions.cpp
)

target_link_libraries(test_parapat PRIVATE
                      ${Boost_FILESYSTEM_LIBRARY}
                      ${Boost_SYSTEM_LIBRARY}
                      ${Boost_UNIT_TEST_FRAMEWORK_LIBRARY}
                      )
add_test(test_parapat test_parapat)
target_link_libraries(test_parapat para-pat)
# make "make test" run the test program
add_custom_target(test
     DEPENDS test_parapat para-pat
     COMMAND ./test_parapat --log_level=success
)

include_directories(test_parapat src/)

#
# Parse examples
#
add_subdirectory(examples)
include_directories(convolutioncpp src)

add_custom_target(documentation
    COMMAND cd ${PROJECT_SOURCE_DIR} && doxygen ./doxygen.conf
)

add_custom_target(createTar
    DEPENDS para-pat
    DEPENDS documentation
    COMMAND rm ${PROJECT_SOURCE_DIR}/examples/images/image[1-9]*.png -f
    COMMAND cd ${PROJECT_SOURCE_DIR} && tar -cvf CS4204_P2_200007413.tar examples/ src/ test/ doxygen/ CMakeLists.txt README.md report.pdf
)
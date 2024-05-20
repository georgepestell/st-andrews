cmake_minimum_required(VERSION 3.1)

project(branchPredictor LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

add_subdirectory(src)
add_subdirectory(test)

add_executable(branchPredictor src/main.cpp)

target_include_directories(branchPredictor PUBLIC "src/")
target_link_libraries(branchPredictor BranchPredictor)

target_include_directories(BranchPredictor PUBLIC "src/")
target_include_directories(testBranchPredictor PUBLIC "src/")
target_include_directories(evaluator PUBLIC "src/")

add_test(testBranchPredictor testBranchPredictor)


add_custom_target(createTar 
  COMMAND cd ${PROJECT_SOURCE_DIR} && zip -r CS4202_P2_200007413.zip src/ test/ README.md CMakeLists.txt report.pdf)
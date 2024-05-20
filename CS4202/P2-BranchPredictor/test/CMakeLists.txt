#find_package(Boost COMPONENTS system unit_test_framework filesystem REQUIRED)

add_executable(testBranchPredictor test.cpp)
target_link_libraries(testBranchPredictor PRIVATE
                      ${Boost_FILESYSTEM_LIBRARY}
                      ${Boost_SYSTEM_LIBRARY}
                      ${Boost_UNIT_TEST_FRAMEWORK_LIBRARY}
                      )
target_link_libraries(testBranchPredictor PRIVATE BranchPredictor)

add_executable(evaluator eval.cpp)
target_link_libraries(evaluator PRIVATE BranchPredictor)


add_custom_target(test ALL
  DEPENDS testBranchPredictor
  COMMAND BRANCHPRED_TRACE_DIR=${PROJECT_SOURCE_DIR}/traces/ ${PROJECT_BINARY_DIR}/test/testBranchPredictor --log_level=test_suite
)

set(EVAL_OUT "output.csv")
add_custom_target(evaluate
  DEPENDS evaluator
  COMMAND ${PROJECT_BINARY_DIR}/test/evaluator ${PROJECT_SOURCE_DIR}/traces > ${PROJECT_BINARY_DIR}/${EVAL_OUT}
)
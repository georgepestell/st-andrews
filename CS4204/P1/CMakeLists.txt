cmake_minimum_required(VERSION 3.2)
project(main LANGUAGES CXX)

set(CMAKE_MAKE_PROGRAM "Unix Makefiles")

add_subdirectory(test)

add_custom_target(createTar ALL COMMAND
    ${CMAKE_COMMAND} -E tar "cvf" "P1-200007413.tar" src test .vscode README.md report.pdf
    WORKING_DIRECTORY ${CMAKE_SOURCE_DIR}
    )
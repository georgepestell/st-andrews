cmake_minimum_required(VERSION 3.1)

project(cacheSim LANGUAGES CXX)

set(CMAKE_GENERATOR "Unix Makefiles")
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

SET(IS_MAIN (${CMAKE_SOURCE_DIR} STREQUAL ${CMAKE_CURRENT_SOURCE_DIR}))

# Include(FetchContent)

# FetchContent_Declare(
#  Catch2
#  GIT_REPOSITORY https://github.com/catchorg/Catch2.git
#  GIT_TAG        v3.4.0 # or a later release
#)

# FetchContent_MakeAvailable(Catch2)
# include(CTest)
# include(Catch)


if (WIN32)
    add_compile_definitions(WINDOWS=1)
endif ()

if (UNIX AND NOT APPLE)
    add_compile_definitions(LINUX=1)
endif()

if (${CMAKE_BUILD_TYPE} MATCHES Debug)
    add_compile_definitions(_DEBUG=1)
else ()
    add_compile_definitions(NDEBUG=1)
endif (${CMAKE_BUILD_TYPE} MATCHES Debug)

add_subdirectory(CacheSim)

# if (IS_MAIN)
    # add_subdirectory(test)

    # include(InstallRequiredSystemLibraries)
    # set(CPACK_RESOURCE_FILE_LICENSE "${CMAKE_CURRENT_SOURCE_DIR}/README.md")
    # set(CPACK_PACKAGE_VERSION_MAJOR "1")
    # set(CPACK_PACKAGE_VERSION_MINOR "0")
    # include(CPack)
# endif()

add_executable(cacheSim ./main.cpp)
target_link_libraries(cacheSim PRIVATE nlohmann_json::nlohmann_json)
target_link_libraries(cacheSim PRIVATE CacheSimLib)

add_custom_target(createTar ALL COMMAND
    ${CMAKE_COMMAND} -E tar "cvf" "P1-CacheSim-200007413.tar" CacheSim CMakeLists.txt main.cpp README.md
    WORKING_DIRECTORY ${CMAKE_SOURCE_DIR}
    )
cmake_minimum_required(VERSION 3.1)
project(CacheSimLib LANGUAGES CXX)

add_library(CacheSimLib SHARED cache.cpp cacheParser.cpp cacheSim.cpp lfuCache.cpp rrCache.cpp lruCache.cpp)
target_include_directories(CacheSimLib PRIVATE "${PROJECT_SOURCE_DIR}")

include(FetchContent)
FetchContent_Declare(
    json 
    GIT_REPOSITORY  https://github.com/nlohmann/json.git
    GIT_TAG         v3.11.3
)
FetchContent_MakeAvailable(json)

target_link_libraries(CacheSimLib PRIVATE nlohmann_json::nlohmann_json)
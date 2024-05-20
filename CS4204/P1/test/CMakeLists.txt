project(test LANGUAGES CXX)

add_executable(testAtomic tests.cpp)
add_executable(testSingleLock tests.cpp)
add_executable(testMultiLock tests.cpp)
add_executable(testLockFree tests.cpp)

target_include_directories(testAtomic PUBLIC ../src/Atomic)
target_include_directories(testSingleLock PUBLIC ../src/SingleLock)
target_include_directories(testMultiLock PUBLIC ../src/MultiLock)
target_include_directories(testLockFree PUBLIC ../src/LockFree)
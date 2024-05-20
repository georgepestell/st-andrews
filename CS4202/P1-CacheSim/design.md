# Design Decisions

- sequential trace reading to prevent memory overload
- enum for kind and policy
- doubly-linked-list cache hierarchy
- caches call eachother instead of the simulator looping through list
- CPUs limiting size use for tag and offset is essential so used dynamic size allocated char arrays using only the size required for each cache line
- the cache metadata itself wouldn't need to be stored on the cache, and so size less important
- ceil required for sizes as it requires at least that number of bits to store so rounding down would mean not all can be addressed
- used JSON library https://github.com/nlohmann/json
- used long for size (and line size) for better coverage of max sizes achieved by 64-bit addresses
- calculating index after fully-associative to prevent redundant calculation
- removed cache kind, because the set count generalises to direct-mapped with 1 set, n-way set associative, and fully-associative with line_count sets

# Testing

- using the Catch2 testing lib
- integration with cmake's ctest tool

# Ideas

- hashmap for cache storage ?

# Reading

- [cache design lecture](https://courses.cs.washington.edu/courses/cse378/09wi/lectures/lec16.pdf)

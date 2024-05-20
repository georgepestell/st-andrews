#ifndef CACHE_H_
#define CACHE_H_

#include <algorithm>
#include <cmath>
#include <cstdint>
#include <cstdlib>
#include <exception>
#include <iostream>
#include <stdexcept>
#include <sys/types.h>
#include <tgmath.h>

using namespace std;

// 64-bit addresses
const int MEMORY_ADDRESS_BITS = 64;

/* Cache Kind
 * - DIRECT = direct-mapped
 * - FULL = fully-associative
 * - N_WAY= set-associative
 */
enum CacheKind { DIRECT, FULL, TWO_WAY, FOUR_WAY, EIGHT_WAY };

class CacheLine {
public:
  bool valid = false;
  uint64_t tag;
};

/**
 * Represents a Cache Level. Allows hierarchies to be created arbitrarily.
 */
class Cache {
public:
  // Cache size settings (Bytes)
  uint64_t total_size;
  unsigned int line_size;

  // Number of directly addressable lines/sets
  // if set_count=line_count, then the cache is direct-mapped
  // if set_count=1, then the cache is fully-associative
  uint64_t set_count;
  uint64_t set_size;

  // Cache metadata calculated on creation
  unsigned long line_count;
  uint64_t index_bits;
  uint64_t offset_bits;

  // Masks for extracting offset and tags from memory addresses
  uint64_t offset_mask;
  uint64_t index_mask;

  // Actual cache store data-structure
  CacheLine *store;

  // Linked list referenceto next node
  Cache *next = NULL;

  // Arbitrary name of cache
  string name;

  // Performance metrics
  uint64_t misses;
  uint64_t hits;

  /**
   * Constructor generating the cache data structure
   * @arg prev The
   */
  Cache(string name, uint64_t line_count, uint line_size, uint64_t set_size);

  void call(uint64_t m_addr, ushort size);

  uint64_t getIndex(uint64_t addr);
  uint64_t getTagIndex(uint64_t addr);
  uint64_t getTag(uint64_t addr);
  uint64_t getAddress(uint64_t tag, uint64_t index);

  // Create a completely virtual replacement function.
  // Means the child replace function will always be called
  virtual void replace(uint64_t index, uint64_t tag) = 0;

  /**
   * Validates that a given cache line is valid and the tag matches
   * @returns HIT if valid and matches given tag, MISS otherwise
   */
  bool tryHit(uint64_t line, uint64_t tag);

  virtual void registerHit(uint64_t line);
  virtual void registerMiss();

  virtual void cleanup();
};

#endif /* CACHE_H_ */
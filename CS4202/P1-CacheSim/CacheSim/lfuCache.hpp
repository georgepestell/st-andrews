#ifndef LRUCACHE_H_
#define LRUCACHE_H_
#include "./cache.hpp"

class LFUCache : public Cache {
private:
  uint *useCount;

public:
  /**
   * Constructor generating the cache data structure
   * @arg prev The
   */
  LFUCache(string name, uint64_t line_count, unsigned int line_size,
           uint64_t set_size);

  void replace(uint64_t index, uint64_t tag);

  void registerHit(uint64_t line);

  void cleanup();
};

#endif // LRUCACHE_H_
#ifndef LFUCACHE_H_
#define LFUCACHE_H_
#include "./cache.hpp"

class LRUCache : public Cache {
private:
  uint64_t *lastAccess;
  uint64_t age;

public:
  /**
   * Constructor generating the cache data structure
   * @arg prev The
   */
  LRUCache(string name, uint64_t line_count, unsigned int line_size,
           uint64_t set_size);

  void replace(uint64_t index, uint64_t tag);
  void registerHit(uint64_t line);

  void cleanup();
};

#endif // LFUCACHE_H_
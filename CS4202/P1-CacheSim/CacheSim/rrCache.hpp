#ifndef RRCACHE_H_
#define RRCACHE_H_
#include "./cache.hpp"

class RRCache : public Cache {
public:
  void replace(uint64_t index, uint64_t tag);

  /**
   * Constructor generating the cache data structure
   * @arg prev The
   */
  RRCache(string name, uint64_t line_count, unsigned int line_size,
          uint64_t set_size);

  void cleanup();

private:
  uint *pos;
};

#endif // RRCACHE_H_
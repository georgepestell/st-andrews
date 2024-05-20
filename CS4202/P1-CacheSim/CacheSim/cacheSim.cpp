#include "./cacheSim.hpp"
#include <stdexcept>

CacheSim::CacheSim(string config_path) {
  this->hierarchy = CacheParser::parse(config_path);
}

void CacheSim::simulate(string trace_path) {

  // Try to open the trace file
  ifstream traceFile(trace_path);

  if (!traceFile) {
    cerr << "Error: Failed to open trace file \"" << trace_path << "\"\n";
    exit(EXIT_FAILURE);
  }

  try {

    // Setup trace line variables to extract
    uint64_t m_addr;
    ushort size;
    uint64_t p_addr;
    char op;

    // Read trace lines sequentially to prevent loading huge files into memory
    while (traceFile >> hex >> p_addr >> m_addr >> dec >> op >> size) {
      this->call(this->hierarchy, m_addr, size);
    }

    // Cleanup the trace file
    traceFile.close();

  } catch (exception e) {
    // Catch errors, and close the trace file before re-throwing
    traceFile.close();
    throw e;
  }
}

void CacheSim::call(Cache *cache, uint64_t m_addr, ushort size) {
  if (cache == NULL) {
    main_memory_accesses++;
    return;
  }

  /**
   * Work out the number of lines to call from the main cache,
   * this means we need the index and tag (as lines wrap around caches)
   *
   * Decrement the end_addr as size includes 0th line referenced
   */
  uint64_t start_addr = cache->getTagIndex(m_addr);
  uint64_t end_addr = cache->getTagIndex(m_addr + size - 1);

  // For each memory line, separate the tag and index and call the cache
  for (uint64_t i = start_addr; i <= end_addr; i++) {

    uint64_t index = i & cache->index_mask;
    uint64_t tag = i >> cache->index_bits;

    // Try accessing thee cache
    // Or fetch line on miss.
    if (!cache->tryHit(index, tag)) {
      // Simulate fetchiing from next cache/memory and updating the cache line
      cache->replace(index, tag);
      call(cache->next, i << cache->offset_bits, cache->line_size);
    }
  }
}

void CacheSim::printJSON(CacheSim *sim) {
  json out = json::object();

  out.emplace("caches", json::array());
  Cache *cache = sim->hierarchy;
  while (cache != NULL) {
    json cacheObj = json::object();
    cacheObj.emplace("name", cache->name);
    cacheObj.emplace("hits", cache->hits);
    cacheObj.emplace("misses", cache->misses);
    out["caches"].push_back(cacheObj);
    cache = cache->next;
  }

  out.emplace("main_memory_accesses", sim->main_memory_accesses);
  cout << out.dump(2) << endl;
}

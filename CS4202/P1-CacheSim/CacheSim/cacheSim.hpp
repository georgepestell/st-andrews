#ifndef CACHESIM_H_
#define CACHESIM_H_

#include <cstdlib>
#include <fstream>
#include <iostream>

#include "./cache.hpp"
#include "./cacheParser.hpp"

#include <nlohmann/json.hpp>

using namespace std;

/**
 * Constructs a caching hierarchy from a supplied config file. Then allows
 * simulating caching operations read from a trace file.
 */
class CacheSim {
public:
  // We are using a linked list data structure as we only ever access cache
  // levels sequentially
  Cache *hierarchy;

  uint64_t main_memory_accesses;

  // Constructor generating simulator cache hierarchy
  CacheSim(string config_path);

  /**
   * Read trace file line by line, simulating caching
   */
  void simulate(string trace_path);
  /**
   * Passses calls to the relevant caches, running from the top of the cache
   * hierarchy, missing down, eventually simulating a main memory acccess.
   */
  void call(Cache *cache, uint64_t m_addr, ushort size);

  /**
   * Prints simulator information as JSON to stdout
   */
  static void printJSON(CacheSim *sim);
};

#endif // CACHESIM_H_
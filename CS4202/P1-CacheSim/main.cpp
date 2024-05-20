#include "./CacheSim/cacheSim.hpp"

/**
 * Command line interface for simulating cache operations from a trace file on a
 * given cache hierarchy configuration.
 */
int main(int argc, char **argv) {

  // Check config and trace filepaths given
  if (argc != 3) {
    cout << "Usage: ./cacheSim <cache-config> <trace-file>\n";
    return 1;
  }

  // Setup the cache simulator with the config supplied
  CacheSim *sim = new CacheSim(argv[1]);

  // Simulate the trace file supplied
  sim->simulate(argv[2]);

  CacheSim::printJSON(sim);

  return 0;
}
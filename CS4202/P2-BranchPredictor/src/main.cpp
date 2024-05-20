#include <cstdlib>
#include <iostream>
#include <stdexcept>
#include <string>

#include <BranchPredictor/AlwaysTruePredictor.hpp>
#include <BranchPredictor/GSharePredictor.hpp>
#include <BranchPredictor/Predictor.hpp>
#include <BranchPredictor/Simulator.hpp>
#include <BranchPredictor/TwoBitPredictor.hpp>

const long DEFAULT_TABLE_SIZE = 512;

int main(int argc, char **argv) {

  // Ensure enough arguments are given
  if (argc < 3 || argc > 4) {
    fprintf(
        stderr,
        "Usage: %s <trace_path> <type> [table_size]\n - type = "
        "alwaystrue|twobit|gshare>\n - tableSize = twobit/gshare table size\n",
        argv[0]);
    return 1;
  }

  uint64_t tableSize = DEFAULT_TABLE_SIZE;

  // Fetch arguments
  string tracefile = argv[1];
  string type = argv[2];

  if (argc == 4) {
    tableSize = atoi(argv[3]);
  }

  Predictor *predictor;

  if (type == "alwaystrue") {
    predictor = new AlwaysTruePredictor();
  } else if (type == "twobit") {
    predictor = new TwoBitPredictor(tableSize);
  } else if (type == "gshare") {
    predictor = new GSharePredictor(tableSize);
  } else {
    fprintf(stderr,
            "Usage: %s <trace_path> <type> [table_size]\n - type = "
            "alwaystrue|twobit|gshare>\n - tableSize = twobit/gshare table "
            "size (default %ld)\n",
            argv[0], DEFAULT_TABLE_SIZE);
    return 1;
  }

  Simulator sim(predictor, tracefile);

  sim.run();
  sim.printCSVResults();
}
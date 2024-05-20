#include <filesystem>
#include <string>

#include <BranchPredictor/AlwaysTruePredictor.hpp>
#include <BranchPredictor/GSharePredictor.hpp>
#include <BranchPredictor/Simulator.hpp>
#include <BranchPredictor/TwoBitPredictor.hpp>

using namespace std;
using namespace CS4202_P2;

const vector<uint> TABLE_SIZES{512, 1024, 2048, 4096};

const vector<string> TRACE_FILES{
    "bwaves.out", "cactusbssn.out", "exchange2.out", "gcc.out",
    "leela.out",  "povray.out",     "wrf.out",       "xz.out"};

const vector<ulong> PROFILE_COUNTS{0, 100, 500, 1000, 2000, 5000, 10000, 20000};

int main(int argc, char **argv) {

  if (argc != 2) {
    fprintf(stderr, "Usage: %s <trace_dir>\n", argv[0]);
    return 1;
  }

  const string TRACE_DIR = argv[1];

  Simulator *sim;
  for (string file : TRACE_FILES) {
    string filepath = TRACE_DIR + "/" + file;

    cerr << filepath << endl;

    // Run the baseline always true predictor
    sim = new Simulator(new AlwaysTruePredictor(), filepath);
    sim->run();
    sim->printCSVResults();
    delete sim;

    for (uint tableSize : TABLE_SIZES) {
      for (ulong profileCount : PROFILE_COUNTS) {

        // Run the two-bit predictor
        sim = new Simulator(new TwoBitPredictor(tableSize), filepath,
                            profileCount);
        sim->run();
        sim->printCSVResults();
        delete sim;

        // Run the gshare predictor
        sim = new Simulator(new GSharePredictor(tableSize), filepath,
                            profileCount);
        sim->run();
        sim->printCSVResults();
        delete sim;
      }
    }
  }
}
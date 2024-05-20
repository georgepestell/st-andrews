#include <BranchPredictor/Predictor.hpp>
#include <BranchPredictor/TraceReader.hpp>

#include <chrono>
#include <ostream>

namespace CS4202_P2 {
class Simulator {
private:
  TraceReader *reader;  /** Reads the trace file given */
  Predictor *predictor; /** The predictor algorithm to use */

  ulong profileTraces =
      0; /** The number of traces left to use in profiling mode */

  ulong total = 0;   /** Total traces predicted */
  ulong correct = 0; /** Total correct trace predictions */

  double time_taken = 0; /** Time taken in seconds */

public:
  /**
   * @brief Construct a new Simulator with profiling
   *
   * @param predictor Prediction algorithm instance
   * @param traceFile Trace filepath
   * @param profileTraces Numer of traces to count as "profiling"
   */
  Simulator(Predictor *predictor, string traceFile, ulong profileTraces) {
    this->reader = new TraceReader(traceFile);
    this->predictor = predictor;
    this->profileTraces = profileTraces;
  }

  /**
   * @brief Construct a new Simulator with no profiling
   *
   * @param predictor Prediction algorithm instance
   * @param traceFile Trace filepath
   */
  Simulator(Predictor *predictor, string traceFile)
      : Simulator(predictor, traceFile, 0) {}

  /**
   * @brief Reads entries from the trace file. Makes predictions using the
   * predictor. Then checks the predictions against ground truth.
   *
   */
  void run();

  /**
   * @brief Prints human readable results from run().
   *
   */
  void printResults();

  /**
   * @brief Prints CSV results from run()
   *
   */
  void printCSVResults();
};
} // namespace CS4202_P2
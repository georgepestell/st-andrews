#pragma once

#include "BranchPredictor/TraceReader.hpp"
#include <BranchPredictor/Trace.hpp>

#include <BranchPredictor/Predictor.hpp>

#include <cstdio>

namespace CS4202_P2 {

/**
 * @brief Always Taken Branch Predictor. Just predicts true indepenent of
 * trace/history.
 *
 */
class AlwaysTruePredictor : public Predictor {
public:
  /**
   * @brief Makes a prediction on the next trace file line (always predicts
   * taken)
   *
   * @return true Predict branch taken (ALWAYS RETURNED)
   */
  bool predict(Trace trace);

  /**
   * @brief Fetches the details of the predictor in CSV format; CLASS, "". The
   * "" represents the empty tableSize column.
   *
   * @return string
   */
  string getDetails();
};
} // namespace CS4202_P2
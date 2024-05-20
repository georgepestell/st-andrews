#pragma once

#include <BranchPredictor/TraceReader.hpp>

#include <cstdio>
#include <sstream>

using namespace std;

namespace CS4202_P2 {
class Predictor {
public:
  /**
   * @brief Predict whether a branch is taken
   *
   * @param trace The branch trace to predict
   * @return true Predict branch TAKEN
   * @return false Predict branch NOT TAKEN
   */
  virtual bool predict(Trace trace) = 0;

  /**
   * @brief Get the Predictor details in CSV format
   *
   * @return string containing the Predictor type (class)
   */
  virtual string getDetails();
};
} // namespace CS4202_P2
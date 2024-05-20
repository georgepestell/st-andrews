#include <BranchPredictor/AlwaysTruePredictor.hpp>

using namespace CS4202_P2;

bool AlwaysTruePredictor::predict(Trace trace) {
  // We successfully predicted
  return true;
}

string AlwaysTruePredictor::getDetails() {
  string baseDetails = Predictor::getDetails();
  ostringstream details;

  // Add a null value to the tableSize column
  details << baseDetails.c_str() << ", \"\"";

  return details.str();
}
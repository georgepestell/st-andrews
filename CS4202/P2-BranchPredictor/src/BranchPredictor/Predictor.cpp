#include <BranchPredictor/Predictor.hpp>

using namespace CS4202_P2;

#include <cxxabi.h>

string Predictor::getDetails() {
  ostringstream details;

  // Add the class name (requires demangling object typeid)
  int status;
  details << abi::__cxa_demangle(typeid(*this).name(), 0, 0, &status);

  return details.str();
}
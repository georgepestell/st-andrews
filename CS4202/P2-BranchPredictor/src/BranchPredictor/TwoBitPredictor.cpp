#include <BranchPredictor/TwoBitPredictor.hpp>

using namespace CS4202_P2;

TwoBitPredictor::TwoBitPredictor(uint64_t tableSize) {
  // Set the predictor attributes
  this->tableSize = tableSize;
  this->indexMask = tableSize - 1;

  // Initialize the BP table to default value
  bitTable = (bitset<2> *)calloc(tableSize, sizeof(bitset<2>));
  for (int i = 0; i < this->tableSize; i++) {
    this->bitTable[i] = bitset<2>(DEFAULT_BITVALUE);
  }
}

void TwoBitPredictor::updateHistory(uint64_t index, bool isTaken) {
  this->bitTable[index].set(0, isTaken);
}

bool TwoBitPredictor::predict(Trace trace) {

  // Get the table index
  uint64_t index = getIndex(trace.p_addr);

  // Get the current prediction (most significant bit)
  bool prediction = this->bitTable[index].test(1);

  // Update prediction if this and previous were wrong
  if (prediction != trace.isTaken &&
      prediction != this->bitTable[index].test(0)) {
    this->bitTable[index].set(1, !prediction);
  }

  // Update prediction history
  this->updateHistory(index, trace.isTaken);

  /*
   * Return the prediction before update as we were predicting before it was
   * taken
   */
  return prediction;
}

uint64_t TwoBitPredictor::getIndex(uint64_t addr) {
  // Returns the least significant bit index
  return addr & indexMask;
}

string TwoBitPredictor::getDetails() {
  string baseDetails = Predictor::getDetails();
  ostringstream details;

  details << baseDetails.c_str() << ", " << this->tableSize;

  return details.str();
}
#include "BranchPredictor/TwoBitPredictor.hpp"
#include <BranchPredictor/GSharePredictor.hpp>
#include <sstream>

uint64_t GSharePredictor::getIndex(uint64_t addr) {
  uint64_t addr_masked = addr >> (64 - (uint64_t)log2(this->indexMask + 1));
  // uint64_t addr_masked = TwoBitPredictor::getIndex(addr);

  // XOR the first bits of the address with the global history
  return addr_masked ^ this->globalHistory;
}

void GSharePredictor::updateHistory(uint64_t index, bool isTaken) {

  // Update the bitTable history
  TwoBitPredictor::updateHistory(index, isTaken);

  // Update the global history
  this->globalHistory <<= 1;
  this->globalHistory += isTaken;
  this->globalHistory &= this->indexMask;
}
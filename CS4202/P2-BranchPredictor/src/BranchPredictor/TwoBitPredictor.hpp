#pragma once

#include <bitset>
#include <cmath>
#include <iostream>

#include "BranchPredictor/TraceReader.hpp"
#include <BranchPredictor/Predictor.hpp>
#include <BranchPredictor/Trace.hpp>

namespace CS4202_P2 {

/**
 * @brief 2-Bit Branch Predictor. Stores local histories of branches in a
 * bitTable which are indexed using the branch address.
 *
 */
class TwoBitPredictor : public Predictor {
protected:
  /**
   * @brief Bit history table. Stores the current prediction, and previous
   * prediction accuracy. Indexed by the least significant program counter
   * (p_addr) bits. Values 0 or 1 = guess false. 2 or 3 = guess true
   */
  bitset<2> *bitTable;
  uint64_t tableSize; /** The size of the bitTable*/
  uint64_t indexMask; /** Mask on the p_addr to index the table */

  /**
   * @brief Default bit table entry value. Set to a low-confidence true
   * prediction (bin) 01 = (dec) 2.
   *
   */
  static const char DEFAULT_BITVALUE = 0b10;

  /**
   * @brief Converts a memory address to a bitTable index
   *
   * @param addr Memory address
   * @return uint64_t Index to the bitTable
   */
  virtual uint64_t getIndex(uint64_t addr);

  /**
   * @brief Updates the history with a given trace
   *
   */
  virtual void updateHistory(uint64_t index, bool isTaken);

public:
  TwoBitPredictor(uint64_t tableSize);

  /**
   * @brief Makes a prediction for whether the branch referred to by the trace
   * was taken / not taken.
   *
   * @param trace Trace instance of a branch call
   * @return true Predict taken
   * @return false Predict not taken
   */
  virtual bool predict(Trace trace);

  /**
   * @brief Get the detaisls of the predictor in CSV format.
   *
   * @return string "CS4202::TwoBitPredictor, tableSize"
   */
  string getDetails();
};
} // namespace CS4202_P2
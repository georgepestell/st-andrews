#include <BranchPredictor/Predictor.hpp>
#include <BranchPredictor/Trace.hpp>
#include <BranchPredictor/TwoBitPredictor.hpp>

#include <map>
#include <sstream>
#include <vector>

using namespace std;
using namespace CS4202_P2;

namespace CS4202_P2 {
/**
 * @brief Predictor using the gshare algrithm. Subclass of TwoBitPredictor,
 * using a different indexing method - XORs a global history and the branch
 * address.
 *
 */
class GSharePredictor : public TwoBitPredictor {
protected:
  uint64_t globalHistory = 0; /** Keeps track of previous predictions */

  /**
   * @brief Updates the global history, and relevant bit-predictor bits
   *
   * @param index The predictor array index to update
   * @param isTaken Whether the latest branch trace was taken
   */
  void updateHistory(uint64_t index, bool isTaken);

  /**
   * @brief Combines the program address and global history to index the
   * predictor vector
   *
   * @param addr Branch program counter address
   * @return uint64_t Index to the predictor table/vector
   */
  uint64_t getIndex(uint64_t addr);

public:
  using TwoBitPredictor::TwoBitPredictor;
};
} // namespace CS4202_P2
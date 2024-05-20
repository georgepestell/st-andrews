#pragma once

#include <fstream>
#include <istream>
#include <stdexcept>

#include <BranchPredictor/Trace.hpp>

using namespace std;

namespace CS4202_P2 {

class EOFException : public exception {};

/**
 * @brief Opens a trace file, and allows fetching lines
 *
 */
class TraceReader {
private:
  string filepath;       /** The filepath to the trace file*/
  ifstream *inputStream; /** Input stream setup on initialization */
public:
  /**
   * @brief Construct a new Trace Reader object, opening a given trace file
   *
   * @param filepath Trace file to read
   */
  TraceReader(string filepath);

  /**
   * @brief Fetches the next line from the trace file
   *
   * @return char*
   */
  Trace readLine();

  /**
   * @brief Get the trace file name
   *
   * @return string The trace filepath
   */
  string getFilepath();

  /**
   * @brief Close the trace file if open
   *
   */
  void close();
};

} // namespace CS4202_P2
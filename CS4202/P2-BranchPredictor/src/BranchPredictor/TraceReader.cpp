#include "TraceReader.hpp"
#include <cstddef>
#include <cstring>

// DEBUG
#include <iostream>

using namespace CS4202_P2;

TraceReader::TraceReader(string filepath) {
  inputStream = new ifstream(filepath);

  // Make sure the file opened successfully
  if (inputStream->fail()) {
    throw runtime_error("Trace file failed open: " + filepath);
  }

  this->filepath = filepath;
}

Trace TraceReader::readLine() {
  // Make sure the file is open
  if (this->inputStream->is_open()) {

    // Each line is 42 characters long (including new line)
    char line[42];

    // Fetch the line in one go (more performant than fetching values
    // individually)
    if (this->inputStream->read((char *)&line, sizeof(line)).eof()) {
      throw EOFException();
    }

    // Parse the line into a Trace object
    Trace trace;

    char p_addr[17];

    strncpy(p_addr, line, 16);
    p_addr[16] = '\0';

    // After performance comparisons, this is the faster than stringstream for
    // converting hex string to int
    trace.p_addr = (uint64_t)strtol(p_addr, NULL, 16);

    // We can ignore t_addr as it's not required
    // strncpy(trace.t_addr, line + 17, 16);
    // trace.t_addr[16] = '\0';

    // trace.branchKind = line[34];

    // Get boolean values
    // trace.isDirect = (line[36] == '1');
    // trace.isConditional = (line[38] == '1');
    trace.isTaken = (line[40] == '1');

    // cerr << trace.toString() << endl;

    return trace;
  } else {
    throw runtime_error("Trace file not open");
  }
}

void TraceReader::close() {
  if (this->inputStream->is_open()) {
    this->inputStream->close();
  }
}

string TraceReader::getFilepath() { return this->filepath; }
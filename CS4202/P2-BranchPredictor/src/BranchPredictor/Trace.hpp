
#pragma once

#include <cstdio>
#include <cstring>
#include <iostream>
#include <string>

using namespace std;

namespace CS4202_P2 {
class Trace {
public:
  uint64_t p_addr; /** Program counter address */
  // NOT REQUIRED: char t_addr[17]; /** Target address */

  // NOT NEEDED char branchKind; /** b = branch, c = call, r = return */

  // NOT NEEDED bool isDirect;
  // NOT NEEDED bool isConditional;
  bool isTaken;

  char *toString() {
    char out[41];
    // Out the p_addr as hex with 16 characters as per the trace files
    sprintf(out, "%016lx %d", p_addr, isTaken);
    return strdup(out);
  }
};
} // namespace CS4202_P2
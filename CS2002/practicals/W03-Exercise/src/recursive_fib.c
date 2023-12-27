#include "fib.h"

// Recursively calculates the nth child of a weighted fibonacci sequence
int fibcalc(int n) {
  // Check for an invalid negative input
  if (n < 0) {
    return -1;
  }

  // Base cases - return 0 for n=0, and 1 for n=1
  if (n <= 1) {
    return n;
  }

  // Calculate the previous fib values needed
  int prevprev = fibcalc(n-2);
  int prev = fibcalc(n-1);

  // Check for overflow and return error
  if (prev < 0) {
    return -1;
  }

  // Calculate and return the weighted fibonacci value
  return (2 * prevprev) + prev;
}

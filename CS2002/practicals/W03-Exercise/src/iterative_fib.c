#include "fib.h"

// Iteratively finds the nth child of a weighted fibonacci sequence
int fibcalc(int n) { 
  // Check for the initial 0 at the beginning of the sequence
  if (n == 0) {
    return n;
  }

  // Set the initial sequence variables for the iterative loop
  int prev = 0;
  int curr = 1;

  // Iterate over the sequence, calculating the current value
  for (int i = 1; i < n; i++) {
    int tmp = curr;
    curr = 2 * prev + curr;
    prev = tmp;

    // Check for overflow and return error
    if (curr < 0) {
      return -1;
    }

  }

  // Return the current sequence value
  return curr;

}

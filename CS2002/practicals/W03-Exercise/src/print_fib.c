#include <stdio.h>
#include <stdlib.h>
#include "fib.h"

// Takes user input and prints out that number of elements from a weighted
// fibonacci sequence
void print_fib() {

  // Number of sequence elements to print
  int n = 0;

  // Get user input and overwrite n
  printf("Length? ");
  scanf("%d", &n);
  
  // Check for negative invalid inputs
  if (n < 0) {
    printf("Invalid input\n");
    exit(1);
  }

  // Print out the array of values for the first n values of the weighted
  // fibonacci sequence
  
  printf("[");
  for (int i = 0; i < n; i++) {
    
    // Print list separators
    if (i != 0) {
      printf(", ");  
    }

    // Calculate the fibonacci value at i, beginning from 0 to n-1
    int fib = fibcalc(i);

    // Check and notify if overflow occurs and exit
    if (fib < 0) {
      printf("Overflow\n");
      exit(1);
    }

    // Print the valid fib value
    printf("%i", fib);

  }

  printf("]\n");
}

#include <stdio.h>
#include <stdlib.h>
#include <sys/signal.h>
#include <sys/wait.h>
#include <unistd.h>

/** GIVEN AS ASSUMED **/
int computePartialSum(int *array, int low, int high) {
  int sum = 0;
  for (int i = low; i < high; i++) {
    sum += array[i];
  }
  return sum;
}

// Code not complete, but calculates each separate part in a process,
// I just had trouble merging the two back together

int computeSum(int array[], int size) {

  int top_cpid = 0;
  int bottom_cpid = 0;

  // Get the parent pid
  int ppid = getpid();

  // Set the value of sum to 0
  int sum = 0;

  // Fork the program to calculate the top half
  top_cpid = fork();

  // Fork to calculate the bottom half
  if (top_cpid != 0) {
    bottom_cpid = fork();
  }

  // The bottom half process will know the top_cpid
  if (top_cpid != 0 && bottom_cpid == 0) {
    // Bottom calculation
    sum = computePartialSum(array, 0, size / 2);
    exit(sum);
  }

  // The top half process will not know either child cpids
  if (top_cpid == 0 && bottom_cpid == 0) {
    // Top Calculation
    sum = computePartialSum(array, size / 2, size);
    exit(sum);
  }

  int child1_sum;
  int child2_sum;

  // Wait for both the top and bottom proceses to complete
  wait(&child1_sum);
  wait(&child2_sum);

  return child1_sum + child2_sum;
}

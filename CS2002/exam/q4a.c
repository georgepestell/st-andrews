#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>

// Make global variables accessible to the threads
int sum;
int *thread_array;
pthread_mutex_t sumMutex;

// Create a structure for thread parameters containing the start and end of
// the sum
typedef struct ThreadOpts {
  int low;
  int high;
} ThreadOpts;

/** GIVEN AS ASSUMED **/
int computePartialSum(int *array, int low, int high) {
  int sum = 0;
  for (int i = low; i < high; i++) {
    sum += array[i];
  }
  return sum;
}

// Thread method which takes arguments via a ThreadOpts struct holding high and
// low indexes for summing in an array
void *thread_computePartialSum(void *args) {
  // Cast the argument to the options structure
  ThreadOpts *opts = (ThreadOpts *)args;

  // Do the hard work in parallel
  int partial_sum = computePartialSum(thread_array, opts->low, opts->high);

  // Lock the sum mutex for accessing the global sum
  pthread_mutex_lock(&sumMutex);

  // Add the calculated partial_sum for this section of the array
  sum += partial_sum;

  // Relock the mutex for other threads
  pthread_mutex_unlock(&sumMutex);

  return NULL;
}

int computeSum(int array[], int size) {

  pthread_t thread_bottom, thread_top;

  // Initialize the mutex for incrementing the sum
  pthread_mutex_init(&sumMutex, NULL);

  // Initialize the values, making sum 0, and the array accessible to the
  // threads
  sum = 0;
  thread_array = array;

  // Create the option parameters for each thread
  // Odd numbers will truncate fractions so this will work with one thread
  // doing an extra calculation
  ThreadOpts bottom_opts = {0, size / 2};
  ThreadOpts top_opts = {size / 2, size};

  // Create two threads, one which totals from the top, and one which totals
  // from the bottom
  pthread_create(&thread_bottom, NULL, thread_computePartialSum, &bottom_opts);
  pthread_create(&thread_top, NULL, thread_computePartialSum, &top_opts);

  // Join the threads together to calculate the total sum
  pthread_join(thread_bottom, NULL);
  pthread_join(thread_top, NULL);

  // Return the calculated total sum
  return sum;
}

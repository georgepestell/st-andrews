/*
 * TestStack.c
 *
 * Very simple unit test file for BlockingStack functionality.
 *
 */

#include <stddef.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

#include "BlockingStack.h"
#include "myassert.h"

#define DEFAULT_MAX_STACK_SIZE 20

typedef struct ThreadOptions {
  bool slow;
  int count;
} ThreadOptions;

/*
 * The stack to use during tests
 */
static BlockingStack *stack;

/*
 * The number of tests that succeeded
 */
static int success_count = 0;

/*
 * The total number of tests run
 */
static int total_count = 0;

/*
 * Setup function to run prior to each test
 */
void setup() {
  stack = new_BlockingStack(DEFAULT_MAX_STACK_SIZE);
  total_count++;
}

/*
 * Teardown function to run after each test
 */
void teardown() { BlockingStack_destroy(stack); }

/*
 * This function is called multiple times from main for each user-defined test
 * function
 */
void runTest(int (*testFunction)()) {
  setup();

  if (testFunction())
    success_count++;

  teardown();
}

/*
 * Two sample user-defined tests included below.
 * You will have to write many more tests.
 *
 * Each test function should return TEST_SUCCESS at the end.
 * Test functions can contain more than one assertion.
 *
 * If any assertion fails, the function name and line number
 * will be printed and the test will return a failure value.
 *
 * You will have to call runTest on your test functions in main below.
 */

/*
 * Checks that the Stack constructor returns a non-NULL pointer.
 */
int newStackIsNotNull() {
  assert(stack != NULL);
  return TEST_SUCCESS;
}

/*
 * Checks that the size of an empty stack is 0.
 */
int newStackSizeZero() {
  assert(BlockingStack_size(stack) == 0);
  return TEST_SUCCESS;
}

/*
 * Check the isEmpty function returns true for a new stack
 */
int newStackIsEmpty() {
  assert(BlockingStack_isEmpty(stack) == true);
  return TEST_SUCCESS;
}

/*
 * Checks that a NULL stack is always empty
 */
int isEmptyOnNullStack() {
  assert(BlockingStack_isEmpty(NULL) == true);
  return TEST_SUCCESS;
}

/*
 * Checks that the size of a NULL stack returns the -1 error value
 */
int sizeOnNullStack() {
  assert(BlockingStack_size(NULL) == -1);
  return TEST_SUCCESS;
}

/*
 * Checks that a small item can be pushed onto the stack successfully
 */
int pushOneElement() {

  int element = 5;

  // Check the element is added succesfully
  assert(BlockingStack_push(stack, &element) == true);

  // Check the size increments and the stack is not empty
  assert(BlockingStack_size(stack) == 1);
  assert(BlockingStack_isEmpty(stack) == false);

  return TEST_SUCCESS;
}

/*
 * Checks that a small item can be push and popped off the stack successfully
 */
int pushAndPopOneElement() {

  // Setup the stack with one element
  int element = 6;
  BlockingStack_push(stack, &element);

  // Check the value of the popped element is correct
  assert(BlockingStack_pop(stack) == &element);

  // Check the size decrements and the stack is empty again
  assert(BlockingStack_size(stack) == 0);
  assert(BlockingStack_isEmpty(stack) == true);
  return TEST_SUCCESS;
}

/*
 * Checks that multiple elements can be pushed to and popped from a stack in
 * different orders
 */
int pushThreePopTwoThenPushOne() {
  // Create four elements which will be added to the stack
  int element_1 = 1;
  int element_2 = 2;
  int element_3 = 3;
  int element_4 = 4;

  // Push the pointers to three elements to the stack
  assert(BlockingStack_push(stack, &element_1) == 1);
  assert(BlockingStack_push(stack, &element_2) == 1);
  assert(BlockingStack_push(stack, &element_3) == 1);

  // Check the size of the stack has incremented by 3
  assert(BlockingStack_size(stack) == 3);

  // Pop two elements from the stack
  assert(BlockingStack_pop(stack) == &element_3);
  assert(BlockingStack_pop(stack) == &element_2);

  // Check the size of the stack has decremented by 2
  assert(BlockingStack_size(stack) == 1);

  // Push the last element pointer to the stack and check the size increments
  assert(BlockingStack_push(stack, &element_4) == 1);
  assert(BlockingStack_size(stack) == 2);

  return TEST_SUCCESS;
}

/*
 * Checks that a stack can be filled
 */
int pushTillFull() {
  // Create an element to put into the stack
  int element = 5;

  // Fill the Stack
  for (int i = 0; i < DEFAULT_MAX_STACK_SIZE; i++) {
    assert(BlockingStack_push(stack, &element) == 1);
  }

  // Double check the stack is full
  assert(BlockingStack_size(stack) == DEFAULT_MAX_STACK_SIZE);
  return TEST_SUCCESS;
}

/*
 * Checks that a stack can be filled and emptied
 */
int pushTillFullThenPopTillEmpty() {
  // Create an element to put into the stack
  int element = 5;

  // Fill the stack
  for (int i = 0; i < DEFAULT_MAX_STACK_SIZE; i++) {
    assert(BlockingStack_push(stack, &element) == 1);
  }

  // Empty the stack
  while (!BlockingStack_isEmpty(stack)) {
    BlockingStack_pop(stack);
  }

  // Double check the stack is empty
  assert(BlockingStack_isEmpty(stack) == true);

  return TEST_SUCCESS;
}

/*
 * Checks that pushing an element to a NULL stack always returns false
 */
int pushOnNullStack() {
  int element = 5;
  assert(BlockingStack_push(NULL, &element) == false);

  return TEST_SUCCESS;
}

/*
 * Checks that popping on a NULL stack always returns NULL
 */
int popOnNullStack() {
  assert(BlockingStack_pop(NULL) == NULL);
  return TEST_SUCCESS;
}

/*
 * Checks that the stack can hold generic pointers which can be pushed
 * successfully
 */
int pushDifferentPointerTypes() {
  int i = 6;
  char c = 'c';
  double d = 423.23231;

  assert(BlockingStack_push(stack, &i) == 1);
  assert(BlockingStack_push(stack, &c) == 1);
  assert(BlockingStack_push(stack, &d) == 1);

  return TEST_SUCCESS;
}

/*
 * Checks that the stack can hold generic pointers which can be popped
 * successfully
 */
int popDifferentPointerTypes() {
  // Create the different variable types
  int i = 6;
  char c = 'c';
  double d = 423.23231;

  // Add the element pointers to the list
  BlockingStack_push(stack, &i);
  BlockingStack_push(stack, &c);
  BlockingStack_push(stack, &d);

  // Check popping the pointers can be retrieved
  assert(BlockingStack_pop(stack) == &d);
  assert(BlockingStack_pop(stack) == &c);
  assert(BlockingStack_pop(stack) == &i);

  return TEST_SUCCESS;
}

/*
 * Checks that a NULL element pushed onto the stack is rejected
 */
int pushNullElement() {

  // Check return value of push is false and the size doesn't increment
  assert(BlockingStack_push(stack, NULL) == false);
  assert(BlockingStack_isEmpty(stack) == true);

  return TEST_SUCCESS;
}

/*
 * Checks that a stack with one element can be cleared
 */
int clearStackWithOneElement() {
  // Add an element to the stack
  int element = 5;
  BlockingStack_push(stack, &element);

  // Check clearing empties the stack
  BlockingStack_clear(stack);
  assert(BlockingStack_isEmpty(stack) == true);

  return TEST_SUCCESS;
}

/*
 * Checks that clearing a stack clears every item
 */
int clearFullStack() {
  // Fill the stack
  for (int i = 0; i < DEFAULT_MAX_STACK_SIZE; i++) {
    BlockingStack_push(stack, &i);
  }

  // Check clearing empties the stack
  BlockingStack_clear(stack);
  assert(BlockingStack_isEmpty(stack) == true);

  return TEST_SUCCESS;
}

/*
 * Checks that clearning an empty stack works but effectively does nothing
 */
int clearEmptyStack() {
  BlockingStack_clear(stack);
  assert(BlockingStack_isEmpty(stack) == true);

  return TEST_SUCCESS;
}

/*
 * Checks that clearing a NULL stack does nothing and doesn't crash
 */
int clearOnNullStack() {
  BlockingStack_clear(NULL);
  return TEST_SUCCESS;
}

/*
 * Thread for pushing a given number of elements onto the stack
 * args contains info on the number of elements and whether or not to add
 * slowness to the calls for thread-safe testing
 */
void *thread_push(void *args) {
  // Get the push options from the thread argument
  ThreadOptions *opts = (ThreadOptions *)args;

  // Push the given number of elements from the options
  for (int i = 0; i < opts->count; i++) {

    // Slow down every 5 calls if the slow flag is given
    if (opts->slow && i % 5 == 0)
      sleep(1);

    // Check a pointer can be successfully pushed to the stack
    int element = 5;
    assert(BlockingStack_push(stack, &element) == true);
  }

  return NULL;
}

/*
 * Thread for popping a given number of elements off the stack
 * args contains info on the number of elements and whether or not to add
 * slowness to the calls for thread-safe testing
 */
void *thread_pop(void *args) {
  // Get the pop options from the thread argument
  ThreadOptions *opts = (ThreadOptions *)args;

  // Pop the given number of elements from the stack
  for (int i = 0; i < opts->count; i++) {

    // If the slow flag is given, sleep every 5 pops
    if (opts->slow && i % 5 == 0) {
      sleep(1);
    }

    // Check an element can be successfully popped from the stack
    assert(BlockingStack_pop(stack) != NULL);
  }

  return NULL;
}

/*
 * Checks that the BlockingStack can be filled using multiple threads
 */
int threadedPushTillFull() {
  pthread_t thread1, thread2;

  // Make sure an even number which will fit in a void* is used as we are
  // splitting into two thread for this test
  int max_size = 20;
  stack = new_BlockingStack(max_size);

  // Setup the threading options with no slowing
  ThreadOptions opts = {false, max_size / 2};

  pthread_create(&thread1, NULL, thread_push, &opts);
  pthread_create(&thread2, NULL, thread_push, &opts);

  pthread_join(thread1, NULL);
  pthread_join(thread2, NULL);

  assert(BlockingStack_size(stack) == max_size);

  return TEST_SUCCESS;
}

/*
 * Checks that a Stack can be filled using multiple threads
 */
int threadedSlowPushAndPopMaxSize() {
  pthread_t thread1, thread2;

  // Make sure a max_size small enough to fit in a void* is used
  int max_size = 20;
  stack = new_BlockingStack(max_size);

  // Setup the threading options to slow pushing
  ThreadOptions push_opts = {true, max_size};
  ThreadOptions pop_opts = {false, max_size};

  pthread_create(&thread1, NULL, thread_push, &push_opts);
  pthread_create(&thread2, NULL, thread_pop, &pop_opts);

  pthread_join(thread1, NULL);
  pthread_join(thread2, NULL);

  assert(BlockingStack_size(stack) == 0);

  return TEST_SUCCESS;
}

/*
 * Checks that a Stack can be eventually pushed and popped with more elements
 * than the maximum size of the stack store through the use of the semaphores
 */
int threadedSlowPushAndPopOverMaxSize() {
  pthread_t thread1, thread2;

  stack = new_BlockingStack(DEFAULT_MAX_STACK_SIZE);

  // Setup the threading options to slow pushing
  ThreadOptions push_opts = {true, DEFAULT_MAX_STACK_SIZE * 2};
  ThreadOptions pop_opts = {false, DEFAULT_MAX_STACK_SIZE * 2};

  // Push and pop in different threads
  pthread_create(&thread1, NULL, thread_push, &push_opts);
  pthread_create(&thread2, NULL, thread_pop, &pop_opts);

  pthread_join(thread1, NULL);
  pthread_join(thread2, NULL);

  assert(BlockingStack_size(stack) == 0);

  return TEST_SUCCESS;
}

/*
 * Checks that a Stack can be filled using multiple threads when the popping is
 * slowed
 */
int threadedPushAndSlowPopMaxSize() {
  pthread_t thread1, thread2;

  // Make sure a max_size small enough to fit in a void* is used
  int max_size = 20;
  stack = new_BlockingStack(max_size);

  // Setup the threading options to slow pushing
  ThreadOptions push_opts = {false, max_size};
  ThreadOptions pop_opts = {true, max_size};

  pthread_create(&thread1, NULL, thread_push, &push_opts);
  pthread_create(&thread2, NULL, thread_pop, &pop_opts);

  pthread_join(thread1, NULL);
  pthread_join(thread2, NULL);

  assert(BlockingStack_size(stack) == 0);

  return TEST_SUCCESS;
}

/*
 * Checks that a Stack can be eventually pushed and popped with more elements
 * than the maximum size of the stack store through the use of the semaphores
 */
int threadedPushAndSlowPopOverMaxSize() {
  pthread_t thread1, thread2;

  stack = new_BlockingStack(DEFAULT_MAX_STACK_SIZE);

  // Setup the threading options to slow pushing
  ThreadOptions push_opts = {false, DEFAULT_MAX_STACK_SIZE * 2};
  ThreadOptions pop_opts = {true, DEFAULT_MAX_STACK_SIZE * 2};

  // Push and pop in different threads
  pthread_create(&thread1, NULL, thread_push, &push_opts);
  pthread_create(&thread2, NULL, thread_pop, &pop_opts);

  pthread_join(thread1, NULL);
  pthread_join(thread2, NULL);

  assert(BlockingStack_size(stack) == 0);

  return TEST_SUCCESS;
}

/*
 * Write more of your own test functions below (such as pushOneElement,
 * pushAndPopOneElement, ...) to help you verify correctness of your Stack
 */

/*
 * Main function for the Stack tests which will run each user-defined test in
 * turn.
 */

int main() {
  //// Setup and Info

  // Initial Values
  runTest(newStackIsNotNull);
  runTest(newStackSizeZero);
  runTest(newStackIsEmpty);

  // Edge Cases
  runTest(isEmptyOnNullStack);
  runTest(sizeOnNullStack);

  //// Push and Pop

  // Base Cases
  runTest(pushOneElement);
  runTest(pushAndPopOneElement);
  runTest(pushThreePopTwoThenPushOne);
  runTest(pushDifferentPointerTypes);
  runTest(popDifferentPointerTypes);
  runTest(pushNullElement);

  // Edge cases
  runTest(pushTillFull);
  runTest(pushTillFullThenPopTillEmpty);
  runTest(pushOnNullStack);
  runTest(popOnNullStack);

  //// Clearing Functionalty
  runTest(clearStackWithOneElement);
  runTest(clearFullStack);
  runTest(clearEmptyStack);
  runTest(clearOnNullStack);

  //// Threading Tests

  // Filling and Emptying
  runTest(threadedPushTillFull);
  runTest(threadedSlowPushAndPopMaxSize);
  runTest(threadedSlowPushAndPopOverMaxSize);
  runTest(threadedPushAndSlowPopMaxSize);
  runTest(threadedPushAndSlowPopOverMaxSize);

  printf("BlockingStack Tests complete: %d / %d tests "
         "successful.\n----------------\n",
         success_count, total_count);

  return 0;
}

/*
 * TestStack.c
 *
 * Very simple unit test file for Stack functionality.
 *
 */

#include <stddef.h>
#include <stdio.h>
#include <stdlib.h>

#include "Stack.h"
#include "myassert.h"

#define DEFAULT_MAX_STACK_SIZE 20

/*
 * The stack to use during tests
 */
static Stack *stack;

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
  stack = new_Stack(DEFAULT_MAX_STACK_SIZE);
  total_count++;
}

/*
 * Teardown function to run after each test
 */
void teardown() { Stack_destroy(stack); }

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
  assert(Stack_size(stack) == 0);
  return TEST_SUCCESS;
}

/*
 * Check the isEmpty function returns true for a new stack
 */
int newStackIsEmpty() {
  assert(Stack_isEmpty(stack) == true);
  return TEST_SUCCESS;
}

/*
 * Checks that a NULL stack is always empty
 */
int isEmptyOnNullStack() {
  assert(Stack_isEmpty(NULL) == true);
  return TEST_SUCCESS;
}

/*
 * Checks that the size of a NULL stack returns the -1 error value
 */
int sizeOnNullStack() {
  assert(Stack_size(NULL) == -1);
  return TEST_SUCCESS;
}

/*
 * Checks that a small item can be pushed onto the stack successfully
 */
int pushOneElement() {

  int element = 5;

  // Check the element is added succesfully
  assert(Stack_push(stack, &element) == true);

  // Check the size increments and the stack is not empty
  assert(Stack_size(stack) == 1);
  assert(Stack_isEmpty(stack) == false);

  return TEST_SUCCESS;
}

/*
 * Checks that a small item can be push and popped off the stack successfully
 */
int pushAndPopOneElement() {

  // Setup the stack with one element
  int element = 6;
  Stack_push(stack, &element);

  // Check the value of the popped element is correct
  assert(*(int *)Stack_pop(stack) == element);

  // Check the size decrements and the stack is empty again
  assert(Stack_size(stack) == 0);
  assert(Stack_isEmpty(stack) == true);
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
  assert(Stack_push(stack, &element_1) == 1);
  assert(Stack_push(stack, &element_2) == 1);
  assert(Stack_push(stack, &element_3) == 1);

  // Check the size of the stack has incremented by 3
  assert(Stack_size(stack) == 3);

  // Pop two elements from the stack
  assert(*(int *)Stack_pop(stack) == element_3);
  assert(*(int *)Stack_pop(stack) == element_2);

  // Check the size of the stack has decremented by 2
  assert(Stack_size(stack) == 1);

  // Push the last element pointer to the stack and check the size increments
  assert(Stack_push(stack, &element_4) == 1);
  assert(Stack_size(stack) == 2);

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
    assert(Stack_push(stack, &element) == 1);
  }

  // Double check the stack is full
  assert(Stack_size(stack) == DEFAULT_MAX_STACK_SIZE);
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
    assert(Stack_push(stack, &element) == 1);
  }

  // Empty the stack
  while (!Stack_isEmpty(stack)) {
    assert(Stack_pop(stack) == &element);
  }

  // Double check the stack is empty
  assert(Stack_isEmpty(stack) == true);

  return TEST_SUCCESS;
}

/*
 * Checks that pushing to a full stack will fail
 */
int pushOnFullStackFails() {
  // Create an element to put into the stack
  int element = 5;

  // Fill the stack
  for (int i = 0; i < DEFAULT_MAX_STACK_SIZE; i++) {
    assert(Stack_push(stack, &element) == 1);
  }

  // Try adding another element
  assert(Stack_push(stack, &element) == 0);
  assert(Stack_size(stack) == DEFAULT_MAX_STACK_SIZE);

  return TEST_SUCCESS;
}

/*
 * Checks that popping from an empty stack will fail
 */
int popEmptyStackReturnsNull() {
  // Try popping on the empty stack
  assert(Stack_pop(stack) == NULL);

  // Make sure the stack is still empty
  assert(Stack_isEmpty(stack) == true);

  return TEST_SUCCESS;
}

/*
 * Checks that pushing an element to a NULL stack always returns false
 */
int pushOnNullStack() {
  int element = 5;
  assert(Stack_push(NULL, &element) == false);

  return TEST_SUCCESS;
}

/*
 * Checks that popping on a NULL stack always returns NULL
 */
int popOnNullStack() {
  assert(Stack_pop(NULL) == NULL);
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

  assert(Stack_push(stack, &i) == 1);
  assert(Stack_push(stack, &c) == 1);
  assert(Stack_push(stack, &d) == 1);

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
  Stack_push(stack, &i);
  Stack_push(stack, &c);
  Stack_push(stack, &d);

  // Check popping the pointers can be retrieved and dereferenced
  assert(*(double *)Stack_pop(stack) == d);
  assert(*(char *)Stack_pop(stack) == c);
  assert(*(int *)Stack_pop(stack) == i);

  return TEST_SUCCESS;
}

/*
 * Checks that a NULL element pushed onto the stack is rejected
 */
int pushNullElement() {

  // Check return value of push is false and the size doesn't increment
  assert(Stack_push(stack, NULL) == false);
  assert(Stack_isEmpty(stack) == true);

  return TEST_SUCCESS;
}

/*
 * Checks that a stack with one element can be cleared
 */
int clearStackWithOneElement() {
  // Add an element to the stack
  int element = 5;
  Stack_push(stack, &element);

  // Check clearing empties the stack
  Stack_clear(stack);
  assert(Stack_isEmpty(stack) == true);

  return TEST_SUCCESS;
}

/*
 * Checks that clearing a stack clears every item
 */
int clearFullStack() {
  // Fill the stack
  for (int i = 0; i < DEFAULT_MAX_STACK_SIZE; i++) {
    Stack_push(stack, &i);
  }

  // Check clearing empties the stack
  Stack_clear(stack);
  assert(Stack_isEmpty(stack) == true);

  return TEST_SUCCESS;
}

/*
 * Checks that clearning an empty stack works but effectively does nothing
 */
int clearEmptyStack() {
  Stack_clear(stack);
  assert(Stack_isEmpty(stack) == true);

  return TEST_SUCCESS;
}

/*
 * Checks that clearing a NULL stack does nothing and doesn't crash
 */
int clearOnNullStack() {
  Stack_clear(NULL);
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
  runTest(pushOnFullStackFails);
  runTest(popEmptyStackReturnsNull);
  runTest(pushOnNullStack);
  runTest(popOnNullStack);

  //// Clearing Functionalty

  runTest(clearStackWithOneElement);
  runTest(clearFullStack);
  runTest(clearEmptyStack);
  runTest(clearOnNullStack);

  printf("Stack Tests complete: %d / %d tests successful.\n----------------\n",
         success_count, total_count);
}

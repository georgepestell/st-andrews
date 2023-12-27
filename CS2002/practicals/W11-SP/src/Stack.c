/*
 * Stack.c
 *
 * Fixed-size generic array-based Stack implementation.
 *
 */

#include "Stack.h"

#include <stddef.h>
#include <stdlib.h>

/*
 * This functions below all return default values and don't work.
 * You will need to provide a correct implementation of the Stack module
 * interface as documented in Stack.h.
 */

Stack *new_Stack(int max_size) {
  Stack *stack = malloc(sizeof(Stack));
  stack->store = malloc(max_size * sizeof(void *));

  stack->max_size = max_size;
  stack->size = 0;
  return stack;
}

bool Stack_push(Stack *this, void *element) {

  // If the element or stack is null, return failure value false
  if (element == NULL || this == NULL)
    return false;

  // If the stack is full, return false, return failure value false
  if (Stack_size(this) >= this->max_size)
    return false;

  // -- Add the element pointer to the array --
  //
  // Calculating the size * pointersize + arraypointer gets the next empty
  // malloced space of the array which can be filled with the element.
  //
  // The size is then incremented.
  //
  *(this->store + (sizeof(void *) * this->size++)) = element;

  return true;
}

void *Stack_pop(Stack *this) {
  // If the stack is NULL or empty, return NULL
  if (!this || Stack_isEmpty(this))
    return NULL;

  // -- Return the most recently added pointer in the array --
  //
  // Decrementing the size of the array and then calculating
  // size * pointersize + arraypointer gets the last filled array space which
  // can be returned.
  //
  // We don't need to clear the data in this space because it will be
  // overwritten next time something is added.
  //
  return *(this->store + (sizeof(void *) * --this->size));
}

int Stack_size(Stack *this) {

  // Make sure the stack parameter is not NULL
  // returning error code -1 if it is
  if (!this)
    return -1;

  // If the stack exists, return it's size
  return this->size;
}

bool Stack_isEmpty(Stack *this) {
  // Make sure the stack parameter is not NULL
  // Return true if it is as NULL stacks are empty
  if (!this)
    return true;

  // If the stack size is 0 it is empty
  if (this->size == 0)
    return true;

  // If not, the stack is not empty
  return false;
}

void Stack_clear(Stack *this) {
  // Make sure the stack parameter is not NULL
  if (!this)
    return;

  // Repeatedly pop elements till the stack is empty
  while (!Stack_isEmpty(this)) {
    Stack_pop(this);
  }
}

void Stack_destroy(Stack *this) {

  // Make sure the stack parameter is not null
  if (this == NULL)
    return;

  // Free the malloced space for the Stack data
  free(this->store);

  // Free the memory space for the Stack itself
  free(this);
}

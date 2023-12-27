/*
 * BlockingStack.c
 *
 * Fixed-size generic array-based BlockingStack implementation.
 *
 */

#include "BlockingStack.h"

#include <fcntl.h>
#include <pthread.h>
#include <semaphore.h>
#include <stdio.h>
#include <stdlib.h>

/*
 * This functions below all return default values and don't work.
 * You will need to provide a correct implementation of the Stack module
 * interface as documented in BlockingStack.h.
 */

BlockingStack *new_BlockingStack(int max_size) {

  // Allocate space for the blocking stack
  BlockingStack *blockingStack = malloc(sizeof(BlockingStack));

  // Create a Stack to contain the data
  blockingStack->stack = new_Stack(max_size);

  // Initialise mutexes and semaphores
  if (sem_init(&blockingStack->mutex, 0, 1))
    return NULL;
  if (sem_init(&blockingStack->num_elements, 0, 0))
    return NULL;
  if (sem_init(&blockingStack->num_free, 0, max_size))
    return NULL;

  // Return the created BlockingStack
  return blockingStack;
}

bool BlockingStack_push(BlockingStack *this, void *element) {
  // Make sure the BlockingStack exists
  if (!this)
    return false;

  // Wait for there to be space in the stack to push to.
  // If this fails, we can safely return error code false
  if (sem_wait(&this->num_free) != 0) {
    return false;
  }

  // Lock the mutex for pushing the stack. If this goes wrong, something
  // bad has happened
  if (sem_wait(&this->mutex) != 0) {
    abort();
  }

  // Store the success value for the push for returning
  bool success = Stack_push(this->stack, element);

  // Unlock the mutex, if this goes wrong, something bad has happened
  if (sem_post(&this->mutex) != 0) {
    abort();
  }

  // If adding succeeds, the number of elements has increased, if not we have to
  // re-add back to the number of free elements
  if (success)
    sem_post(&this->num_elements);
  else
    sem_post(&this->num_free);

  // Return the success value for pushing to the stack
  return success;
}

void *BlockingStack_pop(BlockingStack *this) {
  if (!this)
    return NULL;

  // Wait for there to be elements in the stack and decrement once avaliable. If
  // this fails, we can safely return NULL error code
  if (sem_wait(&this->num_elements) != 0) {
    return NULL;
  }

  // Lock the store mutex. If this fails, something bad has happened.
  if (sem_wait(&this->mutex) != 0) {
    abort();
  }

  // Pop the element from the stack
  void *element = Stack_pop(this->stack);

  // Unlock the store mutex. If this goes wrong, something bad has happened
  if (sem_post(&this->mutex) != 0) {
    abort();
  }

  // If popping is successfull, then the number of free elements is increased.
  // If it fails, then we have to re-add to the number of elements decremented
  // earlier
  if (element != NULL)
    sem_post(&this->num_free);
  else
    sem_post(&this->num_elements);

  // Return the popped element
  return element;
}

int BlockingStack_size(BlockingStack *this) {
  // Make sure the BlockingStack exists
  // Return -1 error code if not
  if (!this)
    return -1;

  // Lock the store mutex to get the current size. If this fails, something bad
  // has happened
  if (sem_wait(&this->mutex) != 0) {
    abort();
  }

  // Get the size of the stack
  int size = Stack_size(this->stack);

  // UnLock the store mutex. If this fails, something bad has happened
  if (sem_post(&this->mutex) != 0) {
    abort();
  }

  // Return the size of the stack at time of calling
  return size;
}

bool BlockingStack_isEmpty(BlockingStack *this) {
  // Check the BlockingStack exists
  // NULL stacks are always empty
  if (!this)
    return true;

  // Get the number of elements in the stack
  int size = BlockingStack_size(this);

  // If the size is 0 or below, it is empty
  if (size <= 0)
    return true;

  // If size is greater than 0, it is not empty
  return false;
}

void BlockingStack_clear(BlockingStack *this) {
  // Skip clearing if the given BlockingStack is NULL
  if (!this)
    return;

  // Lock the store mutex. If this goes wrong, something bad has happened
  if (sem_wait(&this->mutex) != 0) {
    abort();
  }

  // Clear the stack
  Stack_clear(this->stack);

  // Unlock the store mutex. If this goes wrong, something bad has happened
  if (sem_post(&this->mutex) != 0) {
    abort();
  }
}

void BlockingStack_destroy(BlockingStack *this) {
  // Skip destroying if the given BlockingStack is NULL
  if (!this)
    return;

  // Free the space for the contained Stack
  Stack_destroy(this->stack);

  // Destroy the semaphores and mutexes
  sem_destroy(&this->mutex);
  sem_destroy(&this->num_elements);
  sem_destroy(&this->num_free);

  // Free the space for the BlockingStack
  free(this);
}

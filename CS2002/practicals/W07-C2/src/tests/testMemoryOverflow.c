#include <stdio.h>
#include <string.h>
#include <errno.h>
#include "../word.h"
#include "../wordList.h"

#define INITIAL_COUNT 1
#define WORD_TEXT "george"

// Test creating a new word
void testWordCreateMemoryOverflow() {

  // Reset errno for test
  errno = 0;

  printf("----------------------------------------\n");
  printf("--- Running testWordCreateMemoryOverflow\n");
  printf("----------------------------------------\n");

  Word* word = Word_create(WORD_TEXT);

  if (errno != 0 && word == NULL)
    printf("\n✅ SUCCESS\n");
  else
    printf("\n❌ FAILURE\n");

  printf("----------------------------------------\n\n");
}
//
// Test creating a new word
void testWordListCreateMemoryOverflow() {

  // Reset errno for test
  errno = 0;

  printf("----------------------------------------\n");
  printf("--- Running testWordListCreateMemoryOverflow\n");
  printf("----------------------------------------\n");

  WordList* wordList = WordList_create();

  if (errno != 0 && wordList == NULL)
    printf("\n✅ SUCCESS\n");
  else
    printf("\n❌ FAILURE\n");

  printf("----------------------------------------\n\n");
}

int main () {
  testWordCreateMemoryOverflow();
  testWordListCreateMemoryOverflow();
}

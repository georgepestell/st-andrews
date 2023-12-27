#include <stdio.h>
#include <string.h>
#include <errno.h>
#include "../word.h"

#define INITIAL_COUNT 1
#define WORD_TEXT "george"

// Test creating a new word
void testWordCreate() {

  // Reset errno for test
  errno = 0;

  printf("----------------------------------------\n");
  printf("--- Running testWordCreate\n");
  printf("----------------------------------------\n");

  Word* word = Word_create(WORD_TEXT);

  perror("\nError Status");

  if (errno == 0 && word != NULL)
    printf("\n✅ SUCCESS\n");
  else
    printf("\n❌ FAILURE\n");

  printf("----------------------------------------\n\n");
}

// Test creating a new word
void testWordCreateInvalidText() {

  // Reset errno for test
  errno = 0;

  printf("----------------------------------------\n");
  printf("--- Running testWordCreateInvalidText\n");
  printf("----------------------------------------\n");

  Word* word = Word_create(NULL);

  perror("\nError Status");

  if (word == NULL)
    printf("\n✅ SUCCESS\n");
  else
    printf("\n❌ FAILURE\n");

  printf("----------------------------------------\n\n");
}

// Test destroying a created word
void testWordDestroy() {

  // Reset errno for test
  errno = 0;

  printf("----------------------------------------\n");
  printf("--- Running testWordDestroy\n");
  printf("----------------------------------------\n");

  Word* word = Word_create(WORD_TEXT);

  Word_destroy(word);

  perror("\nError Status");

  if (errno == 0)
    printf("\n✅ SUCCESS\n");
  else
    printf("\n❌ FAILURE\n");

  printf("----------------------------------------\n\n");
}

// Check the initial text value of a word matches the text parameter
void testWordGetText() {

  // Reset errno for test
  errno = 0;

  printf("----------------------------------------\n");
  printf("--- Running testWordGetWordText\n");
  printf("----------------------------------------\n");

  Word* word = Word_create(WORD_TEXT);


  char* text = Word_getText(word);

  perror("\nError Status");
  printf("Text: %s\n", text);

  if (!strcmp(text, WORD_TEXT))
    printf("\n✅ SUCCESS\n");
  else {
    printf("Expected: %s, Actual: %s\n", WORD_TEXT, Word_getText(word));
    printf("\n❌ FAILURE\n");
  }

  printf("----------------------------------------\n\n");
}

// Check getting the initial default count
void testWordGetCount() {

  // Reset errno for test
  errno = 0;

  printf("----------------------------------------\n");
  printf("--- Running testWordGetCount\n");
  printf("----------------------------------------\n");

  Word* word = Word_create(WORD_TEXT);

  int count = Word_getCount(word);


  perror("\nError Status");
  printf("Count: %d\n", count);

  if (count == INITIAL_COUNT)
    printf("\n✅ SUCCESS\n");
  else {
    printf("Expected: %d, Actual: %d\n", 0, Word_getCount(word));
    printf("\n❌ FAILURE\n");
  }

  printf("----------------------------------------\n\n");
}

// Check getting a word's initial NULL "next" value
void testWordGetNext() {

  // Reset errno for test
  errno = 0;

  printf("----------------------------------------\n");
  printf("--- Running testWordGetNext\n");
  printf("----------------------------------------\n");

  Word* word = Word_create(WORD_TEXT);

  Word* next = Word_getNext(word);

  perror("\nError Status");
  printf("Next Text: %s\n", Word_getText(next));

  if (next == NULL)
    printf("\n✅ SUCCESS\n");
  else {
    printf("\n❌ FAILURE\n");
  }

  printf("----------------------------------------\n\n");
}

// Check incrementing a word's count
void testWordIncrementCount() {

  // Reset errno for test
  errno = 0;

  printf("----------------------------------------\n");
  printf("--- Running testWordIncrementCount\n");
  printf("----------------------------------------\n");

  Word* word = Word_create(WORD_TEXT);
  Word_incrementCount(word);

  perror("\nError Status");

  if (Word_getCount(word) == INITIAL_COUNT + 1)
    printf("\n✅ SUCCESS\n");
  else {
    printf("Expected: %d, Actual: %d\n", 0, Word_getCount(word));
    printf("\n❌ FAILURE\n");
  }

  printf("----------------------------------------\n\n");
}

// Check incrementing a word's count too high
void testWordIncrementCountTooHigh() {

  // Reset errno for test
  errno = 0;

  printf("----------------------------------------\n");
  printf("--- Running testWordIncrementCountTooHigh\n");
  printf("----------------------------------------\n");

  Word* word = Word_create(WORD_TEXT);
  for (int i = 0; i < MAX_WORD_COUNT; i++) {
    Word_incrementCount(word);
    if (errno != 0) break;
  }

  perror("\nError Status");

  if (errno != 0)
    printf("\n✅ SUCCESS\n");
  else {
    printf("Expected: %d, Actual: %d\n", 0, Word_getCount(word));
    printf("\n❌ FAILURE\n");
  }

  printf("----------------------------------------\n\n");
}

// Check settting a word's next attribute 
void testWordSetNext() {

  // Reset errno for test
  errno = 0;

  printf("----------------------------------------\n");
  printf("--- Running testWordSetNext\n");
  printf("----------------------------------------\n");

  Word* word = Word_create(WORD_TEXT);
  Word* word_next = Word_create("secondWord");
  
  Word_setNext(word, word_next);

  perror("\nError Status");

  if (Word_getNext(word) == word_next)
    printf("\n✅ SUCCESS\n");
  else {
    printf("\n❌ FAILURE\n");
  }

  printf("----------------------------------------\n\n");
}

// Check comparing two Word structs based on text attribute
void testWordEquals() {

  // Reset errno for test
  errno = 0;

  printf("----------------------------------------\n");
  printf("--- Running testWordEquals\n");
  printf("----------------------------------------\n");

  Word* word1 = Word_create(WORD_TEXT);
  Word* word2 = Word_create(WORD_TEXT);

  perror("\nError Status");

  if (Word_equals(word1, word2))
    printf("\n✅ SUCCESS\n");
  else {
    printf("\n❌ FAILURE\n");
  }

  printf("----------------------------------------\n\n");
}

void testWordNullGetText() {

  // Reset errno for test
  errno = 0;

  printf("----------------------------------------\n");
  printf("--- Running testWordNullGetText\n");
  printf("----------------------------------------\n");

  char* text = Word_getText(NULL);

  perror("\nError Status");

  if (text == NULL)
    printf("\n✅ SUCCESS\n");
  else {
    printf("\n❌ FAILURE\n");
  }

  printf("----------------------------------------\n\n");
}


void testWordNullGetNext() {

  // Reset errno for test
  errno = 0;

  printf("----------------------------------------\n");
  printf("--- Running testWordNullGetNext\n");
  printf("----------------------------------------\n");

  Word* next = Word_getNext(NULL);

  perror("\nError Status");

  if (next == NULL)
    printf("\n✅ SUCCESS\n");
  else {
    printf("\n❌ FAILURE\n");
  }

}

void testWordSetNextToItself() {

  // Reset errno for test
  errno = 0;

  printf("----------------------------------------\n");
  printf("--- Running testWordSetNext\n");
  printf("----------------------------------------\n");

  Word* word = Word_create(WORD_TEXT);
  
  Word_setNext(word, word);

  perror("\nError Status");

  if (Word_getNext(word) == word)
    printf("\n✅ SUCCESS\n");
  else {
    printf("\n❌ FAILURE\n");
  }

  printf("----------------------------------------\n\n");
}

int main() {
  testWordCreate();
  testWordCreateInvalidText();
  testWordDestroy();
  testWordGetText();
  testWordGetCount();
  testWordGetNext();
  testWordIncrementCount();
  testWordIncrementCountTooHigh();
  testWordSetNext();
  testWordEquals();
  testWordNullGetText();
  testWordNullGetNext();
  testWordSetNextToItself();
}

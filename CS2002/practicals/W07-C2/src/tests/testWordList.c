#include <stdio.h>
#include <string.h>
#include <errno.h>
#include <limits.h>
#include "../wordList.h"
#include "../word.h"

#define WORD_TEXT "george"

// Test creating a new word
void testWordListCreate() {

  // Reset errno for test
  errno = 0;

  printf("----------------------------------------\n");
  printf("--- Running testWordListCreate\n");
  printf("----------------------------------------\n");

  // --- BEGIN TEST
  
  // Create a word list
  WordList* wordList = WordList_create(WORD_TEXT);

  // --- CHECK SUCCCESS
  
  if (errno == 0 && wordList != NULL)
    printf("\n✅ SUCCESS\n");
  else
    printf("\n❌ FAILURE\n");

  printf("----------------------------------------\n\n");
}

// Test destroying a created word
void testWordListDestroy() {

  // Reset errno for test
  errno = 0;

  printf("----------------------------------------\n");
  printf("--- Running testWordListDestroy\n");
  printf("----------------------------------------\n");

  // Create the word to destroy
  WordList* wordList = WordList_create();

  // Destory the word list
  WordList_destroy(wordList);

  if (errno == 0)
    printf("\n✅ SUCCESS\n");
  else
    printf("\n❌ FAILURE\n");

  printf("----------------------------------------\n\n");
}

// Test getting initial head value
void testWordListGetHead() {

  // Reset errno for test
  errno = 0;

  printf("----------------------------------------\n");
  printf("--- Running testWordListGetHead\n");
  printf("----------------------------------------\n");

  // --- BEGIN TEST
  
  // Create a word list
  WordList* wordList = WordList_create(WORD_TEXT);

  // --- CHECK SUCCCESS
  
  if (wordList != NULL && WordList_getHead(wordList) == NULL)
    printf("\n✅ SUCCESS\n");
  else
    printf("\n❌ FAILURE\n");

  printf("----------------------------------------\n\n");
}

// Test setting a WordList head attribute
void testWordListSetHead() {

  // Reset errno for test
  errno = 0;

  printf("----------------------------------------\n");
  printf("--- Running testWordListSetHead\n");
  printf("----------------------------------------\n");

  // --- BEGIN TEST
  
  // Create a word list
  WordList* wordList = WordList_create();
  Word* word = Word_create(WORD_TEXT);

  // Set the head value
  WordList_setHead(wordList, word);

  // Get the head value
  Word* head = WordList_getHead(wordList);

  // --- CHECK SUCCCESS
  
  if (errno == 0 && wordList != NULL && head != NULL && Word_equals(head, word))
    printf("\n✅ SUCCESS\n");
  else
    printf("\n❌ FAILURE\n");

  printf("----------------------------------------\n\n");
}

// Test getting initial tail value
void testWordListGetTail() {

  // Reset errno for test
  errno = 0;

  printf("----------------------------------------\n");
  printf("--- Running testWordListGetTail\n");
  printf("----------------------------------------\n");

  // --- BEGIN TEST
  
  // Create a word list
  WordList* wordList = WordList_create(WORD_TEXT);

  // --- CHECK SUCCCESS
  
  if (wordList != NULL && WordList_getTail(wordList) == NULL)
    printf("\n✅ SUCCESS\n");
  else
    printf("\n❌ FAILURE\n");

  printf("----------------------------------------\n\n");
}

// Test setting a WordList tail attribute
void testWordListSetTail() {

  // Reset errno for test
  errno = 0;

  printf("----------------------------------------\n");
  printf("--- Running testWordListSetTail\n");
  printf("----------------------------------------\n");

  // --- BEGIN TEST
  
  // Create a word list
  WordList* wordList = WordList_create();
  Word* word = Word_create(WORD_TEXT);

  // Set the tail value
  WordList_setTail(wordList, word);

  // Get the head value
  Word* tail = WordList_getTail(wordList);

  // --- CHECK SUCCCESS
  
  if (errno == 0 && wordList != NULL && tail != NULL && Word_equals(tail, word))
    printf("\n✅ SUCCESS\n");
  else
    printf("\n❌ FAILURE\n");

  printf("----------------------------------------\n\n");
}

// Test trying to get the head of a NULL WordList
void testWordListNullGetHead() {

  // Reset errno for test
  errno = 0;

  printf("----------------------------------------\n");
  printf("--- Running testWordListNullGetHead\n");
  printf("----------------------------------------\n");

  // --- BEGIN TEST
  
  Word* head = WordList_getHead(NULL);

  // --- CHECK SUCCCESS
  
  if (head == NULL)
    printf("\n✅ SUCCESS\n");
  else
    printf("\n❌ FAILURE\n");

  printf("----------------------------------------\n\n");
}

// Test trying to get the tail of a NULL WordList
void testWordListNullGetTail() {

  // Reset errno for test
  errno = 0;

  printf("----------------------------------------\n");
  printf("--- Running testWordListNullGetTail\n");
  printf("----------------------------------------\n");

  // --- BEGIN TEST
  
  Word* tail = WordList_getTail(NULL);

  // --- CHECK SUCCCESS
  
  if (tail == NULL)
    printf("\n✅ SUCCESS\n");
  else
    printf("\n❌ FAILURE\n");

  printf("----------------------------------------\n\n");
}

// Test trying to set a NULL WordList's head
void testWordListNullSetHead() {

  // Reset errno for test
  errno = 0;

  printf("----------------------------------------\n");
  printf("--- Running testWordListNullSetHead\n");
  printf("----------------------------------------\n");

  // --- BEGIN TEST
  
  WordList* wordList = NULL;
  Word* word = Word_create(WORD_TEXT);

  WordList_setHead(wordList, word);

  // --- CHECK SUCCCESS
  
  if (errno != 0 && WordList_getHead(wordList) == NULL)
    printf("\n✅ SUCCESS\n");
  else
    printf("\n❌ FAILURE\n");

  printf("----------------------------------------\n\n");
}


// Test trying to set NULL WordList's tail
void testWordListNullSetTail() {

  // Reset errno for test
  errno = 0;

  printf("----------------------------------------\n");
  printf("--- Running testWordListNullSetTail\n");
  printf("----------------------------------------\n");

  // --- BEGIN TEST
  
  WordList* wordList = NULL;

  Word* word = Word_create(WORD_TEXT);

  WordList_setHead(wordList, word);

  // --- CHECK SUCCCESS
  
  if (errno != 0 && WordList_getHead(wordList) == NULL)
    printf("\n✅ SUCCESS\n");
  else
    printf("\n❌ FAILURE\n");

  printf("----------------------------------------\n\n");
}

// Test trying to set a WordList head to NULL
void testWordListSetNullHead() {

  // Reset errno for test
  errno = 0;

  printf("----------------------------------------\n");
  printf("--- Running testWordListSetNullHead\n");
  printf("----------------------------------------\n");

  // --- BEGIN TEST
  
  WordList* wordList = WordList_create();

  // This should work (but with warnings)
  WordList_setHead(wordList, NULL);

  // --- CHECK SUCCCESS
  
  if (errno == 0 && WordList_getHead(wordList) == NULL)
    printf("\n✅ SUCCESS\n");
  else
    printf("\n❌ FAILURE\n");

  printf("----------------------------------------\n\n");
}

// Test trying to set a WordList tail to NULL
void testWordListSetNullTail() {

  // Reset errno for test
  errno = 0;

  printf("----------------------------------------\n");
  printf("--- Running testWordListSetNullTail\n");
  printf("----------------------------------------\n");

  // --- BEGIN TEST
  
  WordList* wordList = WordList_create();

  // This should work (but with warnings)
  WordList_setTail(wordList, NULL);

  // --- CHECK SUCCCESS
  
  if (errno == 0 && WordList_getHead(wordList) == NULL)
    printf("\n✅ SUCCESS\n");
  else
    printf("\n❌ FAILURE\n");

  printf("----------------------------------------\n\n");
}

// Test checking if an empty WordList contains a word
void testWordListEmptyContains() {

  // Reset errno for test
  errno = 0;

  printf("----------------------------------------\n");
  printf("--- Running testWordListEmptyContains\n");
  printf("----------------------------------------\n");

  // --- BEGIN TEST
  
  WordList* wordList = WordList_create();

  Word* word = Word_create(WORD_TEXT);

  Word* contains = WordList_contains(wordList, word);

  // --- CHECK SUCCCESS
  
  if (errno == 0 && contains == NULL)
    printf("\n✅ SUCCESS\n");
  else
    printf("\n❌ FAILURE\n");

  printf("----------------------------------------\n\n");
}

// Test Adding a word to a list
void testWordListAddWord() {

  // Reset errno for test
  errno = 0;

  printf("----------------------------------------\n");
  printf("--- Running testWordListAddWord\n");
  printf("----------------------------------------\n");

  // --- BEGIN TEST
  
  WordList* wordList = WordList_create();

  Word* word = Word_create(WORD_TEXT);

  WordList_addWord(wordList, word);

  // --- CHECK SUCCCESS
  
  if (errno == 0)
    printf("\n✅ SUCCESS\n");
  else
    printf("\n❌ FAILURE\n");

  printf("----------------------------------------\n\n");
}


// Test Adding multiple words to a list
void testWordListAddThreeWords() {

  // Reset errno for test
  errno = 0;

  printf("----------------------------------------\n");
  printf("--- Running testWordListAddThreeWord\n");
  printf("----------------------------------------\n");

  // --- BEGIN TEST
  
  WordList* wordList = WordList_create();

  Word* word1 = Word_create("word1");
  Word* word2 = Word_create("word2");
  Word* word3 = Word_create("word3");

  printf("I1: text: %s, count: %d\n", Word_getText(word1), Word_getCount(word1));
  printf("I2: text: %s, count: %d\n", Word_getText(word2), Word_getCount(word2));
  printf("I3: text: %s, count: %d\n", Word_getText(word3), Word_getCount(word3));

  WordList_addWord(wordList, word1);
  WordList_addWord(wordList, word2);
  WordList_addWord(wordList, word3);

  Word* listWord1 = WordList_getHead(wordList);
  Word* listWord2 = Word_getNext(listWord1);
  Word* listWord3 = Word_getNext(listWord2);

  printf("\nL1: text: %s, count: %d\n", Word_getText(listWord1), Word_getCount(listWord1));
  printf("L2: text: %s, count: %d\n", Word_getText(listWord2), Word_getCount(listWord2));
  printf("L3: text: %s, count: %d\n", Word_getText(listWord3), Word_getCount(listWord3));

  // --- CHECK SUCCCESS
  
  if (errno == 0 && listWord1 == word1 && listWord2 == word2 && listWord3 == word3)
    printf("\n✅ SUCCESS\n");
  else
    printf("\n❌ FAILURE\n");

  printf("----------------------------------------\n\n");
}

// Test Adding multiple words to a list
void testWordListAddTwoDuplicateWords() {

  // Reset errno for test
  errno = 0;

  printf("----------------------------------------\n");
  printf("--- Running testWordListAddTwoDuplicateWords\n");
  printf("----------------------------------------\n");

  // --- BEGIN TEST
  
  WordList* wordList = WordList_create();

  Word* word1 = Word_create("word1");
  Word* word2 = Word_create("word2");
  Word* word3 = Word_create("word2");
  Word* word4 = Word_create("word2");

  printf("I1: text: %s, count: %d\n", Word_getText(word1), Word_getCount(word1));
  printf("I2: text: %s, count: %d\n", Word_getText(word2), Word_getCount(word2));
  printf("I3: text: %s, count: %d\n", Word_getText(word3), Word_getCount(word3));
  printf("I4: text: %s, count: %d\n", Word_getText(word4), Word_getCount(word4));

  Word* listWord1 = WordList_addWord(wordList, word1);
  printf("\nL1: text: %s, count: %d\n", Word_getText(listWord1), Word_getCount(listWord1));

  Word* listWord2 = WordList_addWord(wordList, word2);
  printf("L2: text: %s, count: %d\n", Word_getText(listWord2), Word_getCount(listWord2));

  Word* listWord3 = WordList_addWord(wordList, word3);
  printf("L3: text: %s, count: %d\n", Word_getText(listWord3), Word_getCount(listWord3));

  Word* listWord4 = WordList_addWord(wordList, word4);
  printf("L4: text: %s, count: %d\n", Word_getText(listWord4), Word_getCount(listWord4));


  // --- CHECK SUCCCESS
  
  if (errno == 0)
    printf("\n✅ SUCCESS\n");
  else
    printf("\n❌ FAILURE\n");

  printf("----------------------------------------\n\n");
}

// Test Adding multiple words to a list
void testWordListAddOverflowCount() {

  // Reset errno for test
  errno = 0;

  printf("----------------------------------------\n");
  printf("--- Running testWordListAddOverflowCount\n");
  printf("----------------------------------------\n");

  // --- BEGIN TEST
  
  WordList* wordList = WordList_create();


  for (int i = 0; i <= MAX_WORD_COUNT; i++) {
    Word* word = Word_create(WORD_TEXT);
    WordList_addWord(wordList, word);
  }

  // --- CHECK SUCCCESS
  
  if (errno != 0)
    printf("\n✅ SUCCESS\n");
  else
    printf("\n❌ FAILURE\n");

  printf("----------------------------------------\n\n");
}
// Test adding words then destroying the list
void testWordListAddDestroyWords() {

  // Reset errno for test
  errno = 0;

  printf("----------------------------------------\n");
  printf("--- Running testWordListAddDestroyWords\n");
  printf("----------------------------------------\n");

  // --- BEGIN TEST
  
  WordList* wordList = WordList_create();

  Word* word1 = Word_create("word1");
  Word* word2 = Word_create("word2");
  Word* word3 = Word_create("word3");

  WordList_addWord(wordList, word1);
  WordList_addWord(wordList, word2);
  WordList_addWord(wordList, word3);
  
  WordList_destroy(wordList);

  // --- CHECK SUCCCESS
  
  if (errno == 0)
    printf("\n✅ SUCCESS\n");
  else
    printf("\n❌ FAILURE\n");

  printf("----------------------------------------\n\n");
}

// Test adding a word to a NULL WordList
void testWordListNullAddWord() {

  // Reset errno for test
  errno = 0;

  printf("----------------------------------------\n");
  printf("--- Running testWordListNullAddWord\n");
  printf("----------------------------------------\n");

  // --- BEGIN TEST
  
  WordList* wordList = NULL;

  Word* word = Word_create(WORD_TEXT);

  WordList_addWord(wordList, word);

  // --- CHECK SUCCCESS
  
  if (errno != 0)
    printf("\n✅ SUCCESS\n");
  else
    printf("\n❌ FAILURE\n");

  printf("----------------------------------------\n\n");
}

// Test adding a null word
void testWordListAddNullWord() {

  // Reset errno for test
  errno = 0;

  printf("----------------------------------------\n");
  printf("--- Running testWordListAddNullWord\n");
  printf("----------------------------------------\n");

  // --- BEGIN TEST
  
  WordList* wordList = WordList_create();

  Word* word = NULL;

  WordList_addWord(wordList, word);

  // --- CHECK SUCCCESS
  
  if (errno == 0 && WordList_getHead(wordList) == NULL)
    printf("\n✅ SUCCESS\n");
  else
    printf("\n❌ FAILURE\n");

  printf("----------------------------------------\n\n");
}


// Test getting a Word contained in a WordList
void testWordListContains() {

  // Reset errno for test
  errno = 0;

  printf("----------------------------------------\n");
  printf("--- Running testWordListContains\n");
  printf("----------------------------------------\n");

  // --- BEGIN TEST
  
  WordList* wordList = WordList_create();

  Word* word1 = Word_create(WORD_TEXT);
  Word* word2 = Word_create("word2");

  WordList_addWord(wordList, word1);
  WordList_addWord(wordList, word2);

  Word* contains = WordList_contains(wordList, word2);

  // --- CHECK SUCCCESS
  
  if (errno == 0 && contains == word2)
    printf("\n✅ SUCCESS\n");
  else
    printf("\n❌ FAILURE\n");

  printf("----------------------------------------\n\n");
}

// Test getting a Word contained in a WordList as the first word
void testWordListContainsFirstWord() {

  // Reset errno for test
  errno = 0;

  printf("----------------------------------------\n");
  printf("--- Running testWordListContainsFirstWord\n");
  printf("----------------------------------------\n");

  // --- BEGIN TEST
  
  WordList* wordList = WordList_create();

  Word* word1 = Word_create(WORD_TEXT);
  Word* word2 = Word_create("word2");

  WordList_addWord(wordList, word1);
  WordList_addWord(wordList, word2);

  Word* contains = WordList_contains(wordList, word1);

  // --- CHECK SUCCCESS
  
  if (errno == 0 && contains == word1)
    printf("\n✅ SUCCESS\n");
  else
    printf("\n❌ FAILURE\n");

  printf("----------------------------------------\n\n");
}

// Test trying to get a Word not in a WordList
void testWordListNotContains() {

  // Reset errno for test
  errno = 0;

  printf("----------------------------------------\n");
  printf("--- Running testWordListNotContains\n");
  printf("----------------------------------------\n");

  // --- BEGIN TEST
  
  WordList* wordList = WordList_create();

  Word* word1 = Word_create(WORD_TEXT);
  Word* word2 = Word_create("word2");

  WordList_addWord(wordList, word1);

  Word* contains = WordList_contains(wordList, word2);

  // --- CHECK SUCCCESS
  
  if (errno == 0 && contains == NULL)
    printf("\n✅ SUCCESS\n");
  else
    printf("\n❌ FAILURE\n");

  printf("----------------------------------------\n\n");
}

// Test getting head value after items are addded
void testWordListGetHeadAfterAdding() {

  // Reset errno for test
  errno = 0;

  printf("----------------------------------------\n");
  printf("--- Running testWordListGetHeadAfterAdding\n");
  printf("----------------------------------------\n");

  // --- BEGIN TEST
  
  // Create a word list
  WordList* wordList = WordList_create(WORD_TEXT);

  Word* word1 = Word_create(WORD_TEXT);
  Word* word2 = Word_create("word2");

  WordList_addWord(wordList, word1);
  WordList_addWord(wordList, word2);

  Word* head = WordList_getHead(wordList);

  // --- CHECK SUCCCESS
  
  if (errno == 0 && head == word1)
    printf("\n✅ SUCCESS\n");
  else
    printf("\n❌ FAILURE\n");

  printf("----------------------------------------\n\n");
}

// Test getting tail value after items are addded
void testWordListGetTailAfterAdding() {

  // Reset errno for test
  errno = 0;

  printf("----------------------------------------\n");
  printf("--- Running testWordListGetTailAfterAdding\n");
  printf("----------------------------------------\n");

  // --- BEGIN TEST
  
  // Create a word list
  WordList* wordList = WordList_create(WORD_TEXT);

  Word* word1 = Word_create(WORD_TEXT);
  Word* word2 = Word_create("word2");

  WordList_addWord(wordList, word1);
  WordList_addWord(wordList, word2);

  Word* tail = WordList_getTail(wordList);

  // --- CHECK SUCCCESS
  
  if (errno == 0 && tail == word2)
    printf("\n✅ SUCCESS\n");
  else
    printf("\n❌ FAILURE\n");

  printf("----------------------------------------\n\n");
}

int main () {
  testWordListCreate();
  testWordListDestroy();
  testWordListGetHead();
  testWordListSetHead();
  testWordListGetTail();
  testWordListSetTail();
  testWordListNullSetHead();
  testWordListNullSetTail();
  testWordListSetNullHead();
  testWordListSetNullTail();
  testWordListEmptyContains();
  testWordListAddWord();
  testWordListAddThreeWords();
  testWordListAddTwoDuplicateWords();
  testWordListAddOverflowCount();
  testWordListAddDestroyWords();
  testWordListNullAddWord();
  testWordListAddNullWord();
  testWordListContains();
  testWordListContainsFirstWord();
  testWordListNotContains();
  testWordListGetHeadAfterAdding();
  testWordListGetTailAfterAdding();
}

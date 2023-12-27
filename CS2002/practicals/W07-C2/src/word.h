#include <stdlib.h>
#include <stdio.h>
#include <limits.h>

#ifndef TESTING_MEMORY_LIMIT
//#define TESTING_MEMORY_LIMIT
#endif


#ifndef TESTING_MEMORY_LIMIT
//#define TESTING_COUNT_MAX
#endif


#ifdef TESTING_COUNT_MAX
#define MAX_WORD_COUNT 100
#else
#define MAX_WORD_COUNT INT_MAX
#endif

#define MAX_WORD_SIZE 29

// Define Word (details in word.c)

// Define Word struct prototype
typedef struct Word Word;

// Word creation and deletion
Word* Word_create(char* text);
void Word_destroy(Word* word);

// Getters & Setters
char* Word_getText(Word* word);
int Word_getCount(Word* word);
Word* Word_getNext(Word* word);

char* Word_setText(Word* word, char* text);
Word* Word_setNext(Word* word, Word* next);
 
// Increment Count
void Word_incrementCount(Word* word);

// Compare words
int Word_equals(Word* word1, Word* word2);

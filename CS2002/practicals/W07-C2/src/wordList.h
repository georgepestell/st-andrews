#include <stdio.h>
#include "word.h"

#ifndef TESTING_MEMORY_LIMIT
//#define TESTING_MEMORY_LIMIT
#endif

// Prototype for Words type
typedef struct WordList WordList;

// Create new collection
WordList* WordList_create();

// Destory WordList once not needed
void WordList_destroy(WordList* wordList);

Word* WordList_contains(WordList* wordList, Word* word);

void WordList_setHead(WordList* wordList, Word* word);
Word* WordList_getHead(WordList* wordList);

void WordList_setTail(WordList* wordList, Word* word);
Word* WordList_getTail(WordList* wordList);

Word* WordList_addWord(WordList* wordList, Word* word);

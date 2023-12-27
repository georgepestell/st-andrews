#include <stdio.h>
#include <limits.h>
#include <ctype.h>
#include <string.h>
#include <errno.h>

#include "wordList.h"
#include "word.h"

// Collection holding an array of Word objects
typedef struct WordList WordList;

struct WordList {
  Word* head;
  Word* tail;
};

WordList* WordList_create() {

  WordList* wordList = malloc(sizeof(WordList));

#ifdef TESTING_MEMORY_LIMIT
  wordList = NULL;
  errno = ENOMEM;
#endif

  if (wordList == NULL) {
    perror("Error");
    return NULL;
  }

  // Set the default WordList attributes
  wordList->head = NULL;
  wordList->tail= NULL;

  return wordList;
}

/** 
 * Get the first Word in a list
 */
Word* WordList_getHead(WordList* wordList) {
  
  // Make sure the wordList is not NULL
  if (wordList == NULL) {
    return NULL;
  }

  return wordList->head;
}

/** 
 * Get the last Word in a list
 */
Word* WordList_getTail(WordList* wordList) {
  // Make sure the WordList parameter is valid
  if (wordList == NULL) {
    fprintf(stderr, "Error: Cannot get tail of NULL WordList\n");
    errno = EINVAL;
    return NULL;
  }
  return wordList->tail;
}

/**
 * Destory a previously created WordList by freeing the heap space
 */
void WordList_destroy(WordList* wordList) {

  // If parameter word is NULL, set errno and return
  if (wordList == NULL) {
    fprintf(stderr, "Error: Cannot destroy NULL WordList\n");
    errno = EINVAL;
    return;
  }

  // Get the head of the list to begin the loop
  Word* current = wordList->head;

  // Loop through all the words in the list and destroy them
  while (current != NULL) {

    // Get the next item in the list and temporarily store it
    Word* next = Word_getNext(current);

    // Destroy the current word
    Word_destroy(current);

    // If  destroying failed, print an error message but continue with cleanup
    if (errno != 0) {
      fprintf(stderr, "Error: Cleanup couldn't destory word in list");
    }

    // Set the current to the stored next value for the next loop
    current = next;

  }

  // Free WordList from memory
  free(wordList);

  // If an error has occured, undefined behaviour is likely so exit
  if (errno != 0) {
    perror("Critical Error Cleaning WordList");
    exit(EXIT_FAILURE);
  }

}

void WordList_setHead(WordList* wordList, Word* word) {
  
  // Make sure the WordList is not null before setting the head
  if (wordList == NULL) {
    errno = EINVAL;
    fprintf(stderr, "Error: Cannot set head of NULL WordList\n");
    return;
  }

  wordList->head = word;
}

void WordList_setTail(WordList* wordList, Word* word) {

  // Make sure the WordList is not null before setting the tail
  if (wordList == NULL) {
    errno = EINVAL;
    fprintf(stderr, "Error: Cannot set tail of NULL WordList\n");
    return;
  }

  wordList->tail = word;
}

// Checks to see if a Word is in the list, and returns a pointer to it
// or NULL if not
Word* WordList_contains(WordList* wordList, Word* word) {
  
  // If the WordList or Word is null, then it cannot exist in the list
  if (wordList == NULL || word == NULL) {
    return NULL;
  }

  // Loop through all list elements to check if the element exists
  Word* current = wordList->head;

  while (current != NULL) {

    // If the given Word matches the current Word in the list, return that list Word
    if (Word_equals(current, word)) {
      return current;
    }

    // If the current word is not a match, get the next one to continue the loop
    current = Word_getNext(current);
  }

  // If the Word cannot be found in the list, return NULL
  return NULL;

}

// Adds a given word to a given words collection, or increments the count
// if the word already exists, before returning the relevant word reference
// in the words collection
Word* WordList_addWord(WordList* wordList, Word* word) {

  // Make sure the wordlist is not null before adding the word
  if (wordList == NULL) {
    fprintf(stderr, "Error: Cannot add Word to a NULL WordList\n");
    errno = EINVAL;
    return NULL;
  }

  // If the word is NULL, adding it to the end of the list does nothing so we
  // can just return NULL
  if (word == NULL) {
    fprintf(stderr, "Error: Word is NULL\n");
    return NULL;
  }

  // If the WordList is empty, add by setting the head and tail values
  if (wordList->head == NULL || wordList->tail == NULL) {
    wordList->head = word;
    wordList->tail = word;
    return word;
  }

  // Check if the word already exists, and fetch it in the list if it does
  Word* wordInList = WordList_contains(wordList, word);

  // If the word list 
  if (wordInList != NULL) {

    // Destroy the duplicate word
    Word_destroy(word);

    // If destroying failed, return a failure
    if (errno != 0) {
      fprintf(stderr, "Error: Cannot destroy Word\n");
      return NULL;
    }

    // Increment the count of the pre-existing word in the collection
    Word_incrementCount(wordInList);

    // If the incrementing failed
    if (errno != 0) {
      fprintf(stderr, "Word \"%s\" in list exceeded maximum count\n", Word_getText(wordInList));
      return NULL;
    }

    // Return the word in the collection
    return wordInList;
  }

  // Here, the element is not already in the WordList so we can add it
  // by setting the next value of the tail and updating the tail value
  Word_setNext(WordList_getTail(wordList), word);

  // Set the tail to the newly added word
  WordList_setTail(wordList, word);

  // Return the word added in the list
  return word;
}


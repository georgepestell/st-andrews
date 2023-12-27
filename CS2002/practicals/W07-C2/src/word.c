#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>
#include <error.h>

#include "word.h"

typedef struct Word Word;

struct Word {
  char* text;
  int count;
  Word* next;
};

/**
 * Returns a reference to a new word struct holding the given text variable
 */
Word* Word_create(char* text) {

  // Make sure the text parameter is valid
  if (text == NULL) {
    fprintf(stderr, "Error: Invalid text parameter for word\n");
    errno = EINVAL;
    return NULL;
  }

  // Allocate heap space for new Word
  Word* word = malloc(sizeof(Word));

#ifdef TESTING_MEMORY_LIMIT
  word = NULL;
  errno = ENOMEM;
#endif

  if (errno != 0 || word == NULL) {
    fprintf(stderr, "Critical Error: Not Enough Heap Space for Word Malloc\n");
    return NULL;
  }

  // Set the default struct values
  word->count = 1;
  word->text = strdup(text);
  word->next = NULL;

  // Return the word pointer
  return word;
}

/**
 * Destroy a previously created word by freeing the heap space
 */
void Word_destroy(Word* word) {
  // If parameter word is NULL, set errno and return
  if (word == NULL) {
    fprintf(stderr, "Error: Cannot destory NULL Word\n");
    errno = EINVAL;
    return;
  }

  // Free the not-null Word struct
  free(word);

  // If an error has occured, undefined behaviour is likely so exit
  if (errno != 0) {
    perror("Critical Error");
    exit(1);
  }

}

// Returns the text string for a given Word struct
char* Word_getText(Word* word) {
  
  // If the word parameter is NULL, return the NULL error value
  if (word == NULL) {
    return NULL;
  }

  // Return the text attribute 
  return word->text;
}


// Returns the word count for a given Word struct
int Word_getCount(Word* word) {

  // If the word parameter is null, return 0
  if (word == NULL) {
    return 0;
  }

  // Return the count attribute
  return word->count;
}

// Get the next attribute for a given Word struct
Word* Word_getNext(Word* word) {

  // If the word parameter is null, return NULL
  if (word == NULL) {
    return NULL;
  }

  // Return the next attribute for word
  return word->next;
}

// Sets the text attribute for a given Word struct
char* Word_setText(Word* word, char* text) {

  // If the word parameter is null, return the NULL error value
  if (word == NULL) {
    fprintf(stderr, "Error: Attempted to set text for a NULL Word\n");
    errno = EINVAL;
    return NULL;
  }

  // Set the text attribute
  return word->text = text;
}

// Sets the next attribute for a given Word struct
Word* Word_setNext(Word* word, Word* next) {

  if (word == NULL) {
    fprintf(stderr, "Error: Attempted to set next for a NULL Word\n");
    errno = EINVAL;
    return NULL;
  }

  // Set the next attribute
  return word->next = next;
}


// Increments the word count for a given word
void Word_incrementCount(Word* word) {
  
  // Make sure the count does not exceed the bounds of an integer
  if (word->count >= MAX_WORD_COUNT) {
    fprintf(stderr, "Error: Max Count for Word \"%s\" reached\n", word->text);
    errno = EOVERFLOW;
    return;
  }

  word->count++;
}


// Check if two words are equivalent, returns 0 if not equivalent, 1 if 
// equivalent, or -1 if one or more words are invalid
int Word_equals(Word* word1, Word* word2) {

  // Check if parameter words are NULL to prevent looking at non-existent text
  // data
  if (word1 == NULL || word2 == NULL) {

    // If both words are NULL, return true
    if (word1 == NULL && word2 == NULL) {
      return 1;
    }

    return 0;
  }

  // If the two text attributes are the same, return true
  if (!strcmp(word1->text, word2->text)) {
    return 1;
  }
  
  // If text values are not the same, return false
  return 0;

}

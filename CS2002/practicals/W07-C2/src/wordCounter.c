#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <errno.h>

#include "wordList.h"
#include "word.h"

static WordList* parseFile(FILE* file) {

  // Try to create a new WordList
  WordList* wordList = WordList_create();

  // Make sure the WordList was sucessfully created
  if (errno != 0) {
    exit(EXIT_FAILURE);
  }

  // Create vars to hold the word and it's size
  char wordArray[MAX_WORD_SIZE];
  int wordSize = 0;

  // Stream the opened file's till there are no more characters left
  while (!feof(file)) {

    // Get the next character
    int c = fgetc(file);

    // If the char is not alphanumeric, then store the current word and
    // start a new word
    if (!isalpha(c)) {

      // If the current word is empty, skip adding it to the list
      if (wordSize == 0) {
        continue;
      }

      // Add a null terminal to the end of the word
      wordArray[wordSize++] = '\0';

      // Create a word using the current word array values
      Word* word = Word_create(wordArray);

      // Add the word to the WordList
      WordList_addWord(wordList, word);

      // Reset the wordSize to begin a new word
      wordSize = 0;

      // Skip adding the non-alphanumeric character to the next word
      continue;
    }

    // Check if the word would be too long if adding the current character
    // and print an error message
    if (wordSize >= MAX_WORD_SIZE) {
      fprintf(stderr, "Error: Word exceeded maximum word size\n");
      exit(EXIT_FAILURE);
    }

    // Add alphanumeric character to word
    wordArray[wordSize++] = tolower(c);
  }

  // Return the filled WordList
  return wordList;
}

// Print words text string and frequency from a given WordList
static void printWordList(WordList* wordList) {

  // Make sure the wordlist is not NULL
  if (wordList == NULL) {
    fprintf(stderr, "WordList parameter is NULL\n");
    exit(EXIT_FAILURE);
  }

  // Get the head of the WordList
  Word* word = WordList_getHead(wordList);

  // Print the CSV header
  printf("Word, Frequency\n");

  // Loop over each Word in the list
  while (word != NULL) {

    char* text = Word_getText(word);
    int count = Word_getCount(word);

    if (text == NULL || count == 0) {
      fprintf(stderr, "Error: Can't get text and/or count from word\n");
      exit(EXIT_FAILURE);
    }

    // Print it's text attribute and count
    printf("%s, %d\n", text, count);

    // Get the next word in the list
    word = Word_getNext(word);

  }

}

// Get the user input for a filename and print out a list of words and their
// respective frequency values in order of first appearance
int main(int argc, char** args) {

  // Make sure the user has supplied a filename
  if (argc != 2) {
    fprintf(stderr, "Usage: ./RunCounter <filename>\n");
    exit(EXIT_FAILURE);
  }

  // Try to open the supplied file
  char* filename = args[1];
  FILE* file = fopen(filename, "r");

  // If the file cannot be opened, print an error message and exit
  if (errno != 0) {
    fprintf(stderr, "Cannot open file %s for reading.\n", filename);
    fprintf(stderr, "Usage: ./RunCounter <filename>\n");
    exit(EXIT_FAILURE);
  }

  // Get the word list for the given file
  WordList* wordList = parseFile(file);

  // Print out the words in order of appearance
  printWordList(wordList);

  // Cleanup the WordList from memory
  WordList_destroy(wordList);

  if (errno != 0) {
    fprintf(stderr, "Error: Couldn't destory wordList\n");
    exit(EXIT_FAILURE);
  }

}

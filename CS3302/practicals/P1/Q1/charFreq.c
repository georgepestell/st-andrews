// frequencyAnalysis.c

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

typedef struct CharFreq {
    char character;
    int count;
} CharFreq;

int main(int argc, char *argv[]) {
    if (argc != 2) {
        printf("usage: ./freqAnalysis <string>\n");
        exit(1);
    }

    char* inputString = argv[1];
    int inputStringLength = strlen(inputString);

    CharFreq uniqueChars[inputStringLength];
    int uniqueCharCount = 0;

    // Count frequencies of all unique characters in the input string
    for (int position = 0; position < inputStringLength; position++) {

        char currentChar = inputString[position];

        // Check if the currentChar has already appeared
        bool isUnique = true;
        for (int i = 0; i < uniqueCharCount; i++) {

            // Increment matching unique char
            if (currentChar == uniqueChars[i].character) {
                uniqueChars[i].count++;
                isUnique = false;
                break;
            }
        }

        // If character hasn't appeared yet, append new CharFreq to array
        if (isUnique) {
           uniqueChars[uniqueCharCount++] = (CharFreq) {currentChar, 1}; 
        }
    }

    // Print the frequency array
    for (int i = 0; i < uniqueCharCount; i++) {
        float freq = ((float) uniqueChars[i].count / inputStringLength) * 100;
        printf("%0.1f%%\t'%c'\n", freq, uniqueChars[i].character);
    }
}


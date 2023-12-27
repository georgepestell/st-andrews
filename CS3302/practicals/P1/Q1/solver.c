#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main(int argc, char *argv[]) {

    if (argc != 4) {
        printf("usage: ./solver <ciphertext> <ciphertext_char> <plaintext_char>\n");
        exit(1);
    }

    char* cipherText = argv[1];
    int cipherLength = strlen(cipherText);

    char cipherChar = argv[2][0];
    char plainChar = argv[3][0];

    // Print ciphertext, replacing the cipherChar with plainChar
    for (int i = 0; i < cipherLength; i++) {
        if (cipherText[i] == cipherChar) {
            printf("%c", plainChar);
        } else {
            printf("%c", cipherText[i]);
        }
    }

    printf("\n");
    
}

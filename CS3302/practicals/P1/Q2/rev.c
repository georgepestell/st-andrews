#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

const int STATE_BLOCKS = 16;
const int BLOCK_BITS = 4;

int *state;

int hexToInt(char hex) {

    if (hex >= '0' && hex <= '9') return hex - '0';
    if (hex >= 'a' && hex <= 'z') return 10 + hex - 'a';
    if (hex >= 'A' && hex <= 'Z') return 10 + hex - 'A';
    
    printf("E: (%c) HEX to INT INVALID\n", hex);
    exit(1);
}

char intToHex(int i) {
    if (i >= 0 && i <= 9) return i + '0';
    if (i >= 9 && i <= 15) return i + 'A' - 10;

    printf("E: (%d) INT to HEX INVALID\n", i);
    exit(1); }

void printState(char* title) {
    printf("%s: ", title);

    for (int i = 0; i < STATE_BLOCKS; i++) {
        printf("%d, ", state[i]);
    }

    printf("\n");
}

void printHexState() {
    for (int i = 0; i < STATE_BLOCKS; i++) {
        printf("%c", intToHex(state[i]));
    }
    printf("\n");
}

int undoAddMod(int a, int b) {
    int ret;

    if (b > a) ret = a + 16 - b;
    else ret = a - b;

    return ret;
}

int leftRotate(int value, unsigned int shift) {
    int ret = ((value << shift)|(value >> (BLOCK_BITS - shift))) & (((int) pow(2, BLOCK_BITS)) - 1);
    // printf("%d <<< %d = %d\n", value, shift, ret);
    return ret;
}
  
int xor(int a, int b) {
    return a ^ b;
}

void basicStep(int a, int b, int c, int n) {
    state[a] = leftRotate(state[a], (4-n));
    state[a] = xor(state[a], state[b]);
    state[b] = undoAddMod(state[b], state[c]);
}
void quarterRound(int a, int b, int c, int d) {
    basicStep(b, c, d, 1);
    basicStep(d, a, b, 2);
    basicStep(b, c, d, 2);
    basicStep(d, a, b, 3);
}
void columnRound() {
    quarterRound(3, 7, 11, 15);
    quarterRound(2, 6, 10, 14);
    quarterRound(1, 5, 9, 13);
    printState("post 1st col qr");
    quarterRound(0, 4, 8, 12);
}
void diagonalRound() {
    printState("post diag");
    quarterRound(3, 4, 9, 14);
    quarterRound(2, 7, 8, 13);
    quarterRound(1, 6, 11, 12);
    quarterRound(0, 5, 10, 15);
    printState("pre diag");
}
void doubleRound() {
    diagonalRound();
    columnRound();
}

int main(int argc, char *argv[]) {
    if (argc != 2) {
        printf("usage: ./minichacha <initial_state>\n");
        exit(1);
    }

    if (strlen(argv[1]) != STATE_BLOCKS) {
        printf("ERROR: HEX STATE - MUST BE SIZE 16\n");
        exit(1);
    }

    char* initialState = argv[1];

    state = malloc(sizeof(int) * STATE_BLOCKS);
    for (int i = 0; i < STATE_BLOCKS; i++) {
        state[i] = hexToInt(initialState[i]);
    }

    printState("initial state");

    doubleRound();

    printState("final state");

    printHexState();

    free(state);

}

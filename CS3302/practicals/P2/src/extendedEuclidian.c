/** 
 * Code adapted from CS3302 Practical 1 (Q5)
 *
 */

// BEGIN ADAPTED CODE

#include <stdio.h>
#include <stdlib.h>

// input: x = 89, y = 55
// output: h,s,t: h=gcd(x,y), h = sx + ty

int X = 89;
int Y = 55;

typedef struct Pair {
    int x;
    int y;
} Pair;

typedef struct Output {
    int h;
    int s;
    int t;
} Output;

Output extEuclidian(int x, int y) {
    if (x <= y) {
        fprintf(stderr, "ERROR, x must be greater than y\n");
        exit(1);
    }

    Pair s = {1, 0};
    Pair t = {0, 1};

    while (y != 0) {

        int tmpX = x;
        int tmpY = y;
        Pair tmpS = s;
        Pair tmpT = t;

        x = y;
        y = tmpX % tmpY;
        s.x = tmpS.y;
        t.x = tmpT.y;
        s.y = tmpS.x - ((tmpX / tmpY) * tmpS.y);
        t.y = tmpT.x - ((tmpX / tmpY) * tmpT.y);

        // printf("\nx  = %d\ny  = %d\nSx = %d\nTx = %d\nSy = %d\nTy = %d\n", x, y, s.x, t.x, s.y, t.y);
    }

    return (Output) {x, s.x, t.x};

}

int main(int argc, char* args[]) {
    if (argc != 3) {
      printf("usage: extendedEuclidian X Y\n");
      exit(1);
    }

    int X = (int) strtol(args[1], NULL, 10);
    int Y = (int) strtol(args[2], NULL, 10);

    printf("X = %d\nY = %d\n", X, Y);

    Output results = extEuclidian(X, Y);

    printf("h = %d, s = %d, t = %d\n", results.h, results.s, results.t);
}

// END ADAPTED CODE

#include <stdio.h>
#include "collatz.h"

int length;

void print_result(bool result, int length, int* array) { 

    if (!result) { 
        printf("Sequence too long or numbers out of range \n");
    }
    for(int i=0; i < length; i++) { 
        printf("%d ",array[i]);
    }
    printf("\n");
}


int main(void) {

    printf("%d %d\n", MAXARRAYSIZE, INT_MAX);
    int sequence[MAXARRAYSIZE];

    print_result(collatz(15,3,1,2,&length,sequence),length,sequence);
    print_result(collatz(-1,3,1,2,&length,sequence),length,sequence);
    print_result(collatz(63,3,1,2,&length,sequence),length,sequence);
    print_result(collatz(15,4,1,3,&length,sequence),length,sequence);
    print_result(collatz(15,2,1,3,&length,sequence),length,sequence);
    print_result(collatz(1023,3,1,2,&length,sequence),length,sequence);
}


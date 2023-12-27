/*
Thread interleaving demo for CS2002 tutorial

Saleem Bhatti, 08 April 2012
Marwan Fayed, April 2018 (modifications)
Jon Lewis, April 2021 (modifications)

compile: 
  clang -o thread_ops thread_ops.c -pthread

run:
  ./thread_ops

or:
  ./thread_ops join

 */

#include <stdio.h>
#include <sys/types.h>
#include <sys/syscall.h>
#include <string.h>
#include <unistd.h>
#include <pthread.h>
#include <stdbool.h>

#define MAX_THREADS 11

unsigned long var = 0;

void *add(void *arg) {
     /* force sleep to give chance for next thread creation */
	nanosleep((const struct timespec[]){{0, 50000L}}, NULL);
    unsigned long val = *((unsigned long *) arg);
    var = var + val;
    printf("add(%lu) var: %lu\n", val, var);
    return (void *) 0;
}



int main(int argc, char* argv[]){

    pthread_t pt[MAX_THREADS];
    bool do_join = false;

    if (argc == 2 && strcmp(argv[1], "join") == 0)
        do_join = true;

    printf("-- start of main() var: %lu\n", var);
    for (unsigned long i = 0; i < MAX_THREADS; i++) {
        printf("main() i: %lu\n", i);
        pthread_create(&pt[i], NULL, add, (void *) &i);
    }

    if (do_join) {
        printf("-- before join in main() var: %lu\n", var);

        for (unsigned long i = 0; i < MAX_THREADS; i++) {
            pthread_join(pt[i], NULL);
            printf("main() pthread_join(%lu)\n", i);
        }
        printf("\n");
    }

    printf("-- end of main() var: %lu\n", var);
    return 0;
}

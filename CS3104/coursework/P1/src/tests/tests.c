#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <signal.h>

#include "../process.c"
#include "../sched.c"
#include "assert.h"

enum STATUS {
    FAILED = 0,
    SUCCESS = 1
};

int test_count = 0;
int success_count = 0;

long EXAMPLE_PRIORITY = 15.0;
char* EXAMPLE_PATH = "/usr/bin/ls";
char* EXAMPLE_ARGS[] = { "-a" };

Sched *sched;
Process *process;

void setup() {
    errno = 0;
    sched = Sched_create();
    process = Process_create(EXAMPLE_PRIORITY, EXAMPLE_PATH, (char **) EXAMPLE_ARGS);
}

void runTest(int test()) {
    setup();

    if ((*test)() == SUCCESS) {
        success_count++;
    }
    test_count++;
}

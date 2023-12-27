#ifndef _PROCESS_H
#define _PROCESS_H

const int DEFAULT_NEW_PID = -1;

const int MAX_PRIORITY = 20;
const int MIN_PRIORITY = 0;

const int CYCLES_BEFORE_AGEING = 3;

enum PROCESS_STATE {
    NEW,
    READY,
    RUNNING,
    TERMINATED
};

typedef struct Process {
    struct Process *next;
    struct Process *prev;
    enum PROCESS_STATE state;
    int pid;
    int priority;
    int runCount;
    char *path;
    char **args;
} Process;

// Create and Destroy

Process *Process_create(int priority, char* path, char** args);

void Process_setup(Process *process);

void Process_destroy(Process *process);

// Setters

void Process_setPriority(Process *process, int priority);

void Process_setNext(Process *process, Process *next);

void Process_setPrev(Process *process, Process *prev);

// Functionality

void Process_run(Process *process);

void Process_stop(Process *process);

void Process_age(Process *process);

#endif

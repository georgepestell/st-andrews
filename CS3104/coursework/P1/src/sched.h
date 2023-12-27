#ifndef _SCHED_H
#define _SCHED_H

#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <signal.h>
#include <wait.h>
#include <stdbool.h>
#include "process.h"

typedef struct Sched { 
    int size;
    struct Process *head;
    struct Process *tail;
} Sched;

Sched *Sched_create();

bool Sched_isEmpty(Sched *sched);

void Sched_admit(Sched *sched, Process *process);

void Sched_wait(Process *process);

void Sched_push(Sched* sched, Process* process);

Process *Sched_pop(Sched *sched);

void Sched_run(Sched *sched);

void Sched_cycle(Sched *sched);

void Sched_empty();

void Sched_destroy();

#endif

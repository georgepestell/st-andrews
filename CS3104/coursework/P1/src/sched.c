#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <signal.h>
#include <unistd.h>

#include "sched.h"

Sched *Sched_create() { 

    Sched *sched = malloc(sizeof(Sched));

    if (sched == NULL) {
        perror("Malloc failed creating Sched");
        return NULL;
    }

    sched->size = 0;
    sched->head = NULL;
    sched->tail = NULL;

    return sched;
}


Process *Sched_pop(Sched *sched) {
    if (sched == NULL) {
        errno = EINVAL;
        fprintf(stderr, "Can not pop from NULL sched\n");
        return NULL;
    }

    if (Sched_isEmpty(sched)) {
        return NULL;
    }

    Process *process = sched->head;

    sched->head = process->next;
    sched->size--;

    if (Sched_isEmpty(sched)) {
        sched->tail = NULL;
    }

    Process_setNext(process->prev, process->next);
    Process_setPrev(process->next, process->prev);

    Process_setNext(process, NULL);
    Process_setPrev(process, NULL);

    return process;
}

bool Sched_isEmpty(Sched* sched) {
    if (sched->size == 0) {
        return true;
    } else {
        return false;
    }
}

void Sched_empty(Sched *sched) {
    while (!Sched_isEmpty(sched)) {
        Sched_pop(sched);
    }
}

void Sched_destroy(Sched *sched) {
    if (!sched) {
        errno = EINVAL;
        fprintf(stderr, "Cannot destroy NULL sched\n");
        return;
    }
    if (sched->head) {
        fprintf(stderr, "Warning: Destroying non-empty sched\n");
    }

    // Destroy all contained processes
    while(!Sched_isEmpty(sched)) {
        Process_destroy(Sched_pop(sched));
    }

    free(sched);
}


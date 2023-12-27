// This program reads a configuration file and executes each command specified
// using a simple user-space scheduler

#include "sched.h"

void Sched_admit(Sched *sched, Process* process) {
    if (process == NULL) {
        fprintf(stderr, "Can not admit NULL PCB\n");
        errno = EINVAL;
        return;
    }
    if (process->state != NEW) {
        fprintf(stderr, "Warning: process already admitted");
        return;
    }

    Process_setup(process);

    Sched_push(sched, process);

    return;
}

void Sched_push(Sched *sched, Process *process) {
    if (sched == NULL) {
        errno = EINVAL;
        fprintf(stderr, "Can not push to NULL sched\n");
        return;
    }
    if (process == NULL) {
        errno = EINVAL;
        fprintf(stderr, "Can not push NULL to sched\n");
        return;
    }

    Process_setPrev(process, sched->tail);
    Process_setNext(sched->tail, process);


    if (Sched_isEmpty(sched)) {
        sched->head = process;
    }

    sched->tail = process;
    sched->size++;
}

void Sched_run(Sched *sched) {
    if (sched == NULL) {
        fprintf(stderr, "Cannot run scheduler on NULL sched\n");
        errno = EINVAL;
        return;
    }

    while (!Sched_isEmpty(sched)) {
        Sched_cycle(sched);
    } 
}

void Sched_cycle(Sched *sched) {
    Process *process = Sched_pop(sched);

    Process_run(process);
    Sched_wait(process);
    
    if (process->state != TERMINATED) {
        Sched_push(sched, process);
    }
}

void Sched_wait(Process *process) {
    if (!process) {
        fprintf(stderr, "Cannot wait for NULL process\n");
        errno = EINVAL;
        return;
    }

    if (process->state == RUNNING) { 
        int status;
        waitpid(process->pid, &status, 0);

        // Set the state based upon the child process state
        if (WIFSTOPPED(status)) {
            process->state = READY;
        } else {
            process->state = TERMINATED;
        }

    }
}

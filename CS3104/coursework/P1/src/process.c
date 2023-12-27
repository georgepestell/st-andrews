#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <string.h>
#include <signal.h>
#include <unistd.h>

#include "process.h"

Process *Process_create(int priority, char* path, char** args) {
    if (!path) {
        errno = EINVAL;
        fprintf(stderr, "Cannot create process with NULL path\n");
        return NULL;
    }
    if (!args) {
        errno = EINVAL;
        fprintf(stderr, "Cannot create process with NULL args\n");
        return NULL;
    }
    
    Process *process = malloc(sizeof(Process));

    if (!process) {
        perror("Malloc failed creating PCB");
        return NULL;
    }

    process->next = NULL;
    process->prev = NULL;
    process->state = NEW;
    process->pid = DEFAULT_NEW_PID;
    process->runCount = 0;

    process->args = args;
    process->path = strdup(path);

    Process_setPriority(process, priority);

    return process;
}

void Process_setup(Process *process) {
    if (process == NULL) {
        fprintf(stderr, "Cannot setup NULL PCB\n");
        errno = EINVAL;
        return;
    }

    if (process->pid != DEFAULT_NEW_PID) {
        fprintf(stderr, "Warning: process already setup");
    }

    int pid = fork();

    if (pid < 0) {
        perror("Fork failed\n");
        process->state = TERMINATED;
        return;
    }
    else if (pid == 0) {
        execvp(process->path, process->args);
        exit(0);
    }

    kill(pid, SIGSTOP);
    process->pid = pid;
    process->state = READY;
}

void Process_setPriority(Process *process, int priority) {
    if (priority > MAX_PRIORITY) {
        process->priority = MAX_PRIORITY;
    } else if (priority < MIN_PRIORITY) {
        process->priority = MIN_PRIORITY;
    } else {
        process->priority = priority;
    }
}

void Process_setNext(Process *process, Process *next) {
    if (!process) {
        return;
    }

    process->next = next;
}

void Process_setPrev(Process *process, Process *prev) {
    if (!process) {
        return;
    }

    process->prev = prev;
}

void Process_run(Process *process) {
    if (!process) {
        fprintf(stderr, "Can not run NULL PCB\n");
        errno = EINVAL;
        return;
    }

    if (process->state == READY) {
        kill(process->pid, SIGCONT);
        process->runCount++;
        process->state = RUNNING;
    } else {
        fprintf(stderr, "Process not ready to run\n");
    }

}

void Process_stop(Process *process) {
    if (process == NULL) {
        fprintf(stderr, "Cannot stop NULL process\n");
        errno = EINVAL;
        return;
    }
    if (process->state == RUNNING) {
        process->state = READY;
        kill(process->pid, SIGSTOP);
    }
} 

void Process_age(Process *process) {
    if (!process) {
        fprintf(stderr, "Cannot age NULL Process\n");
        errno = EINVAL;
        return;
    }

    process->runCount++;

    if (process->runCount >= CYCLES_BEFORE_AGEING) {
        process->runCount = 0;
        Process_setPriority(process, process->priority - 1);
    }
}

void Process_destroy(Process *process) {
    if (!process) { 
        errno = EINVAL;
        fprintf(stderr, "Cannot destroy NULL sched\n");
        return;
    }

    free(process);
}

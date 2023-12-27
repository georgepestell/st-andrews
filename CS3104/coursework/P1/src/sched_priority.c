// This program reads a configuration file and executes each command specified
// using a simple user-space scheduler

#include "sched.h"

const int QUANTUM_MILLISECS = 100;
const int MICRO_IN_MILLI_SECS = 1000;

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
    if (!sched) {
        errno = EINVAL;
        fprintf(stderr, "Can not push to NULL sched\n");
        return;
    }
    if (!process) {
        errno = EINVAL;
        fprintf(stderr, "Can not push NULL to sched\n");
        return;
    }

    if (Sched_isEmpty(sched)) {
        sched->head = process;
        sched->tail = process;
        sched->size++;
        return;
    }

    Process *current = sched->head;
    Process *smaller = NULL;
    while (current != NULL) {
        if (process->priority > current->priority) {
            smaller=current;
            break;
        }
        current=current->next;
    }

    if (!smaller) {
        process->next = NULL;
        process->prev = sched->tail;
        sched->tail->next = process;
        sched->tail = process;
    } else if (smaller == sched->head) {
        process->prev = NULL;
        process->next = sched->head;
        sched->head->prev = process;
        sched->head = process;
    } else {
        process->next = smaller;
        process->prev = smaller->prev; 
        smaller->prev->next = process;
        smaller->prev = process;
    }

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
    if (!sched) {
        fprintf(stderr, "Cannot run cycle on NULL Sched");
    }

    
    Process *process = Sched_pop(sched);

    Process_run(process);
    Sched_wait(process);

    Process_age(process);

    if (process->state != TERMINATED) {
        Sched_push(sched, process);
    }

}

void Sched_wait(Process *process) {
    if (!process) {
        fprintf(stderr, "Cannot wait for NULL Process\n");
        errno = EINVAL;
        return;
    }

    if (process->state == RUNNING) {
        pid_t timer_pid = fork();
        if (timer_pid < 0) {
            fprintf(stderr, "Timer fork failed\n");
            Process_stop(process);
            return;
        } else if (timer_pid == 0) {
            usleep(QUANTUM_MILLISECS * MICRO_IN_MILLI_SECS);
            Process_stop(process);
            exit(0);
        }

        int status;
        pid_t result = waitpid(process->pid, &status, WNOHANG);

        if (result == 0) {
            waitpid(process->pid, &status, WUNTRACED);
        }

        kill(timer_pid, SIGTERM);

        if (WIFSTOPPED(status)) {
            process->state = READY;
        } else {
            process->state = TERMINATED;
        }
    }
}

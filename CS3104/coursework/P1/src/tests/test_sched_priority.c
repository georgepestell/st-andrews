#include "tests.c"

#include "../sched_priority.c"

int testPushProcessToSched() {
    Sched_push(sched, process);

    assert(sched->head == process);
    assert(sched->tail == process);
    return SUCCESS;
}

int testPushIncrementsSchedSize() {
    Sched_push(sched, process);

    assert(sched->size == 1);
    return SUCCESS;
} 

int testPopProcessFromSched() {
    Sched_push(sched, process);

    Process *popped_process = Sched_pop(sched);

    assert(popped_process == process);
    assert(!sched->head);
    assert(!sched->tail);

    return SUCCESS;
}

int testPopDecrementsSchedSize() {
    Sched_push(sched, process);

    Process *popped_process = Sched_pop(sched);
    assert(sched->size == 0);

    return SUCCESS;
}

int testPushProcessesToSched() {
    long priority_2 = 20.0;
    char *path_2 = "/usr/bin/cp";
    char *args_2[] = { "-r" };

    long priority_3 = 0.00;
    char *path_3 = "/usr/bin/cp";
    char *args_3[] = { "-f" };

    Process *process_2 = Process_create(priority_2, path_2, (char **) args_2);
    Process *process_3 = Process_create(priority_3, path_3, (char **) args_3);

    Sched_push(sched, process);
    assert(sched->size == 1);

    Sched_push(sched, process_2);
    assert(sched->size == 2);

    Sched_push(sched, process_3);
    assert(sched->size == 3);

    assert(sched->head == process_2);
    assert(sched->tail == process_3);

    return SUCCESS;
}

int testPopProcessesFromSched() {
    long priority_2 = 20.0;
    char *path_2 = "/usr/bin/cp";
    char *args_2[] = { "-r" };

    Process *process_2 = Process_create(priority_2, path_2, (char **) args_2);

    Sched_push(sched, process);
    Sched_push(sched, process_2);

    Process *pop_process_1 = Sched_pop(sched);

    assert(sched->size == 1);

    Process *pop_process_2 = Sched_pop(sched);

    assert(sched->size == 0);
    assert(!sched->head);
    assert(!sched->tail);

    assert(pop_process_1 == process_2);
    assert(pop_process_2 == process);


    return SUCCESS;
}

int testPopProcessesAttributesFromSched() {
    long priority_2 = 20.0;
    char *path_2 = "/usr/bin/cp";
    char *args_2[] = { "-r" };

    Process *process_2 = Process_create(priority_2, path_2, (char **) args_2);

    Sched_push(sched, process);
    Sched_push(sched, process_2);

    Process *pop_process_1 = Sched_pop(sched);
    Process *pop_process_2 = Sched_pop(sched);

    assert(pop_process_1->priority == priority_2);
    assert(pop_process_2->priority == EXAMPLE_PRIORITY);

    assert(strcmp(pop_process_1->path, path_2) == 0);
    assert(strcmp(pop_process_2->path, EXAMPLE_PATH) == 0);

    assert(pop_process_1->args == (char **) args_2);
    assert(pop_process_2->args == (char **) EXAMPLE_ARGS);

    return SUCCESS;
}

int testPushPopProcessSched() {
    long priority_2 = 20.0;
    char *path_2 = "/usr/bin/cp";
    char *args_2[] = { "-r" };

    Process *process_2 = Process_create(priority_2, path_2, (char **) args_2);

    Sched_push(sched, process);
    Sched_pop(sched);

    Sched_push(sched, process_2);
    assert(sched->size == 1);
    assert(sched->head == process_2);
    assert(sched->tail == process_2);

    Sched_push(sched, process);
    assert(sched->size == 2);
    assert(sched->head == process_2);
    assert(sched->tail == process);
 
    Sched_pop(sched);
    assert(sched->size == 1);
    assert(sched->head == process);
    assert(sched->tail == process);

    return SUCCESS;

}

int testPushNullProcessToSched() {
    Sched_push(sched, NULL);

    assert(errno == EINVAL);
    assert(sched->size == 0);
    assert(!sched->head);

    return SUCCESS;
} 

int testPushProcessToNullSched() {
    Sched_push(NULL, process);

    assert(errno == EINVAL);
    assert(sched->size == 0);
    assert(!sched->head);

    return SUCCESS;
} 

int testPopProcessFromNullSched() {
    Process *pop_process = Sched_pop(NULL);

    assert(!pop_process);
    assert(errno == EINVAL);

    return SUCCESS;
} 

int testPopEmptySched() {
    Process* pop_process = Sched_pop(sched);

    assert(!pop_process);
    assert(sched->size == 0);

    return SUCCESS;
}

int testWaitForProcess() {

    Process_setup(process);
    Process_run(process);
    Sched_wait(process);

    assert(process->state == TERMINATED);
    assert(errno == 0);

    return SUCCESS;
}

int testAgeProcessIncrementsRunCount() {
    Process_age(process);
    assert(process->runCount == 1);
    return SUCCESS;
}

int testAgeProcessAboveThresholdResetsRunCount() {
    for (int i = 0; i < CYCLES_BEFORE_AGEING; i++) {
        Process_age(process);
    }
    assert(process->runCount == 0);
    return SUCCESS;
}

int testAgeProcessBelowThresholdMaintainsPriority() {
    Process_age(process);

    assert(process->priority == EXAMPLE_PRIORITY);

    return SUCCESS;
}

int testAgeProcessAboveThresholdDecrementsPriority() {
    Process_age(process);
    Process_age(process);
    Process_age(process);
    Process_age(process);
    Process_age(process);

    assert(process->priority == EXAMPLE_PRIORITY - 1);

    return SUCCESS;
}

int testRunSingleProcessSched() {
    Sched_admit(sched, process);
    
    Sched_run(sched);
    assert(Sched_isEmpty(sched));
    assert(process->state == TERMINATED);

    return SUCCESS;
}

int testRunTwoProcessSched() {
    Process *process_2 = Process_create(
            EXAMPLE_PRIORITY+3, EXAMPLE_PATH, (char**) EXAMPLE_ARGS);

    Sched_admit(sched, process);
    Sched_admit(sched, process_2);

    Sched_run(sched);

    assert(Sched_isEmpty(sched));
    assert(process->state == TERMINATED);
    assert(process_2->state == TERMINATED);

    return SUCCESS;
}

int testRunMultipleProcessSched() {
    Process *process_2 = Process_create(
            EXAMPLE_PRIORITY+3, EXAMPLE_PATH, (char**) EXAMPLE_ARGS);
    Process *process_3 = Process_create(
            EXAMPLE_PRIORITY+1, EXAMPLE_PATH, (char**) EXAMPLE_ARGS);

    Sched_admit(sched, process);
    Sched_admit(sched, process_2);
    Sched_admit(sched, process_3);

    Sched_run(sched);

    assert(Sched_isEmpty(sched));
    assert(process->state == TERMINATED);
    assert(process_2->state == TERMINATED);
    assert(process_3->state == TERMINATED);

    return SUCCESS;
}

int main() {
    runTest(&testPushProcessToSched);
    runTest(&testPushIncrementsSchedSize);

    runTest(&testPopProcessFromSched);
    runTest(&testPopDecrementsSchedSize);

    runTest(&testPushProcessesToSched);
    runTest(&testPopProcessesFromSched);
    runTest(&testPopProcessesAttributesFromSched);
    runTest(&testPushPopProcessSched);

    runTest(&testWaitForProcess);

    runTest(&testAgeProcessIncrementsRunCount);
    runTest(&testAgeProcessBelowThresholdMaintainsPriority);
    runTest(&testAgeProcessAboveThresholdResetsRunCount);
    runTest(&testAgeProcessAboveThresholdDecrementsPriority);

    runTest(&testRunSingleProcessSched);
    runTest(&testRunTwoProcessSched);
    runTest(&testRunMultipleProcessSched);

    printf("-------------------------------\n");
    printf("%d / %d Functionality tests succssful\n\n", success_count, test_count);
    fflush(stdout);

    success_count = 0;
    test_count = 0;

    runTest(testPushNullProcessToSched);
    runTest(testPushProcessToNullSched);
    runTest(testPopProcessFromNullSched);

    runTest(&testPopEmptySched);

    printf("-------------------------------\n");
    printf("%d / %d Edge-case tests succssful\n", success_count, test_count);
    fflush(stdout);
}

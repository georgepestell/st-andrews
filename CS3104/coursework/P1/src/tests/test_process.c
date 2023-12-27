#include "tests.c"

int testCreateProcess() {
    assert(process);
    return SUCCESS;
}

int testCreateProcessDefaultAttributes() {
    assert(!process->next);
    assert(process->pid == DEFAULT_NEW_PID);
    assert(process->state == NEW);
    assert(process->runCount == 0);
    return SUCCESS;
}

int testCreateProcessSetsAttributes() {
    assert(process->priority == EXAMPLE_PRIORITY);
    assert(strcmp(process->path, EXAMPLE_PATH) == 0);
    assert(process->args == (char **)EXAMPLE_ARGS);
    return SUCCESS;
}

int testSetProcessPriority() {
    return SUCCESS;
}

int testSetProcessPriorityTooHigh() {
    Process_setPriority(process, MAX_PRIORITY + 10);
    assert(process->priority == MAX_PRIORITY);
    return SUCCESS;
}
int testSetProcessPriorityTooLow() {
    Process_setPriority(process, MIN_PRIORITY - 32);
    assert(process->priority == MIN_PRIORITY);
    return SUCCESS;
}

int testCreateProcess_nullPath() {
    Process* new_process = Process_create(EXAMPLE_PRIORITY, NULL, (char **) EXAMPLE_ARGS);
    
    assert(!new_process);
    assert(errno == EINVAL);

    return SUCCESS;
}
int testCreateProcess_nullArgs() {
    Process *new_process = Process_create(EXAMPLE_PRIORITY, EXAMPLE_PATH, NULL);
    
    assert(!new_process);
    assert(errno == EINVAL);

    return SUCCESS;
}

int testSetupProcess() {
    Process_setup(process);

    assert(process->pid != DEFAULT_NEW_PID);
    assert(process->state = READY);
    assert(errno == 0);
    
    return SUCCESS;

}

int testRunProcess() {

    Process_setup(process);
    Process_run(process);

    assert(process->state == RUNNING);
    assert(errno == 0);

    return SUCCESS;
}


int testStopProcess() {
    Process_setup(process);
    Process_run(process);
    Process_stop(process);

    assert(process->state == READY);
    assert(errno == 0);

    return SUCCESS;
}

int main() {
    runTest(&testCreateProcess);
    runTest(&testCreateProcessDefaultAttributes);
    runTest(&testCreateProcessSetsAttributes);
    runTest(&testSetProcessPriority);


    printf("-------------------------------\n");
    printf("%d / %d General functionality tests succssful\n\n", success_count, test_count);
    fflush(stdout);

    test_count = 0;
    success_count = 0;

    runTest(&testSetProcessPriorityTooHigh);
    runTest(&testSetProcessPriorityTooLow);

    printf("-------------------------------\n");
    printf("%d / %d Edge case tests succssful\n\n", success_count, test_count);
    fflush(stdout);
}

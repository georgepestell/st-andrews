#include "tests.c"

int testCreateSched() {
    assert(sched);
    return SUCCESS;
}

int testCreateSchedDefaultAttributes() {
    assert(sched->size == 0);
    assert(!sched->head);
    assert(!sched->tail);
    return SUCCESS;
}

int testSchedIsEmpty() {
    assert(Sched_isEmpty(sched));
    return SUCCESS;
}

int testEmptySched() {
    Sched_empty(sched);
    assert(Sched_isEmpty(sched));
    return SUCCESS;
}

int testDestroySched() {
    Sched *s = Sched_create();

    Sched_destroy(s);
    assert(errno == 0);
    return SUCCESS;
}

int testPopEmptySched() {
    Process *p = Sched_pop(sched); 

    assert(!p);
    assert(sched->size == 0);
    assert(Sched_isEmpty(sched));

    return SUCCESS;
}


int main() {
    runTest(&testCreateSched);
    runTest(&testCreateSchedDefaultAttributes);

    runTest(&testSchedIsEmpty);
    runTest(&testEmptySched);

    runTest(&testDestroySched);

    printf("-------------------------------\n");
    printf("%d / %d Functionality tests succssful\n\n", success_count, test_count);
    fflush(stdout);

    success_count = 0;
    test_count = 0;

    runTest(&testPopEmptySched);

    printf("-------------------------------\n");
    printf("%d / %d Edge-case tests succssful\n", success_count, test_count);
    fflush(stdout);

}

#include "tests.c"

#include "../parser.c"
#include "../sched_rr.c"

int testParseProcessConfigNoArgs() {
    Process *p = parseProcessConfig("10 /usr/bin/ls");

    assert(p->priority == 10);
    assert(strcmp(p->path, "/usr/bin/ls") == 0);
    assert(!p->args[1]);

    return SUCCESS;
}

int testParseProcessConfigOneArg() {
    Process *p = parseProcessConfig("8 /usr/bin/ls -a");

    assert(p->priority == 8);
    assert(strcmp(p->path, "/usr/bin/ls") == 0);
    assert(strcmp(p->args[1], "-a") == 0);
    assert(!p->args[2]);

    return SUCCESS;
}

int testParseProcessConfigManyArgs() {

    Process *p = parseProcessConfig("2 /usr/bin/ls -a -b -c -d -e -f -g");

    assert(p->priority == 2);
    assert(strcmp(p->path, "/usr/bin/ls") == 0);
    assert(strcmp(p->args[7], "-g") == 0);
    assert(!p->args[8]);
    return SUCCESS;
}

int testParseScheduleConfigCreatesSchedule() {
    Sched *s = parseScheduleConfig("./configs/single-line.conf");
    assert(s);
    assert(s->size == 1);
    return SUCCESS;
}
int testParseScheduleConfigCreatesProcess() {
    Sched *s = parseScheduleConfig("./configs/single-line.conf");
    Process *p = Sched_pop(s);
    assert(p);
    return SUCCESS;
}
int testParseScheduleSetsUpProcess() {
    Sched *s = parseScheduleConfig("./configs/single-line.conf");
    Process *p = Sched_pop(s);
    assert(p);
    assert(p->state == READY);
    return SUCCESS;
}

int testParseScheduleMultiProcessConfig() {
    Sched *s = parseScheduleConfig("./configs/multi-line.conf");

    assert(s->size == 4);

    while (!Sched_isEmpty(s)) {
        Process *p = Sched_pop(s);
        assert(p->state == READY);
    }

    return SUCCESS;
}

int testParseProccesConfigPriorityTooLow() {
    Process *p = parseProcessConfig("-10 /usr/bin/ls");

    assert(p);
    assert(p->priority == MIN_PRIORITY);
    return SUCCESS;
}

int testParseProccesConfigPriorityTooHigh() {
    Process *p = parseProcessConfig("30 /usr/bin/ls");

    assert(p);
    assert(p->priority == MAX_PRIORITY);
    return SUCCESS;
}

int testParseProcessConfigEmpty() {
    Process *p = parseProcessConfig("");
    assert(!p);
    return SUCCESS;
}

int testParseProcessConfigNull() {
    Process *p = parseProcessConfig(NULL);
    assert(!p);
    return SUCCESS;
}

int testParseScheduleConfigEmpty() {
    Sched *s = parseScheduleConfig("./configs/empty.conf");
    assert(!s);
    return SUCCESS;
}

int testParseScheduleConfigEmptyLine() {
    Sched *s = parseScheduleConfig("./configs/empty-line.conf");
    assert(s);
    assert(s->size == 3);
    return SUCCESS;
}

int testParseScheduleConfigNoFile() {
    Sched *s = parseScheduleConfig("./configs/doesntexist");
    assert(!s);
    return SUCCESS;
}

int testParseScheduleConfigNull() {
    Sched *s = parseScheduleConfig(NULL);
    assert(!s);
    return SUCCESS;
}

int main() {

    runTest(&testParseProcessConfigNoArgs);
    runTest(&testParseProcessConfigOneArg);
    runTest(&testParseProcessConfigManyArgs);

    runTest(&testParseScheduleConfigCreatesSchedule);
    runTest(&testParseScheduleConfigCreatesProcess);
    runTest(&testParseScheduleSetsUpProcess);
    runTest(&testParseScheduleMultiProcessConfig);

    printf("-------------------------------\n");
    printf("%d / %d General functionality tests succssful\n", success_count, test_count);
    fflush(stdout);

    test_count = 0;
    success_count = 0;

    runTest(&testParseProccesConfigPriorityTooLow);
    runTest(&testParseProccesConfigPriorityTooHigh);
    runTest(&testParseProcessConfigNull);
    runTest(&testParseProcessConfigEmpty);
    
    runTest(&testParseScheduleConfigNull);
    runTest(&testParseScheduleConfigEmpty);
    runTest(&testParseScheduleConfigEmptyLine);
    runTest(&testParseScheduleConfigNoFile);

    printf("-------------------------------\n");
    printf("%d / %d Edge-case tests succssful\n", success_count, test_count);
    fflush(stdout);
}


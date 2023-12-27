#include "tests.c"

#include "../parser.c"

// CHANGE THIS TO TEST DIFFERENT SCHEDULERS
#include "../sched_priority.c"

void testTimeToCompleteHighPriorityItem() {
    Sched *sched = parseScheduleConfig("./configs/low-priorities.conf");

    Process *target = parseProcessConfig("20 ./printchars ! 20");
    
    Sched_admit(sched, target);

    while (!Sched_isEmpty(sched)) {
        Sched_cycle(sched);
        
        if (target->state == TERMINATED) {
            break;
        }
    }
}

void testAverageWaitTime() {
    Sched *sched = parseScheduleConfig("./configs/chars.conf");
    Sched_run(sched);
}

int main () {
    // testTimeToCompleteHighPriorityItem();
    testAverageWaitTime();
}

#include "sched.c"
#include "parser.c"
#include "process.c"

#include "sched_priority.c"

// #define runScheduler(sched) runRRScheduler(sched)

int main(int argc, char **argv)
{
    // Make sure a config_file parameter was given
    if (argc != 2) {
        printf("usage: ./sched <config_file>\n");
        exit(1);
    }
    
    Sched *sched = parseScheduleConfig(argv[1]);

    if (!sched) {
        exit(1);
    }

    Sched_run(sched);

    Sched_destroy(sched);
}

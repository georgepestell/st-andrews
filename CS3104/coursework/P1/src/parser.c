#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "sched.h"

const char *CONFIG_DELIM = " ";
const int ARGS_ARRAY_BLOCK_SIZE = 4;

Process *parseProcessConfig(char *config_string) {
    if (!config_string) {
        fprintf(stderr, "Cannot parse NULL process config\n");
        return NULL;
    }

    char *config = strdup(config_string);

    char* priority_string = strtok(config, CONFIG_DELIM);

    if (!priority_string) {
        fprintf(stderr, "Config line invalid: '%s'\n", config);
        return NULL;
    }

    int priority = (int) strtol(priority_string, NULL, 10);

    char *path = strtok(NULL, CONFIG_DELIM);

    if (!path) {
        fprintf(stderr, "Config line invalid: '%s'\n", config);
        return NULL;
    }

    char **args = malloc(sizeof(char *) * (ARGS_ARRAY_BLOCK_SIZE + 1));
    if (args == NULL) {
        perror("Malloc failed creating args array");
        return NULL;
    }

    args[0] = strdup(path);

    char *arg; 
    int argc = 1;
    while ((arg = strtok(NULL, CONFIG_DELIM))) {
        // Expand array when only 1 space is available for final NULL
        if ((argc % ARGS_ARRAY_BLOCK_SIZE) == 0) {
            args = realloc(args, sizeof(char *) * 
                    (argc + ARGS_ARRAY_BLOCK_SIZE + 1));
            if (!args) {
                    perror("Realloc failed resizing args");
                    free(args);
                    return NULL;
            }
        }
        args[argc++] = strdup(arg);
    }

    args[argc++] = NULL;

    return Process_create(priority, path, args);
}

Sched *parseScheduleConfig(char* config_path) {
    if (!config_path) {
        fprintf(stderr, "Cannot parse NULL config_path\n");
        return NULL;
    }

    FILE *config_file;
    if ((config_file = fopen(config_path, "r")) == NULL) {
        perror("Couldn't open file");
        return NULL;
    }

    Sched *sched = Sched_create();

    char *line = NULL;
    size_t bufsize = 32; 
    size_t lineLength = 0;
    while ((lineLength = getline(&line, &bufsize, config_file)) != -1) {

        line[lineLength-1] = '\0';

        Process *process = parseProcessConfig(line);

        if (process) {
            Sched_admit(sched, process);
        }

    }

    if (Sched_isEmpty(sched)) {
        fprintf(stderr, "No valid schedule config lines\n");
        Sched_destroy(sched);
        return NULL;
    }

    fclose(config_file);

    return sched;
}


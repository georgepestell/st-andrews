// This is a simple loop which will print some chars
// It is a useful way to test that the scheduler is working

#include <stdio.h>
#include <stdlib.h>
#include <limits.h>
#include <unistd.h>

int main(int argc, char** argv) 
{
    char c;
    int count;

    if(argc < 2) c = '*';
    else c = *argv[1];

    if(argc < 3) count = 30;
    else count = (int) strtol(argv[2], NULL, 10);
    

    for(int i=1; i<count; i++)
    {
        printf("%c",c);
        fflush(stdout);
        usleep(100000);
    }
}

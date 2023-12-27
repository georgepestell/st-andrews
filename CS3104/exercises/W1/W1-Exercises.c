#include <stdio.h>
#include <stdbool.h>
#include <malloc.h>
#include <string.h>

/* W01 Exercises */

/* 1. Data Types
 *
 * a. Data type bytes
 *
 *  char    = 1 x
 *  bool    = 1 x
 *  short   = 2 x
 *  int     = 1 - (4)
 *  long    = 2 - (8)
 *  float   = 4 x
 *  double  = 8 x
 *  void*   = 8 x
 *
 * b. data type example bytes
 *
 * 'c'      = 1 ?
 * 12       = 4 ?
 * O12      = 8 ?
 * 0x12     = 4 ?
 * 121      = 4 ?
 * -12      = 4 ?
 * 12.0     = 4 ?
 * 12f      = 4 ?
 *
 * 2. Pointers
 *
 * a. 
 * 
 * ptr = pointer to numbers array   x
 * *ptr = 1                         x
 * cptr = pointer to numbers array  x
 * *cptr = '1'                      x
 *
 * ptr[0] = 1                       x
 * ptr[2] = undefined               x
 * cptr[0] = 1                      x
 * cptr[1] = undefined              x
 *
 * 3. Logical operators
 *
 * a. 0 x
 * b. 1 x
 * c. 0 x
 * d. 1 x
 * e. 1 x
 * f. 1 x
 * g. 1 x
 * h. 3 x
 *
 */

void exerciseOneA() {
    printf("%s:\t%lu\n", "char", sizeof(char));
    printf("%s:\t%lu\n", "bool", sizeof(bool));
    printf("%s:\t%lu\n", "short", sizeof(short));
    printf("%s:\t%lu\n", "int", sizeof(int));
    printf("%s:\t%lu\n", "long", sizeof(long));
    printf("%s:\t%lu\n", "float", sizeof(float));
    printf("%s:\t%lu\n", "double", sizeof(double));
    printf("%s:\t%lu\n", "void*", sizeof(void*));
    printf("\n");
    return;
}

void exerciseOneB() {
    return;
}

void exerciseTwo() {
    int numbers[] = { 1 , 2 };
    int *ptr = numbers;
    char *cptr = (char*) numbers;

    printf("ptr =\t%p\n", ptr);
    printf("*ptr =\t%d\n", *ptr);
    printf("cptr =\t%p\n", cptr);
    printf("*cptr =\t%d\n", *cptr);
    printf("\n");

    printf("ptr[0] =\t%d\n", ptr[0]);
    printf("ptr[2] =\t%d\n", ptr[2]);
    printf("cptr[0] =\t%d\n", cptr[0]);
    printf("cptr[1] =\t%d\n", cptr[1]);

    printf("\n");
    return;
}

void exerciseThree() {

    int a = 1;

    printf("%d\n", 12 && 0);
    printf("%d\n", 12 || 0);
    printf("%d\n", 12 && NULL);
    printf("%d\n", a);
    printf("%d\n", 1 && a--);
    printf("%d\n", 1 && ++a);
    printf("%d\n", 1 || a--);
    printf("%d\n", 2 | 1);

    printf("\n");
    return;
}

char* getStringInput(int maxSize) {
    int inputStringSize = 0;
    
    char* inputString;
    inputString = (char *) malloc(maxSize);

    char inputChar;
    while (inputStringSize <= maxSize - 1) {
        inputChar = fgetc(stdin);
        if (inputChar == '\n') break;
        inputString[inputStringSize++] = inputChar;
    }

    inputString[inputStringSize++] = '\0';

    return inputString;
    
}

void exerciseFour() {
    int MAX_STRING_SIZE = 15;

    printf("ENTER STRING A: ");
    char* inputStringA = getStringInput(MAX_STRING_SIZE);

    printf("ENTER STRING B: ");
    char* inputStringB = getStringInput(MAX_STRING_SIZE);


    if (strcmp(inputStringA, inputStringB) == 0) printf("Equal\n");
    else printf("Unequal\n");
    

    free(inputStringA);
    free(inputStringB);

    printf("\n");
    return;
}

typedef struct Cat {
    char* name;
    int size;
    float weight;
    void* previous;
    void* next;
} Cat;

void printCatInfo(Cat* cat) {
    printf("Name: %s\nSize: %d\nWeight: %.2f\n", cat->name, cat->size, cat->weight);
}

void exerciseFive() {
   Cat holly = { "Holly", 15, 14.5, NULL, NULL};
   Cat flynn = { "Flynn", 16, 17.6, &holly, NULL };
   Cat digger = { "Digger", 19, 22.6, &flynn, &holly};

   holly.previous = &digger;
   holly.next = &flynn;
   flynn.next = &digger;

   Cat* current = &holly;
   printCatInfo(current);

   current = current->next;
   printCatInfo(current);

   current = current->next;
   printCatInfo(current);

   current = current->next;
   printCatInfo(current);

   printf("\n");
   return;
}

void exerciseSix() {
    char STRING_CONSTANT[100] = "hello there my name is George!";

    char *token = strtok(STRING_CONSTANT, " ");
    while (token != NULL) {
      printf("%s\n", token);
      token = strtok(NULL, " ");
    }

    printf("\n");
    return;
}

int main(int argc, char *argv[]) {
    exerciseOneA();
    exerciseOneB();
    exerciseTwo();
    exerciseThree();
    exerciseFour();
    exerciseFive();
    exerciseSix();

}

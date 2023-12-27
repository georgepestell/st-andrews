#include "collatz.h"

static int array_length;
static int *array;
static int mult;
static int add;
static int div; 

static bool collatz_recurse(int current) {

    if( array_length >= MAXARRAYSIZE) {
        return false; }

    array[array_length] = current;

    array_length += 1;

    if (current == 1) { 
        return true; } 
     
    if (current%div == 0) { 
        return collatz_recurse(current/div); }

    // we must be in the case where we multiply
    // but would we get overflow?
  
    if  (current > (INT_MAX-add)/mult) { 
        return false; }

    return collatz_recurse(current*mult + add);


}



bool collatz(int start, int in_mult, int in_add, int in_div, int *in_length, int* in_array) { 

     array_length = 0;
     mult = in_mult;
     add = in_add;
     div = in_div;
     array = in_array;

     bool result = false;

     if(start >= 1) { 
         result = collatz_recurse(start);
     }
     *in_length=array_length;
     return result;
}

     




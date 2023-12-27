/**
 * Following code taken/adapted from myassert.h resource supplied for 
 * CS2002 Practical-C2 (W07)
 *
 * author: jonl
 *
 */

// -- BEGIN CODE --

#ifndef _ASSERT
#define _ASSERT

#define assert(expression) \
    if (!(expression)) { \
        printf("Assertion failed in test: %s (%s:%d)\n",  \
                __func__, __FILE__, __LINE__); \
        return FAILED; \
    }
#endif

// -- END CODE --

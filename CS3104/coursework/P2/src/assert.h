/**
 * Following code taken/adapted from myassert.h resource supplied for 
 * CS2002 Practical-C2 (W07)
 *
 * author: jonl
 *
 */

// -- BEGIN CODE --

enum TEST_STATUS {
  FAILED,
  SUCCESS
};

#ifndef _ASSERT
#define _ASSERT

#define assert(expression) \
    if (!(expression)) { \
        printf("x : %s (%s:%d)\n",  \
                __func__, __FILE__, __LINE__); \
        return FAILED; \
    }
#endif

// -- END CODE --

#define SUCCESS() \
  printf("✓ : %s\n", __func__); \
  return SUCCESS;

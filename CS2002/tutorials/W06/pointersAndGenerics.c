#include <stdio.h>

int main() {
  int i = 1;
  int * ip = &i;
  int** ip2 = &ip;
  void ** vp = ip2;
  void* vp2 = vp;

  int i2 = **(int**)vp2;

  printf("%i", i2);
}


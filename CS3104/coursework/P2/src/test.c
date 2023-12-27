#include "paging.h"
#include "assert.h"
#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <errno.h>

const size_t DEFAULT_PHYSICAL_MEMORY_BITS=15;

int success_count = 0;
int test_count = 0;

void *table, *store;
size_t physical_memory_bits = DEFAULT_PHYSICAL_MEMORY_BITS;
int page_count;

void setup() {
  errno = 0;
  page_count = 1<<(physical_memory_bits - OFFSET_BITS);
  store = malloc(1<<physical_memory_bits);
  table = pt_init();
}

void cleanup() {
  free(store);
  free(table);
  physical_memory_bits = DEFAULT_PHYSICAL_MEMORY_BITS;
}

void runTest(int test()) {
  setup();

  if (test() == SUCCESS) { 
    success_count++;
  }
  test_count++;

  cleanup();
}

int testPageTableInitializeValuesToZero() {
  for (int i = 0; i < PAGETABLE_ROWS; i++) {
    PageEntry pageEntry = ((PageEntry *) table)[i];

    assert(pageEntry.frame_number == 0);
    assert(pageEntry.readonly == 0);
    assert(pageEntry.executable == 0);
    assert(pageEntry.valid == 0);

  }
  SUCCESS();
}

int testMapPageToFrameSetsValues() {
  int pageNumber = 1;
  int frameNumber = 11;
  bool readonly = 1;
  bool executable = 1;

  map_page_to_frame(table, pageNumber, frameNumber, readonly, executable);

  PageEntry pageEntry = ((PageEntry *) table)[pageNumber];

  assert(errno == 0);

  assert(pageEntry.frame_number == frameNumber);
  assert(pageEntry.readonly == readonly);
  assert(pageEntry.executable == executable);
  assert(pageEntry.valid);

  SUCCESS();
}

int testMapPageToFrameNullTable() {
  int pageNumber = 2;
  map_page_to_frame(NULL, pageNumber, 12, 0, 1);

  PageEntry pageEntry = ((PageEntry *)table)[pageNumber];

  assert(errno == EINVAL);
  assert(!pageEntry.valid)

  SUCCESS();
}

int testMapPageNumberTooHigh() {
  int pageNumber = PAGETABLE_ROWS + 1;
  map_page_to_frame(table, pageNumber, 12, 0, 1);
  
  assert(errno == EINVAL);

  SUCCESS();
}

int testMapFrameNumberTooHigh() {
  int frameNumber = PAGETABLE_ROWS + 1;
  map_page_to_frame(table, 12, frameNumber, 0, 1);
  
  assert(errno == EINVAL);

  SUCCESS();
}

int testVirtualToPhysicalZero() {

  int frameNumber = 0;
  int pageNumber = 0;
  int offset = 0;
  
  uint16_t virtualAddress = 0; 

  map_page_to_frame(table, pageNumber, frameNumber, 0, 0);

  uint16_t physicalAddress = virtual_to_physical(table, virtualAddress);

  assert(errno == 0);
  assert(physicalAddress == 0);

  SUCCESS();
}

int testVirtualToPhysicalNonZeroPage() {

  int frameNumber = 127;
  int pageNumber = 65;
  int offset = 0;
  
  uint16_t virtualAddress = (pageNumber << OFFSET_BITS) + offset;

  map_page_to_frame(table, pageNumber, frameNumber, 0, 0);

  uint16_t physicalAddress = virtual_to_physical(table, virtualAddress);

  assert(errno == 0);
  assert((physicalAddress >> OFFSET_BITS) == frameNumber);
  assert((physicalAddress & OFFSET_MASK) == offset);

  SUCCESS();
}

int testVirtualToPhysicalNonZeroOffset() {

  int frameNumber = 127;
  int pageNumber = 65;
  int offset = 17;
  
  uint16_t virtualAddress = (pageNumber << OFFSET_BITS) + offset; 

  map_page_to_frame(table, pageNumber, frameNumber, 0, 0);

  uint16_t physicalAddress = virtual_to_physical(table, virtualAddress);
  
  assert(errno == 0);

  assert((physicalAddress >> OFFSET_BITS) == frameNumber);
  assert((physicalAddress & OFFSET_MASK) == offset);

  SUCCESS();
}

int testVirtualToPhysicalNullTable() {

  int frameNumber = 127;
  int pageNumber = 65;
  int offset = 17;
  
  uint16_t virtualAddress = (pageNumber << OFFSET_BITS) + offset; 

  map_page_to_frame(table, pageNumber, frameNumber, 0, 0);

  uint16_t physicalAddress = virtual_to_physical(NULL, virtualAddress);

  assert(errno == EINVAL);
  assert(physicalAddress == 0);
  SUCCESS();
}

int testVirtualToPhysicalNotMapped() {
  int frameNumber = 127;
  int pageNumber = 65;
  int offset = 17;
  
  uint16_t virtualAddress = (pageNumber << OFFSET_BITS) + offset; 
  uint16_t physicalAddress = virtual_to_physical(table, virtualAddress);

  assert(errno != 0);
  assert(physicalAddress == 0);

  SUCCESS();
}

int testStoreSmallData() {
  int pageNumber = 65;
  int frameNumber = 127;
  int offset = 17;

  map_page_to_frame(table, pageNumber, frameNumber, 0, 0);

  char text[] = "Feel the rhythm, feel the rhyme.";
  int length = sizeof(text);

  uint16_t virtualAddress = (pageNumber << OFFSET_BITS) + offset; 

  store_data(table, store, text, virtualAddress, length);

  SUCCESS();
}

int testStoreLargeData() {
  int pageNumber = 65;
  int frameNumber = 127;
  int offset = 0;

  map_page_to_frame(table, pageNumber, frameNumber, 0, 0);

  char text[] = "jesus, don't cry. you can rely on me honey. you can combine anything you want. i'll be around. you were right about the stars.";
  int length = strlen(text);

  uint16_t virtualAddress = (pageNumber << OFFSET_BITS) + offset; 

  store_data(table, store, text, virtualAddress, length);

  SUCCESS();
}

int testReadSmallData() {
  int pageNumber = 65;
  int frameNumber = 127;
  int offset = 17;

  map_page_to_frame(table, pageNumber, frameNumber, 0, 0);

  char text[] = "Feel the rhythm, feel the rhyme.";
  int length = sizeof(text);

  uint16_t virtualAddress = (pageNumber << OFFSET_BITS) + offset; 

  store_data(table, store, text, virtualAddress, length);

  char* buffer = malloc(length + 1);
  read_data(table, store, buffer, virtualAddress, length);

  assert(strcmp(buffer, text) == 0);

  free(buffer);

  SUCCESS();
}

int testReadLargeData() {
  int pageNumber = 65;
  int frameNumber = 127;
  int offset = 0;

  map_page_to_frame(table, pageNumber, frameNumber, 0, 0);

  char text[] = "jesus, don't cry. you can rely on me honey. you can combine anything you want. i'll be around. you were right about the stars.";
  int length = strlen(text);

  uint16_t virtualAddress = (pageNumber << OFFSET_BITS) + offset; 

  store_data(table, store, text, virtualAddress, length);

  char* buffer = malloc(length + 1);
  read_data(table, store, buffer, virtualAddress, length);

  assert(strcmp(buffer, text) == 0);

  free(buffer);
  SUCCESS();
}

int testStoreInReadOnlyFrameFails() {
  int pageNumber = 65;
  int frameNumber = 127;
  int offset = 17;

  map_page_to_frame(table, pageNumber, frameNumber, 1, 1);

  char text[] = "jesus, don't cry. you can rely on me honey.";
  int length = sizeof(text);

  uint16_t virtualAddress = (pageNumber << OFFSET_BITS) + offset; 

  store_data(table, store, text, virtualAddress, length);

  assert(errno == EROFS);

  SUCCESS();
}

int testStoreAndReadDifferentTypes() { int pageNumber = 65;
  int frameNumber = 127;
  int offset = 0;

  map_page_to_frame(table, pageNumber, frameNumber, 0, 0);

  uint16_t virtualAddress;

  // Create text to store
  char text[] = "jesus, don't cry.";
  int textLength = strlen(text);

  // Create a number to store
  int* number = malloc(sizeof(uint16_t));
  *number = 424;
  uint16_t numberLength = sizeof(uint16_t);

  typedef struct NewObject { 
    char name[10];
    int age;
    bool isAlive;
  } NewObject; 

  // Create a new object to store
  NewObject* object = malloc(sizeof(NewObject));
  *object = (NewObject) { "George", 21, true };
  int objectLength = sizeof(NewObject);

  // Store the text
  virtualAddress = (pageNumber << OFFSET_BITS) + offset; 
  store_data(table, store, text, virtualAddress, textLength);
  offset+=textLength;

  // Store the number 
  virtualAddress = (pageNumber << OFFSET_BITS) + offset; 
  store_data(table, store, number, virtualAddress, numberLength);
  offset+=numberLength;
  
  // Store the object
  virtualAddress = (pageNumber << OFFSET_BITS) + offset; 
  store_data(table, store, object, virtualAddress, objectLength);

  // Reset offset to begin getting values again
  offset = 0;

  // Create buffers to read into
  char *textBuffer = malloc(textLength + 1);
  uint16_t *numberBuffer = malloc(numberLength);
  NewObject* objectBuffer = malloc(objectLength);

  // Read the text
  virtualAddress = (pageNumber << OFFSET_BITS) + offset; 
  read_data(table, store, textBuffer, virtualAddress, textLength);
  offset+=textLength;

  // Read the number
  virtualAddress = (pageNumber << OFFSET_BITS) + offset; 
  read_data(table, store, numberBuffer, virtualAddress, numberLength);
  offset+=numberLength;

  // Read the object
  virtualAddress = (pageNumber << OFFSET_BITS) + offset; 
  read_data(table, store, objectBuffer, virtualAddress, objectLength);

  // Check the different types stored correctly
  assert(strcmp(text, textBuffer) == 0);
  assert(*numberBuffer == *number);
  assert(strcmp(objectBuffer->name, object->name) == 0);
  assert(objectBuffer->age == object->age);
  assert(objectBuffer->isAlive == object->isAlive);

  SUCCESS();
}


int testStoreAndReadAllAddresses() {
  for (int i = 0; i < page_count; i++) {
    map_page_to_frame(table, i, i, false, false);

    size_t length = sizeof(i);
    int *buffer = malloc(length);
    *buffer = i;

    store_data(table, store, buffer, i << OFFSET_BITS, length);
  }

  for (int i = 0; i < page_count; i++) {
    size_t length = sizeof(i);
    int *buffer = malloc(length);
    read_data(table, store, buffer, i << OFFSET_BITS, length);
    assert(*buffer == i);
    free(buffer);
  }

  SUCCESS();
}

int testStoreAndReadMaxPhysicalSize() {
  cleanup();
  physical_memory_bits = 16;
  setup();

  for (int i = 0; i < page_count; i++) {
    map_page_to_frame(table, i, i, false, false);

    size_t length = sizeof(i);
    int *buffer = malloc(length);
    *buffer = i;

    store_data(table, store, buffer, i << OFFSET_BITS, length);
  }

  for (int i = 0; i < page_count; i++) {
    int *buffer = malloc(1);
    size_t length = sizeof(i);
    read_data(table, store, buffer, i << OFFSET_BITS, length);
    assert(*buffer == i);
    free(buffer);
  }

  SUCCESS();
}

int main() {

  runTest(testPageTableInitializeValuesToZero);

  runTest(testMapPageToFrameSetsValues);

  runTest(testMapPageToFrameNullTable);
  runTest(testMapPageNumberTooHigh);
  runTest(testMapFrameNumberTooHigh);

  runTest(testVirtualToPhysicalZero);
  runTest(testVirtualToPhysicalNonZeroPage);
  runTest(testVirtualToPhysicalNonZeroOffset);
  runTest(testVirtualToPhysicalNullTable);
  runTest(testVirtualToPhysicalNotMapped);

  runTest(testStoreSmallData);
  runTest(testStoreLargeData);

  runTest(testReadSmallData);
  runTest(testReadLargeData);

  runTest(testStoreAndReadDifferentTypes);

  runTest(testStoreInReadOnlyFrameFails);

  runTest(testStoreAndReadAllAddresses);
  runTest(testStoreAndReadMaxPhysicalSize);

  printf("%d / %d TESTS PASSED\n", success_count, test_count);
}

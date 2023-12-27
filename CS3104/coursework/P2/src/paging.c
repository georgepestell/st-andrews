#include "paging.h"
#include <stdlib.h>
#include <stdio.h>
#include <errno.h>

// Your implementation goes in here!

void* pt_init() {
  void* table = malloc(PAGETABLE_SIZE);
  for (int i = 0; i < PAGETABLE_ROWS; i++) {
    ((PageEntry *)table)[i] = (PageEntry) { 0, 0, 0, 0 };
  }

	return table;
}

uint16_t virtual_to_physical(void* table, uint16_t virtual_address) {
  if (!table) {
    fprintf(stderr, "ERROR: page table is NULL\n");
    errno = EINVAL;
    return 0;
  }

  uint16_t page_number = virtual_address >> OFFSET_BITS;
  uint16_t page_offset = virtual_address & OFFSET_MASK;

  if (page_number < 0 || page_number >= PAGETABLE_ROWS) {
    fprintf(stderr, "ERROR: Invalid virtual address (%d)\n", virtual_address);
    errno = EINVAL;
    return 0;
  }

  PageEntry page_entry = ((PageEntry *) table)[page_number];

  if (!page_entry.valid) {
    fprintf(stderr, "ERROR: Page (%d) invalid\n", page_number);
    errno = EFAULT;
    return 0;
  } 

  uint16_t frame_number = page_entry.frame_number;

  uint16_t physical_address = (frame_number << OFFSET_BITS) + page_offset;

  return physical_address;
}

void map_page_to_frame(void* table, uint16_t page_number, uint16_t frame_number, bool readonly, bool executable) {
  if (!table) {
    fprintf(stderr, "ERROR - Cannot map to NULL page table\n");
    errno = EINVAL;
    return;
  }

  if (page_number < 0 || page_number >= PAGETABLE_ROWS) {
    fprintf(stderr, "ERROR - Mapping: Invalid page number (%d)\n", page_number);
    errno = EINVAL;
    return;
  };

  if (frame_number < 0 || frame_number >= PAGETABLE_ROWS) {
    fprintf(stderr, "ERROR - Mapping: Invalid frame number (%d)\n", frame_number);
    errno = EINVAL;
    return;
  };

  ((PageEntry *) table)[page_number] = 
    (PageEntry) { frame_number, readonly, executable, 1 };

}

void print_table(void* table) {
  printf("page frame r e v\n");
  for (int i = 0; i < PAGETABLE_ROWS; i++) {
    PageEntry page_entry = ((PageEntry *) table)[i];
    if (page_entry.valid) { 
      printf("%0.3d  %0.3d   %d %d %d\n", i, page_entry.frame_number, page_entry.readonly, page_entry.executable, page_entry.valid);
    }
  }
}

void unmap_page(void* table, uint16_t page_number) {
  *((uint16_t *) table + page_number * PAGETABLE_SIZE) = 0;
}

void store_data(void* table, void* store, void* buffer, uint16_t virtual_address, size_t length) {

  uint16_t physical_address = virtual_to_physical(table, virtual_address);
  uint16_t frame = physical_address >> OFFSET_BITS;


  if ((physical_address+length) >> OFFSET_BITS != frame) {
    fprintf(stderr, "ERROR: Attempting to store data outside of frame\n");
    errno = ERANGE;
    return;
  }

  PageEntry page_entry = ((PageEntry *)table)[virtual_address >> OFFSET_BITS];

  if (page_entry.readonly) {
    fprintf(stderr, "ERROR: Cannot store data to readonly frame\n");
    errno = EROFS;
    return;
  } 

  for (int i = 0; i < length; i++) {
    ((char *) store)[physical_address + i] = ((char *)buffer)[i];
  }
}

void read_data(void* table, void* store, void* buffer, uint16_t virtual_address, size_t length) {

  uint16_t physical_address = virtual_to_physical(table, virtual_address);
  uint16_t frame = physical_address >> OFFSET_BITS;

  if ((physical_address+length) >> OFFSET_BITS != frame) {
    fprintf(stderr, "ERROR: Attempting to read outside of frame\n");
    errno = ERANGE;
    return;
  }


  for (int i = 0; i < length; i++) {
    ((char *) buffer)[i] = ((char *) store)[physical_address + i];
  }
}

#include "./lfuCache.hpp"

LFUCache::LFUCache(string name, uint64_t line_count, unsigned int line_size,
                   uint64_t set_size)
    : Cache(name, line_count, line_size, set_size) {

  // Initialize each lines useCount to 0
  // Calloc allows a large array to be created, all set to 0
  this->useCount = (uint *)calloc(line_count, sizeof(uint));
}

void LFUCache::replace(uint64_t index, uint64_t tag) {

  // As previously explained, find the set range
  uint64_t start_line = index * set_size;
  uint64_t end_line = start_line + set_size;

  // Loop through all set lines for the least frequent useCount

  uint64_t line = start_line;
  uint minUsage = this->useCount[start_line];
  for (uint64_t i = start_line; i < end_line; i++) {
    if (this->useCount[i] < minUsage) {
      line = i;
      minUsage = this->useCount[i];
    }

    // As with LRU, the valid bit check would only optimise for small trace
    // files, and slowed down search where the cache is full
  }

  // Replace the line
  this->store[line].tag = tag;
  this->store[line].valid = true;

  // Reset the use count of the line
  // Set to 1 as this is an access, and so should be greater than un-accessed
  // lines Allows not checking valid bits in searching for the minUsage
  this->useCount[line] = 1;
}

void LFUCache::registerHit(uint64_t line) {
  this->useCount[line]++;
  Cache::registerHit(line);
}

void LFUCache::cleanup() {
  free(this->useCount);
  Cache::cleanup();
}
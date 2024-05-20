#include "./rrCache.hpp"

RRCache::RRCache(string name, uint64_t line_count, unsigned int line_size,
                 uint64_t set_size)
    : Cache(name, line_count, line_size, set_size) {

  // Initialize each set's position value to 0
  this->pos = (uint *)calloc(set_count, sizeof(uint));
}

void RRCache::replace(uint64_t index, uint64_t tag) {

  // Find the set range
  uint64_t start_line = index * this->set_size;

  // Store the current position and increment (looping back)
  uint64_t line = start_line + this->pos[index];
  this->pos[index] = (this->pos[index] + 1) % this->set_size;

  // Replace the selected line
  this->store[line].tag = tag;
  this->store[line].valid = true;
}

void RRCache::cleanup() {
  free(this->pos);
  Cache::cleanup();
}
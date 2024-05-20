#include "./cache.hpp"
#include <stdexcept>

Cache::Cache(string name, uint64_t line_count, uint line_size,
             uint64_t set_size) {

  if (set_size > line_count) {
    throw invalid_argument("set_size cannot exceed line_count");
  }

  this->name = name;
  this->line_size = line_size;
  this->set_size = set_size;
  this->line_count = line_count;

  this->misses = 0;
  this->hits = 0;

  // Calculate the number of lines which fit into the storage
  // Rounded down as rounding up would mean the capacity would be exceeded
  // long double used for large caches
  this->set_count = ceil((long double)line_count / (long double)set_size);

  // Calculate the number of bits required to store the index
  // and offset Rounded up as the float value represents the
  // minimum number of bits to fully reference each words,
  // and so rounding down loses access to some words
  this->index_bits = ceil(log2((long double)set_count));
  this->offset_bits = ceil(log2((long double)line_size));
  this->offset_mask = exp2(offset_bits) - 1;
  this->index_mask = exp2(index_bits) - 1;

  // Make sure the memory addresses are large enough to store index and offset
  if (index_bits + offset_bits > MEMORY_ADDRESS_BITS) {
    throw range_error("Cache too large to be fully indexed by address size");
  }

  // Create the cache line array
  // Calloc used instead of array initializer for very large arrays, it also
  // sets bits to 0 (unlike malloc) which means lines are already initialized
  // invalid
  this->store = (CacheLine *)calloc(line_count, sizeof(CacheLine));
};

uint64_t Cache::getIndex(uint64_t addr) {
  return (addr >> this->offset_bits) & this->index_mask;
}

uint64_t Cache::getTagIndex(uint64_t addr) { return addr >> this->offset_bits; }

uint64_t Cache::getTag(uint64_t addr) {
  return (addr >> this->offset_bits) >> this->index_bits;
}

bool Cache::tryHit(uint64_t index, uint64_t tag) {

  // Get the range of lines referrenced by the index
  // Allows fully associative and n-mapped caches to act the same
  // as fully associative just means set sizes are 1
  uint64_t start_line = index * this->set_size;
  uint64_t end_line = start_line + this->set_size;

  for (uint64_t i = start_line; i < end_line; i++) {
    if (this->store[i].valid && this->store[i].tag == tag) {
      this->registerHit(i);
      return true;
    }
  }

  this->registerMiss();
  return false;
}

uint64_t Cache::getAddress(uint64_t tag, uint64_t index) {
  return ((tag << this->index_bits) + index) << this->offset_bits;
}

void Cache::registerHit(uint64_t line) { this->hits++; }
void Cache::registerMiss() { this->misses++; }

void Cache::cleanup() { free(this->store); }
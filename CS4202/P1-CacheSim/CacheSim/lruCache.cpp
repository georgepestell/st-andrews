#include "./lruCache.hpp"

LRUCache::LRUCache(string name, uint64_t line_count, unsigned int line_size,
                   uint64_t set_size)
    : Cache(name, line_count, line_size, set_size) {

  // Initialize the LRU cache vars
  // Calloc allows a large lastAccess array all set to 0
  this->lastAccess = (uint64_t *)calloc(line_count, sizeof(uint64_t));
  this->age = 0;
}

void LRUCache::replace(uint64_t index, uint64_t tag) {

  // Similar to the other cache types, the index points to a set, and so the set
  // range must be calculated and looped over
  uint64_t start_line = index * set_size;
  uint64_t end_line = start_line + set_size;

  // Search for the oldest line
  uint64_t line = start_line;
  uint64_t minLastAccess = this->lastAccess[start_line];
  for (uint64_t i = start_line + 1; i < end_line; i++) {
    uint64_t currLastAccess = this->lastAccess[i];

    // Get the line with the min lastAccess value, ties selecting the smallest
    // index (hence < not <=)
    if (currLastAccess < minLastAccess) {
      minLastAccess = currLastAccess;
      line = i;
    }

    /**
     * Here, it would be possible to break out of this loop if the line checked
     * is invalid, as they should be filled first, and this would save
     * processing lines whilst the cache is empty.
     *
     * However, as lines begin with lastAccess values 0, the first empty line
     * will always be chosen.
     *
     * Additionally, this skip only works whilst the cache is not full, but the
     * extra check reduced the speed of execution for larger trace files where
     * the cache was full most of the time.
     */
  }

  // Set the age of the line to the current age
  // Also increment the age
  this->lastAccess[line] = ++this->age;

  // Replace the line
  this->store[line].tag = tag;
  this->store[line].valid = true;
}

void LRUCache::registerHit(uint64_t line) {
  // Increment the cache age, and set the last access var of the hit line to
  // that age
  this->lastAccess[line] = ++this->age;
  Cache::registerHit(line);
}

void LRUCache::cleanup() {
  free(this->lastAccess);
  Cache::cleanup();
}
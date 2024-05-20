#include "./cacheParser.hpp"
#include <iostream>
#include <stdexcept>

Cache *CacheParser::parse(string config_path) {
  // Open config file
  ifstream configFile(config_path);

  // Check file successfully opened
  if (!configFile) {
    cerr << "Error: Failed to open config file \"" << config_path << "\"\n";
    exit(EXIT_FAILURE);
  }

  // Read the config file as JSON
  json caches;
  try {
    caches = json::parse(configFile).at("caches");
  } catch (json::out_of_range e) {
    configFile.close();
  }

  // DEBUG: Print the cache config
  // cerr << caches << endl;

  Cache *head = NULL;
  Cache *current = NULL;

  // Read caches from config. Given in order from highest to lowest level
  Cache *cache;
  for (auto &cacheJSON : caches) {
    string name = cacheJSON.at("name");
    uint64_t total_size = cacheJSON.at("size");
    uint line_size = cacheJSON.at("line_size");
    uint64_t line_count = total_size / line_size;

    uint64_t set_size = parseKindToSetSize(cacheJSON["kind"], line_count);

    // Direct cache replacement policy is redundant as lines always map to
    // specific lines
    if (cacheJSON["kind"] == "direct") {
      cache = new RRCache(name, line_count, line_size, set_size);
    } else {
      try {

        // Read the replacement policy if given, but default to round-robin if
        // ommitted. Each type is it's own class which are each children of the
        // Cache class. Allows calling the "replace" and "registerHit"
        // functions to be overriden.
        string cachePolicy = cacheJSON.at("replacement_policy");

        if (cachePolicy == "rr") {
          cache = new RRCache(name, line_count, line_size, set_size);
        } else if (cachePolicy == "lfu") {
          cache = new LFUCache(name, line_count, line_size, set_size);
        } else if (cachePolicy == "lru") {
          cache = new LRUCache(name, line_count, line_size, set_size);
        } else {
          configFile.close();
          throw invalid_argument("Invalid Replacement Policy");
        }
        // Default to Round Robin replacment scheme
      } catch (json::out_of_range e) {
        cache = new RRCache(name, line_count, line_size, set_size);
      }
    }

    // Create the new cache with the given properties and add to the hierarchy
    if (head == NULL) {
      head = cache;
    } else {
      current->next = cache;
    }
    current = cache;
  }

  // Cleanup - close config file
  configFile.close();

  return head;
}

uint64_t CacheParser::parseKindToSetSize(string kind_string,
                                         uint64_t line_count) {
  if (kind_string == "full") {
    return line_count;
  } else if (kind_string == "direct") {
    return 1;
  } else if (kind_string == "2way") {
    return 2;
  } else if (kind_string == "4way") {
    return 4;
  } else if (kind_string == "8way") {
    return 8;
  } else {
    throw invalid_argument("Invalid kind: \"" + kind_string + "\"");
  }
};

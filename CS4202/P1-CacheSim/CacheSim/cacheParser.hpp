#ifndef CACHEPARSER_H_
#define CACHEPARSER_H_

#include <fstream>
#include <iostream>
#include <list>
#include <string>
#include <tuple>

#include "./cache.hpp"
#include "./lfuCache.hpp"
#include "./lruCache.hpp"
#include "./rrCache.hpp"

#include <nlohmann/json.hpp>

using namespace std;
using json = nlohmann::json;

class CacheParser {
public:
  static Cache *parse(string config_path);

  static uint64_t parseKindToSetSize(string kind_string, uint64_t line_count);
};

#endif // CACHEPARSER_H_
#ifndef CMSET_H_
#define CMSET_H_

#include <atomic>
#include <map>
#include <mutex>

using namespace std;

/**
 * MultiSet allowing on the fly instantiation of sets for an arbitrary type
 */
template <class T> class CMSet {

private:
  // Use a map of elements to their multiplicity as atomic_int variables,
  // allowing mutliple threads to access each entry concurrently
  map<T, atomic_int> store;

public:
  bool contains(T element);
  int count(T element);
  void add(T element);
  bool remove(T element);
};

#endif // CMSET_H_
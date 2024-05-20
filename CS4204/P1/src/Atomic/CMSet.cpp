#include "./CMSet.hpp"

/**
 * Checks if an element is in the set
 * @returns true if element exists in set at least once, false otherwise
 */
template <class T> bool CMSet<T>::contains(T element) {
  try {

    // We don't need to lock here, because the access solely is done in count()
    int count = this->count(element);
    return count > 0;

  } catch (out_of_range) {
    return false;
  }
}

/**
 * Counts the number of a given element in the set
 * @returns element count in the set
 */
template <class T> int CMSet<T>::count(T element) {
  return this->store[element];
}

/**
 * Adds an element to the set
 */
template <class T> void CMSet<T>::add(T element) { ++this->store[element]; }

/**
 * Removes the first instance of a given element in the set if it exists
 * @arg element The element to remove from the set
 * @returns True if an element was removed. False if no instances were found
 */
template <class T> bool CMSet<T>::remove(T element) {
  int count = this->store[element];
  if (count <= 0) {
    return false;
  } else {
    this->store[element]--;
    return true;
  }
}

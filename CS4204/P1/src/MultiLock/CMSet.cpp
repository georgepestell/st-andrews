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
  mutex *lock = &this->locks[element];
  lock->lock();
  int count = this->store[element];
  lock->unlock();
  return count;
}

/**
 * Adds an element to the set
 */
template <class T> void CMSet<T>::add(T element) {

  // Check if the element is in the map
  typename map<T, mutex>::iterator lockIt = this->locks.find(element);

  mutex *lock;

  if (lockIt == this->locks.end()) {
    this->mutexMapLock.lock();
    // Fetch the lock referenced (creating a new one if first thread)
    lock = &this->locks[element];
    this->mutexMapLock.unlock();
  } else {
    lock = &lockIt->second;
  }

  lock->lock();
  this->store[element]++;
  lock->unlock();
}

/**
 * Removes the first instance of a given element in the set if it exists
 * @arg element The element to remove from the set
 * @returns True if an element was removed. False if no instances were found
 */
template <class T> bool CMSet<T>::remove(T element) {
  mutex *lock = &this->locks[element];
  lock->lock();
  int count = this->store[element];
  if (count <= 0) {
    lock->unlock();
    return false;
  } else {
    this->store[element]--;
    lock->unlock();
    return true;
  }
}

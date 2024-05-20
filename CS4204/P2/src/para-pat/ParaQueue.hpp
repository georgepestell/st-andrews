/**
 * @file ParaQueue.hpp
 * @brief Description of the class ParaQueue
 */

#ifndef PARAPAT_QUEUE_H_
#define PARAPAT_QUEUE_H_

#include <mutex>
#include <queue>
#include <string>

using namespace std;

namespace ParaPat {
class ParaQueueEmptyException : public exception {
  public:
    const char *what() const throw() { return "Queue Empty"; }
};

template <typename type> class ParaQueue {

  public:
    queue<type> store;
    mutex *store_mutex = new mutex;

    type pop();
    void push(type item);
    int size();
    bool empty();
    queue<type> getStore();

    // Allows quick creation of thread-safe queue from existing queue
    ParaQueue(queue<type> store) { this->store = store; }

    // Default constructor creating a new, empty store
    ParaQueue() {}
};
} // namespace ParaPat

#include "ParaQueue.cpp"

#endif
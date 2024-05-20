/**
 * @file ParaQueue.cpp
 * @brief Implementation of the class ParaQueue.
 */

#ifndef PARAPAT_QUEUE_IMPL_
#define PARAPAT_QUEUE_IMPL_

#include "ParaQueue.hpp"

using namespace ParaPat;

/**
 * Thread-safe queue pop.
 *  @returns item from the front of the queue
 *
 */
template <typename type> type ParaQueue<type>::pop() {
    this->store_mutex->lock();

    if (this->store.empty()) {
        this->store_mutex->unlock();
        throw ParaQueueEmptyException();
    }

    type item = this->store.front();
    this->store.pop();

    this->store_mutex->unlock();

    return item;
}

/**
 * Thread-safe queue push
 * @param item Item to add to back of queue
 *
 */
template <typename type> void ParaQueue<type>::push(type item) {
    this->store_mutex->lock();
    this->store.push(item);
    this->store_mutex->unlock();
}

/**
 * Fetches the size of the queue
 */
template <typename type> int ParaQueue<type>::size() {
    this->store_mutex->lock();
    int size = this->store.size();
    this->store_mutex->unlock();
    return size;
}

/**
 * Returns whether the queue is empty.
 */
template <typename type> bool ParaQueue<type>::empty() {
    return this->size() == 0;
}

template <typename type> queue<type> ParaQueue<type>::getStore() {
    return this->store;
}

#endif
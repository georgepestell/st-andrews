/**
 * @file Node.cpp
 * @brief Implementation of the Node interface.
 */

#ifndef PARAPAT_NODE_IMPL_
#define PARAPAT_NODE_IMPL_

#include "Node.hpp"
#include <iostream>
#include <stdexcept>

using namespace ParaPat;

using namespace std;

template <typename in_type, typename out_type>
void Node<in_type, out_type>::run() {

    // Run the child spawn function in a new thread, passing in the node
    int status = pthread_create(&this->thread_id, NULL, this->spawn_fn, this);

    // If the thread failed to spawn, throw an error
    if (status != 0) {
        throw NodeThreadCreationError(status);
    }
}

template <typename in_type, typename out_type>
void Node<in_type, out_type>::wait() {

    // Alert the user if waiting on a potentially infinite loop
    if (!this->stop_on_empty) {
        cerr << "WARNING: Waiting on Node who is not set to stop once input is "
                "empty."
             << endl;
    }

    pthread_join(this->thread_id, NULL);
}

template <typename in_type, typename out_type>
void Node<in_type, out_type>::run_and_wait() {
    run();
    wait();
}

template <typename in_type, typename out_type>
void Node<in_type, out_type>::set_stop_on_empty(bool value) {
    this->stop_on_empty = value;
}

template <typename in_type, typename out_type>
queue<out_type> Node<in_type, out_type>::getOutput() {
    return this->output_queue->store;
}

#endif
/**
 * @file Worker.cpp
 * @brief Implementation of the Node sub-class Worker.
 */

#ifndef PARAPAT_WORKER_IMPL_
#define PARAPAT_WORKER_IMPL_

#include "ParaQueue.hpp"
#define PARAPAT_WORKER_STREAM_WAIT 0.01s

#include <iostream>

#include "Worker.hpp"

template <typename in_type, typename out_type>
void *Worker<in_type, out_type>::spawn(void *arg) {

    // Get worker details
    Worker<in_type, out_type> *worker = (Worker<in_type, out_type> *)arg;

    /**
     * Repeatedly pop items from the queue and process.
     *
     * Infinitely executes unless signaled by the worker. Means input queues can
     * be added to during execution
     *
     */
    while (true) {
        try {

            // Fetch a task from the queue
            in_type task = worker->input_queue->pop();

            // Execute the worker function on the task
            out_type res = worker->worker_fn(task);

            // Add the result to the output queue
            worker->output_queue->push(res);

        } catch (ParaQueueEmptyException e) {

            // Check stop parameter. When true, it constantly checks for the
            // input to queue to be updated.
            if (worker->stop_on_empty && worker->input_queue->empty()) {
                return NULL;
            }

            // Sleep to prevent high CPU busy-wait
            std::this_thread::sleep_for(PARAPAT_WORKER_STREAM_WAIT);
        }
    }
}

#endif
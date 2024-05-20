/**
 * @file Farm.cpp
 * @brief Implementation of the Node sub-class Farm.
 */

#ifndef PARAPAT_FARM_IMPL_
#define PARAPAT_FARM_IMPL_

#define PARAPAT_FARM_STREAM_WAIT 0.01s

#include "Farm.hpp"
#include "Node.hpp"
#include "ParaQueue.hpp"

using namespace ParaPat;

template <typename in_type, typename out_type>
void *Farm<in_type, out_type>::spawn(void *arg) {
    Farm<in_type, out_type> *farm = (Farm<in_type, out_type> *)arg;

    if (farm->worker_count < 1) {
        throw invalid_argument("Farms require at least one worker");
    }

    /*
     * Run the workers first, allowing workers to process input as soon as it's
     * added from the main input queue
     */
    for (auto worker : farm->workers) {
        try {
            worker->run();
        } catch (NodeThreadCreationError e) {

            /*
             * Thread failed to spawn
             *
             * Most likely caused by lack of resources due to too many threads.
             * Best to fail, as large tasks may be intractible if too few
             * threads successfully spawn causing confusion.
             */

            // Signal other threads to exit early
            cerr << "ERROR: Worker failed to spawn" << endl;
            for (auto worker : farm->workers) {
                pthread_kill(worker->thread_id, SIGTERM);
            }

            // Wait for the threads to cleanly exit
            for (auto worker : farm->workers) {
                pthread_join(worker->thread_id, NULL);
            }

            throw e;
        }
    }

    /*
     * Fill Worker input queues from master input queue
     *
     * do {} while () allows pipelining farms, as the input queue is
     * continuously re-checked if empty, allowing async filling.
     *
     * w is set here so it isn't reset to 0 each loop. Prevents the first worker
     * getting most tasks when pipelining
     */
    int w = 0;
    while (true) {
        try {
            // Move task from master queue to worker queue
            farm->workers[w]->input_queue->push(farm->input_queue->pop());

            // Distribute tasks evenly (wrap around)
            w = ++w % farm->workers.size();
        } catch (ParaQueueEmptyException e) {

            // Popping from input queue failed

            // Check stop parameter. When true, it constantly checks for the
            // input to queue to be updated.
            if (farm->stop_on_empty && farm->input_queue->empty()) {
                break;
            }

            // Sleep to prevent high CPU busy-wait
            std::this_thread::sleep_for(PARAPAT_FARM_STREAM_WAIT);
        }
    }

    // Now there are no more tasks to distribute, tell the Workers to stop once
    // their input queues are empty.
    for (auto worker : farm->workers) {
        worker->set_stop_on_empty(true);
    }

    // After telling each worker to stop, we now must wait for them to finish
    // Doing this separately prevents unnecessary busy-wait whilst waiting for
    // previous workers to finish.
    for (Worker<in_type, out_type> *worker : farm->workers) {
        worker->wait();

        // Collect the worker output queue into the Farm master output queue
        while (!worker->output_queue->empty()) {
            farm->output_queue->push(worker->output_queue->pop());
        }
    }

    return NULL;
}

#endif
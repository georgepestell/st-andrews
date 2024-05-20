/**
 * @file Worker.hpp
 * @brief Description of the Node sub-class Worker.
 */

#ifndef PARAPAT_WORKER_H_
#define PARAPAT_WORKER_H_

#include "Node.hpp"
#include "ParaQueue.hpp"

using namespace ParaPat;

namespace ParaPat {

template <typename in_type, typename out_type>
class Worker : public Node<in_type, out_type> {
  public:
    out_type (*worker_fn)(in_type);

    static void *spawn(void *arg);

    // Constructor, setting the worker function and input queue
    Worker(out_type (*worker_fn)(in_type), ParaQueue<in_type> *input_queue) {
        this->worker_fn = worker_fn;
        this->input_queue = input_queue;
        this->spawn_fn = this->spawn;
    }

    Worker(out_type (*worker_fn)(in_type))
        : Worker<in_type, out_type>(worker_fn, new ParaQueue<in_type>()) {}

    /**
     * Manages a worker spawned by a pattern implementation.
     * Runs in a thread. Fetches tasks from a given input queue. Adds the
     * results to an output queue
     *
     */
    static void *workerWrapper(void *arg);
};

} // namespace ParaPat

#include "Worker.cpp"

#endif
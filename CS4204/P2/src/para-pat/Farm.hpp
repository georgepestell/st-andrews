/**
 * @file Farm.hpp
 * @brief Description of the Node sub-class Farm.
 */

#ifndef PARAPAT_FARM_H_
#define PARAPAT_FARM_H_

#include <csignal>

#include "Node.hpp"
#include "Worker.hpp"

namespace ParaPat {

/**
 * @brief Farm Skeleton. Applies the worker_fn to each element in the input
 * queue, outputting results to the output queue.
 *
 * @tparam in_type Task type (input type to worker functon)
 * @tparam out_type Task output type (output type of worker function)
 */
template <typename in_type, typename out_type>
class Farm : public Node<in_type, out_type> {
  public:
    /**
     * @brief The number of Worker nodes to spawn
     *
     */
    int worker_count;

    /**
     * @brief Function each Worker will run on their input queues. Set by
     * worker_fn constructor parameter
     *
     */
    out_type (*worker_fn)(in_type);

    /**
     * @brief Set of workers in the Farm. Filled during initialization. Size
     * determined by worker_count constructor parameter.
     */
    vector<Worker<in_type, out_type> *> workers;
    /**
     * @brief Function ran in new thread when Node::run() is called. Fills
     * Worker input queues from the master queue. Runs each Worker, then
     * collects Worker outputs into master output queue.
     *
     * @param arg pthread_create requires the function be static, and so the
     * instance must be passed through as an argument
     * @return void * - just to satisfy pthread_create
     */
    static void *spawn(void *arg);

    /**
     * @brief Constructs a new Farm object. Sets the master input queue to the
     * one pointed to (does not copy). Spawns worker_count x Worker nodes with
     * the worker_fn.
     *
     * @param worker_count Number of workers to spawn
     * @param worker_fn Function Workers will apply to input
     * @param input_queue Master thread-safe input queue (does not copy)
     */
    Farm(uint worker_count, out_type (*worker_fn)(in_type),
         ParaQueue<in_type> *input_queue) {

        // Create Worker nodes
        for (int i = 0; i < worker_count; i++) {
            Worker<in_type, out_type> *new_worker = new Worker(worker_fn);

            // Farm asynchronously fills Worker input queues. Therefore, Workers
            // should only stop once master input queue is empty.
            new_worker->set_stop_on_empty(false);

            this->workers.push_back(new_worker);
        }

        // Set Farm attributes
        this->worker_count = worker_count;
        this->input_queue = input_queue;

        // Set the spawn_fn to the Farm spawn function. Allows Node parent to
        // call child function.
        this->spawn_fn = this->spawn;
    }

    /**
     * @brief Constructs a new Farm object. Creates master input queue from the
     * supplied std::queue (creates a thread-safe copy). Calls master
     * constructor.
     * @param worker_count Number of workers to spawn
     * @param worker_fn Function Workers will apply to input
     * @param input_queue std::queue to generate master queue from (copies)
     */
    Farm(uint worker_count, out_type (*worker_fn)(in_type),
         queue<in_type> input_queue)
        : Farm(worker_count, worker_fn, new ParaQueue<in_type>(input_queue)){};

    /**
     * @brief Construct a new Farm object. Creates empty master input queue.
     *
     * @param worker_count Number of workers to spawn
     * @param worker_fn Function Workers will apply to input
     */
    Farm(uint worker_count, out_type (*worker_fn)(in_type))
        : Farm(worker_count, worker_fn, new ParaQueue<in_type>()){};
};

} // namespace ParaPat

#include "Farm.cpp"

#endif
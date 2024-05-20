/**
 * @file Node.hpp
 * @brief Description of the Node interface.
 */

#ifndef PARAPAT_NODE_H_
#define PARAPAT_NODE_H_

#include "ParaQueue.hpp"
#include <atomic>
#include <cstring>
#include <stdexcept>
#include <thread>

using namespace ParaPat;
using namespace std;

namespace ParaPat {

/**
 * @brief Exception thrown when the spawn function failed to be ran in a new
 * thread
 *
 */
class NodeThreadCreationError : public runtime_error {
  public:
    int status;

    NodeThreadCreationError(int status)
        : runtime_error("Failed to spawn node thread. Status: " +
                        string(strerror(status))){};
};

/**
 * @brief Node interface class. Defines the common functions and attributes of
 * all Nodes.
 *
 * @tparam in_type Input queue task type
 * @tparam out_type Output queue result type
 */
template <typename in_type, typename out_type> class Node {
  public:
    /**
     * @brief Node thread id set by pthread_create when run() is called
     */
    pthread_t thread_id;

    /**
     * @brief The output queue of the node, filled in the child's spawn function
     *
     */
    ParaQueue<out_type> *output_queue = new ParaQueue<out_type>();

    /**
     * @brief The input queue of the node, initialized in the child's
     * constructor.
     *
     */
    ParaQueue<in_type> *input_queue;

    /**
     * @brief A flag denoting whether the node should stop finish once the input
     * queue is empty. When set to false, the node should continue checking the
     * input queue, allowing asynchronous input queue filling.
     *
     */
    atomic_bool stop_on_empty = true;

    /**
     * @brief Used in the run function for calling the child's spawn function.
     * Due to c++ pointer restrictions, this must be a variable which is set by
     * the child's constructor.
     *
     */
    void *(*spawn_fn)(void *);

    /**
     * @brief Set the stop_on_empty variable to true/false
     *
     * @param value Whether to stop when the input queue is empty, or continue
     * checking for  for input.
     *
     */
    virtual void set_stop_on_empty(bool value);

    /**
     * @brief Run the Node. Spawns a new thread running the child's spawn
     * function.
     *
     */
    void run();

    /**
     * @brief Wait for the thread created by run() to complete.
     *
     */
    void wait();

    /**
     * @brief Calls run() and then wait() for the Node to finish (self
     * explanatory).
     *
     */
    void run_and_wait();

    /**
     * @brief Creates a copy of the output queue as a std::queue data structure.
     *
     * @return queue<out_type> A copy of the output queue
     */
    queue<out_type> getOutput();
};
} // namespace ParaPat

#include "Node.cpp"

#endif
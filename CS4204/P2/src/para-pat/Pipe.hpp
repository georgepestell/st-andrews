/**
 * @file Pipe.hpp
 * @brief Description of the Node sub-class Pipe.
 */

#ifndef PARAPAT_PIPE_H_
#define PARAPAT_PIPE_H_

#include "ParaQueue.hpp"
#include "Worker.hpp"
#include <csignal>

using namespace ParaPat;

namespace ParaPat {

template <typename stage1_in_type, typename stage2_in_type,
          typename stage3_in_type, typename out_type>
class Pipe : public Node<stage1_in_type, out_type> {
  public:
    static void *spawn(void *arg);

    Node<stage1_in_type, stage2_in_type> *stage1;
    Node<stage2_in_type, stage3_in_type> *stage2;
    Node<stage3_in_type, out_type> *stage3;

    void set_stop_on_empty(bool value);

    // Default constructor, allowing pipeline of pipes
    Pipe(Node<stage1_in_type, stage2_in_type> *stage1,
         Node<stage2_in_type, stage3_in_type> *stage2,
         Node<stage3_in_type, out_type> *stage3,
         ParaQueue<stage1_in_type> *input_queue) {

        this->stage1 = stage1;
        this->stage2 = stage2;
        this->stage3 = stage3;
        this->input_queue = input_queue;

        stage1->input_queue = input_queue;
        stage2->input_queue = stage1->output_queue;
        stage3->input_queue = stage2->output_queue;

        this->output_queue = stage3->output_queue;

        // Ensure each node is set to correctly stop
        stage1->set_stop_on_empty(true);
        stage2->set_stop_on_empty(false);
        stage3->set_stop_on_empty(false);

        this->spawn_fn = this->spawn;
    }

    // Constructor allowing pipeline to be created with stdlib queue
    Pipe(Node<stage1_in_type, stage2_in_type> *stage1,
         Node<stage2_in_type, stage3_in_type> *stage2,
         Node<stage3_in_type, out_type> *stage3,
         queue<stage1_in_type> input_queue)
        : Pipe(stage1, stage2, stage3,
               new ParaQueue<stage1_in_type>(input_queue)) {}
};

} // namespace ParaPat

#include "Pipe.cpp"

#endif
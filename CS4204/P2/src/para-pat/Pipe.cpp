/**
 * @file Pipe.cpp
 * @brief Implementation of the Node sub-class Pipe.
 */

#ifndef PARAPAT_PIPE_IMPL_
#define PARAPAT_PIPE_IMPL_

#include "Pipe.hpp"

template <typename stage1_in_type, typename stage2_in_type,
          typename stage3_in_type, typename out_type>
void *Pipe<stage1_in_type, stage2_in_type, stage3_in_type, out_type>::spawn(
    void *arg) {
    Pipe<stage1_in_type, stage2_in_type, stage3_in_type, out_type> *pipe =
        (Pipe<stage1_in_type, stage2_in_type, stage3_in_type, out_type> *)arg;

    pipe->stage1->run();
    pipe->stage2->run();
    pipe->stage3->run();

    pipe->stage1->wait();

    pipe->stage2->set_stop_on_empty(true);
    pipe->stage2->wait();

    pipe->stage3->set_stop_on_empty(true);
    pipe->stage3->wait();

    return NULL;
}

template <typename stage1_in_type, typename stage2_in_type,
          typename stage3_in_type, typename out_type>
void Pipe<stage1_in_type, stage2_in_type, stage3_in_type,
          out_type>::set_stop_on_empty(bool value) {
    this->stop_on_empty = value;
    this->stage1->set_stop_on_empty(value);
}

#endif
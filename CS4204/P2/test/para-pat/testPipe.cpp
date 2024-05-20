#include <boost/test/unit_test.hpp>
#include <para-pat/Farm.hpp>
#include <para-pat/Pipe.hpp>

#include "../functions.hpp"

BOOST_AUTO_TEST_SUITE(test_Pipe)

BOOST_AUTO_TEST_CASE(simpleExample) {
    queue<bool> input_queue({true, true, false, true, false});

    // Create workers for each function

    Worker<bool, int> *stage1 = new Worker(&convertBoolToInt);
    Worker<int, int> *stage2 = new Worker(&addOne);
    Worker<int, int> *stage3 = new Worker(&addOne);

    Pipe<bool, int, int, int> *pipe =
        new Pipe(stage1, stage2, stage3, input_queue);

    pipe->run_and_wait();

    BOOST_CHECK_EQUAL(pipe->output_queue->pop(), 3);
    BOOST_CHECK_EQUAL(pipe->output_queue->pop(), 3);
    BOOST_CHECK_EQUAL(pipe->output_queue->pop(), 2);
    BOOST_CHECK_EQUAL(pipe->output_queue->pop(), 3);
    BOOST_CHECK_EQUAL(pipe->output_queue->pop(), 2);
}

BOOST_AUTO_TEST_SUITE_END()
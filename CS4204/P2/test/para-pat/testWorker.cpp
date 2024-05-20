#include <boost/test/unit_test.hpp>
#include <csignal>
#include <queue>
#include <thread>

#include "../functions.hpp"
#include <para-pat/Farm.hpp>
#include <para-pat/ParaQueue.hpp>

BOOST_AUTO_TEST_SUITE(test_Worker)

BOOST_AUTO_TEST_CASE(emptiesInputQueue) {
    ParaQueue<bool> *input_queue = new ParaQueue<bool>();

    input_queue->push(true);
    input_queue->push(true);
    input_queue->push(false);

    Worker<bool, int> worker = Worker(&alwaysReturnTwo, input_queue);

    worker.run_and_wait();

    BOOST_CHECK_EQUAL(input_queue->empty(), true);
}

BOOST_AUTO_TEST_CASE(populatesOutputQueue) {
    ParaQueue<bool> *input_queue = new ParaQueue<bool>();

    input_queue->push(true);
    input_queue->push(true);
    input_queue->push(false);

    Worker<bool, int> worker = Worker(&convertBoolToInt, input_queue);

    worker.run_and_wait();

    BOOST_CHECK_EQUAL(worker.output_queue->size(), 3);
}

BOOST_AUTO_TEST_CASE(correctOutputValues) {
    ParaQueue<bool> *input_queue = new ParaQueue<bool>();

    input_queue->push(true);
    input_queue->push(true);
    input_queue->push(false);
    input_queue->push(false);
    input_queue->push(false);
    input_queue->push(false);
    input_queue->push(false);

    Worker<bool, int> worker = Worker(&convertBoolToInt, input_queue);

    worker.run_and_wait();

    int zeroCount = 0;
    int oneCount = 0;

    while (!worker.output_queue->empty()) {
        int value = worker.output_queue->pop();
        if (value == 0) {
            zeroCount++;
        } else if (value == 1) {
            oneCount++;
        } else {
            BOOST_FAIL("convertBoolToInt should only output 0s and 1s");
        }
    }

    BOOST_CHECK_EQUAL(zeroCount, 5);
    BOOST_CHECK_EQUAL(oneCount, 2);
}

BOOST_AUTO_TEST_SUITE_END()
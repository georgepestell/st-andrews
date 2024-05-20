#include <boost/test/unit_test.hpp>
#include <queue>

#include "../functions.hpp"
#include <para-pat/ParaQueue.hpp>

BOOST_AUTO_TEST_SUITE(test_ParaQueue)

BOOST_AUTO_TEST_CASE(initialSizeIsZero) {
    ParaQueue<int> *input_queue = new ParaQueue<int>();
    BOOST_CHECK_EQUAL(input_queue->size(), 0);
}

BOOST_AUTO_TEST_CASE(initialEmptyTrue) {
    ParaQueue<int> *input_queue = new ParaQueue<int>();
    BOOST_CHECK_EQUAL(input_queue->empty(), true);
}

BOOST_AUTO_TEST_CASE(pushSingleItemIncrementsSize) {
    ParaQueue<int> *output_queue = new ParaQueue<int>();
    output_queue->push(3);
    BOOST_CHECK_EQUAL(output_queue->size(), 1);
}

BOOST_AUTO_TEST_CASE(pushSingleItemNotEmpty) {
    ParaQueue<int> *output_queue = new ParaQueue<int>();
    output_queue->push(3);
    BOOST_CHECK_EQUAL(output_queue->empty(), false);
}

BOOST_AUTO_TEST_CASE(initializeWithQueue) {
    queue<int> initialQueue({4, 5, 6, 11, 42});

    ParaQueue<int> paraQueue(initialQueue);

    while (!paraQueue.empty()) {
        BOOST_CHECK_EQUAL(paraQueue.pop(), initialQueue.front());
        initialQueue.pop();
    }
}

BOOST_AUTO_TEST_SUITE_END()
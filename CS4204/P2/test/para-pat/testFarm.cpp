#include <boost/test/tools/old/interface.hpp>
#include <boost/test/unit_test.hpp>
#include <queue>

#include "../functions.hpp"
#include <para-pat/Farm.hpp>
#include <para-pat/ParaQueue.hpp>

using namespace std;

using namespace ParaPat;

BOOST_AUTO_TEST_SUITE(test_Farm)

BOOST_AUTO_TEST_CASE(outputVectorCorrectType) {

    queue<bool> input_queue({true, false, true, true, false, true});

    Farm<bool, int> farm = Farm(3, &convertBoolToInt, input_queue);

    BOOST_CHECK_NO_THROW((farm.run_and_wait()));
}

BOOST_AUTO_TEST_CASE(getOutputReturnsQueueCopy) {
    ParaQueue<int> *output_queue = new ParaQueue(queue<int>({3, 4, 11, 5}));

    Farm<bool, int> farm = Farm(3, &convertBoolToInt, new ParaQueue<bool>);

    farm.output_queue = output_queue;

    queue<int> returnQueue = farm.getOutput();

    while (!returnQueue.empty()) {
        BOOST_CHECK_EQUAL(returnQueue.front(), output_queue->pop());
        returnQueue.pop();
    }
}

BOOST_AUTO_TEST_CASE(outputsAllGenerated) {

    queue<bool> input_queue({true, false, true, true, false, true});

    Farm<bool, int> farm = Farm(3, &convertBoolToInt, input_queue);

    farm.run_and_wait();

    queue<int> output_queue = farm.getOutput();

    BOOST_CHECK_EQUAL(output_queue.size(), 6);

    int oneCount = 0;
    int zeroCount = 0;
    while (!output_queue.empty()) {
        int o = output_queue.front();
        output_queue.pop();
        if (o == 1) {
            oneCount++;
        } else if (o == 0) {
            zeroCount++;
        } else {
            BOOST_FAIL("Output should be zero or one");
        }
    }

    BOOST_CHECK_EQUAL(oneCount, 4);
    BOOST_CHECK_EQUAL(zeroCount, 2);
}

BOOST_AUTO_TEST_SUITE_END()
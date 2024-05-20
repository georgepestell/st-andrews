#define BOOST_TEST_MODULE implementation_tests
#include <boost/test/included/unit_test.hpp>

#include <atomic>
#include <condition_variable>
#include <cstdlib>
#include <future>
#include <string>
#include <thread>

#include <CMSet.cpp>

using namespace std;

const int DEFAULT_THREADS = 8;
const int WORK_MULTIPLIER = 10;

/**
 * Later tests include threading interaction. An atomic counter is used to
 * ensure that all threads complete before value checking.
 *
 */

// Ensure no errors are thrown upon initialization with a type
BOOST_AUTO_TEST_CASE(test_initialization) {
  CMSet<string> *set = new CMSet<string>();
}

BOOST_AUTO_TEST_CASE(test_contains_initially_false) {
  int TEST_MIN = -12823;
  int TEST_MAX = 18312;
  CMSet<int> *set = new CMSet<int>();
  for (int i = TEST_MIN; i <= TEST_MAX; i++) {
    BOOST_TEST(set->contains(i) == false);
  }
}

BOOST_AUTO_TEST_CASE(test_count_initially_zero) {
  int TEST_MIN = -12238;
  int TEST_MAX = 39212;
  CMSet<int> *set = new CMSet<int>();
  for (int i = TEST_MIN; i <= TEST_MAX; i++) {
    BOOST_TEST(set->count(i) == 0);
  }
}

BOOST_AUTO_TEST_CASE(test_add_one) {
  char val = 'r';
  CMSet<char> *set = new CMSet<char>();

  set->add(val);
  BOOST_TEST(set->count(val) == 1);
}

BOOST_AUTO_TEST_CASE(test_contains_true_on_add) {
  char val = 'r';
  CMSet<char> *set = new CMSet<char>();

  set->add(val);
  BOOST_TEST(set->contains(val) == true);
}

BOOST_AUTO_TEST_CASE(test_remove_one) {
  char val = 'r';
  CMSet<char> *set = new CMSet<char>();

  set->add(val);
  BOOST_TEST(set->remove(val) == true);
}

BOOST_AUTO_TEST_CASE(test_remove_count) {
  char val = 'r';
  CMSet<char> *set = new CMSet<char>();

  // 3 - 1 = 2
  set->add(val);
  set->add(val);
  set->add(val);

  // Check the remove succeeds
  BOOST_TEST(set->remove(val) == true);
  BOOST_TEST(set->count(val) == 2);
}

BOOST_AUTO_TEST_CASE(test_remove_one_on_empty) {
  char val = 'r';
  CMSet<char> *set = new CMSet<char>();

  BOOST_TEST(set->remove(val) == false);
  BOOST_TEST(set->count(val) == 0);
}

BOOST_AUTO_TEST_CASE(test_contains_false_on_add_remove) {
  char val = 'r';
  CMSet<char> *set = new CMSet<char>();

  set->add(val);
  set->remove(val);
  BOOST_TEST(set->contains(val) == false);
}

BOOST_AUTO_TEST_CASE(test_thread_add) {
  string val = "testString";
  CMSet<string> *set = new CMSet<string>();

  // Get the max threads, or use the default
  int THREADS = (thread::hardware_concurrency())
                    ? thread::hardware_concurrency()
                    : DEFAULT_THREADS;

  // Each thread will do 127 additions
  int ADD_COUNT = 127 * WORK_MULTIPLIER;
  int TARGET_TOTAL = THREADS * ADD_COUNT;

  // Setup the count to 1, which is decremented once the threads are initialized
  atomic_int count(1);
  for (int t = 0; t < THREADS; t++) {
    new thread([set, val, ADD_COUNT, &count] {
      count++;
      for (int i = 0; i < ADD_COUNT; i++) {
        set->add(val);
      }
      count--;
    });
  }
  count--;

  // Wait for all threads to complete
  while (count) {
    sleep(1);
  }

  // Check the total
  BOOST_TEST(set->count(val) == TARGET_TOTAL);
}

BOOST_AUTO_TEST_CASE(test_thread_remove) {
  // Get the max threads, or use the default

  int THREADS = (thread::hardware_concurrency())
                    ? thread::hardware_concurrency()
                    : DEFAULT_THREADS;

  // Add ADD_COUNT sequentially, remove REMOVE_COUNT per thread
  int ADD_COUNT = 1233 * THREADS * WORK_MULTIPLIER;
  int REMOVE_COUNT = 810 * WORK_MULTIPLIER;

  char c = 'a';
  char *val = &c;
  CMSet<char *> *set = new CMSet<char *>();

  // Add the initial values sequentially
  for (int i = 0; i < ADD_COUNT; i++) {
    set->add(val);
  }

  // Setup the count to 1, which is decremented once the threads are initialized
  atomic_int count(1);
  for (int t = 0; t < THREADS; t++) {
    new thread([set, val, REMOVE_COUNT, &count] {
      count++;
      for (int i = 0; i < REMOVE_COUNT; i++) {
        set->remove(val);
      }
      count--;
    });
  }
  count--;

  // Wait for all threads to complete
  while (count) {
    sleep(1);
  }

  // Check the total
  BOOST_TEST(set->count(val) == ADD_COUNT - (REMOVE_COUNT * THREADS));
}

BOOST_AUTO_TEST_CASE(test_thread_add_and_remove) {
  // Get the max threads, or use the default

  int THREADS = (thread::hardware_concurrency())
                    ? thread::hardware_concurrency()
                    : DEFAULT_THREADS;

  // Add and remove per thread
  int ADD_COUNT = 1233 * WORK_MULTIPLIER;
  int REMOVE_COUNT = 810 * WORK_MULTIPLIER;

  char c = 'a';
  char *val = &c;
  CMSet<char *> *set = new CMSet<char *>();

  // Setup the count to 1, which is decremented once the threads are initialized
  atomic_int count(1);
  for (int t = 0; t < THREADS; t++) {
    new thread([set, val, ADD_COUNT, &count] {
      count++;
      for (int i = 0; i < ADD_COUNT; i++) {
        set->add(val);
      }
      count--;
    });
    new thread([set, val, REMOVE_COUNT, &count] {
      count++;
      for (int i = 0; i < REMOVE_COUNT; i++) {
        // Repeatedly try removing till successful
        while (!set->remove(val)) {
          // DO NOTHING
        }
      }
      count--;
    });
  }

  count--;

  // Wait for all threads to complete
  while (count) {
    sleep(1);
  }

  // Check the total
  BOOST_TEST(set->count(val) ==
             (ADD_COUNT * THREADS) - (REMOVE_COUNT * THREADS));
}

BOOST_AUTO_TEST_CASE(test_thread_add_and_remove_adding_entries) {
  // Get the max threads, or use the default

  int THREADS = (thread::hardware_concurrency())
                    ? thread::hardware_concurrency()
                    : DEFAULT_THREADS;

  int MAX_VAL = 100;
  map<int, int> expected;

  // Add and remove per thread
  int ADD_COUNT = 1233 * WORK_MULTIPLIER;
  int REMOVE_COUNT = 810 * WORK_MULTIPLIER;

  CMSet<short> *set = new CMSet<short>();

  // For each thread, pick a random value
  short *values = new short[THREADS];

  for (int i = 0; i < THREADS; i++) {
    short val = (short)(rand() % MAX_VAL);
    values[i] = val;
    expected[val] += ADD_COUNT - REMOVE_COUNT;
  }

  // Setup the count to 1, which is decremented once the threads are
  // initialized
  atomic_int count(1);
  for (int t = 0; t < THREADS; t++) {
    new thread([set, values, t, ADD_COUNT, &count] {
      count++;
      for (int i = 0; i < ADD_COUNT; i++) {
        set->add(values[t]);
      }
      count--;
    });
    new thread([set, values, t, REMOVE_COUNT, &count] {
      count++;
      for (int i = 0; i < REMOVE_COUNT; i++) {
        // Repeatedly try removing till successful
        while (!set->remove(values[t])) {
          // DO NOTHING
        }
      }
      count--;
    });
  }

  count--;

  // Wait for all threads to complete
  while (count) {
    sleep(1);
  }

  for (int i = 0; i < MAX_VAL; i++) {
    // cerr << "i: " << i << ", e: " << expected[i] << ", r: " << set->count(i)
    //      << endl; // DEBUG: Check expected output

    BOOST_TEST(set->count(i) == expected[i]);
  }
}

BOOST_AUTO_TEST_CASE(test_thread_add_and_remove_adding_overlapping) {
  // Get the max threads, or use the default

  int THREADS = (thread::hardware_concurrency())
                    ? thread::hardware_concurrency()
                    : DEFAULT_THREADS;

  int MAX_VAL = 3;
  map<int, int> expected;

  // Add and remove per thread
  int ADD_COUNT = 1233 * WORK_MULTIPLIER;
  int REMOVE_COUNT = 810 * WORK_MULTIPLIER;

  CMSet<short> *set = new CMSet<short>();

  // For each thread, pick a random value
  short *values = new short[THREADS];

  for (int i = 0; i < THREADS; i++) {
    short val = (short)(rand() % MAX_VAL);
    values[i] = val;
    expected[val] += ADD_COUNT - REMOVE_COUNT;
  }

  // Setup the count to 1, which is decremented once the threads are
  // initialized
  atomic_int count(1);
  for (int t = 0; t < THREADS; t++) {
    new thread([set, values, t, ADD_COUNT, &count] {
      count++;
      for (int i = 0; i < ADD_COUNT; i++) {
        set->add(values[t]);
      }
      count--;
    });
    new thread([set, values, t, REMOVE_COUNT, &count] {
      count++;
      for (int i = 0; i < REMOVE_COUNT; i++) {
        // Repeatedly try removing till successful
        while (!set->remove(values[t])) {
          // DO NOTHING
        }
      }
      count--;
    });
  }

  count--;

  // Wait for all threads to complete
  while (count) {
    sleep(1);
  }

  for (int i = 0; i < MAX_VAL; i++) {
    // cerr << "i: " << i << ", e: " << expected[i] << ", r: " << set->count(i)
    //     << endl; // DEBUG: Check expected values

    // Ensure that each value has the expected number of entries
    BOOST_TEST(set->count(i) == expected[i]);
  }
}

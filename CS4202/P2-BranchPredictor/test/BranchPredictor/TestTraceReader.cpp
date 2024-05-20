#include <asm-generic/errno-base.h>
#include <boost/test/tools/old/interface.hpp>
#include <boost/test/unit_test.hpp>

#include <BranchPredictor/TraceReader.hpp>
#include <cstdio>
#include <iostream>
#include <stdexcept>
#include <string>

using namespace std;
using namespace CS4202_P2;

string traceDir = getenv("BRANCHPRED_TRACE_DIR");

BOOST_AUTO_TEST_SUITE(test_TraceReader);

BOOST_AUTO_TEST_CASE(initializesInputStream) {
  TraceReader *r;

  cerr << traceDir << endl;

  // Use a temporary file (preventing relative filepath errors)
  BOOST_CHECK_NO_THROW((r = new TraceReader(traceDir + "/bwaves.out")));

  r->close();
}

BOOST_AUTO_TEST_CASE(initizationThrowsOnInvalidFile) {
  TraceReader *r;
  BOOST_CHECK_THROW((r = new TraceReader(traceDir + "/DOES_NOT_EXIST.out")),
                    runtime_error);
}

BOOST_AUTO_TEST_CASE(readLineCreatesObject) {
  TraceReader r = TraceReader(traceDir + "/bwaves.out");
  Trace trace = r.readLine();
  BOOST_CHECK_EQUAL(trace.p_addr, 139865580069555);
  // BOOST_CHECK_EQUAL(trace.branchKind, 'c');
  // BOOST_CHECK_EQUAL(trace.isDirect, true);
  // BOOST_CHECK_EQUAL(trace.isConditional, false);
  BOOST_CHECK_EQUAL(trace.isTaken, true);

  r.close();
}

BOOST_AUTO_TEST_CASE(tracetoStringFormatCorrect) {
  TraceReader r = TraceReader(traceDir + "/bwaves.out");
  Trace trace = r.readLine();
  BOOST_CHECK_EQUAL(trace.toString(), "00007f34fe3762b3 1");

  r.close();
}

BOOST_AUTO_TEST_CASE(readsManyLinesCorrectly) {
  TraceReader r = TraceReader(traceDir + "/bwaves.out");
  Trace trace;

  trace = r.readLine();
  BOOST_CHECK_EQUAL(trace.toString(), "00007f34fe3762b3 1");
  trace = r.readLine();
  BOOST_CHECK_EQUAL(trace.toString(), "00007f34fe3770a9 0");
  trace = r.readLine();
  BOOST_CHECK_EQUAL(trace.toString(), "00007f34fe3770de 1");
  trace = r.readLine();
  BOOST_CHECK_EQUAL(trace.toString(), "00007f34fe3770fe 1");
  trace = r.readLine();
  BOOST_CHECK_EQUAL(trace.toString(), "00007f34fe3770f8 0");

  r.close();
}

BOOST_AUTO_TEST_CASE(readLineEOFThrowsEOFException) {
  char filename[] = "/tmp/testBranch.XXXXXX";
  int fd = mkstemp(filename);

  if (fd == -1) {
    BOOST_FAIL("Failed to setup test temp file");
  }

  char t[] = "00007f34fe3770f8 00007f34fe3770f8 c 1 0 1\n";

  write(fd, t, sizeof(t));

  close(fd);

  TraceReader r = TraceReader(filename);
  Trace trace;

  BOOST_CHECK_NO_THROW((r.readLine()));
  BOOST_CHECK_THROW((trace = r.readLine()), EOFException);

  r.close();
  // unlink(filename);
}

BOOST_AUTO_TEST_SUITE_END();
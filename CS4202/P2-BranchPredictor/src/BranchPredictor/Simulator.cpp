#include <BranchPredictor/Simulator.hpp>

using namespace CS4202_P2;

void Simulator::run() {

  // Time the main execution
  auto t_start = chrono::high_resolution_clock::now();

  while (true) {
    Trace trace;
    try {
      trace = reader->readLine();
    } catch (EOFException e) {
      break;
    }

    bool prediction = predictor->predict(trace);

    // Only increment statistics after profiling is complete
    if (this->profileTraces <= 0) {
      this->total++;
      if (prediction == trace.isTaken) {
        this->correct++;
      }
    } else {
      this->profileTraces--;
    }
  }
  auto t_end = chrono::high_resolution_clock::now();

  this->reader->close();

  // Get the ellapsed time in seconds
  this->time_taken = chrono::duration<double>(t_end - t_start).count();
}

void Simulator::printResults() {
  uint incorrect = this->total - this->correct;

  fprintf(stdout,
          "----------\n- Details:\t%s\n- Trace:\t%s\n- Total:\t%d\n- "
          "Correct:\t%d (%.2f\%)\n- "
          "Incorrect:\t%d "
          "(%.2f\%)\n- Time:\t\t%f\n",
          this->predictor->getDetails().c_str(),
          this->reader->getFilepath().c_str(), this->total, this->correct,
          ((float)this->correct / (float)this->total) * 100, incorrect,
          ((float)incorrect / (float)this->total) * 100, this->time_taken);
}

void Simulator::printCSVResults() {
  string details = this->predictor->getDetails();

  uint incorrect = this->total - this->correct;
  fprintf(stdout, "%s, %s, %d, %d, %f, %d, %f, %f\n", details.c_str(),
          this->reader->getFilepath().c_str(), this->total, this->correct,
          ((float)this->correct / (float)this->total), incorrect,
          ((float)incorrect / (float)this->total), this->time_taken);
}
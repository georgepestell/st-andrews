add_library(BranchPredictor STATIC
  TraceReader.cpp
  Predictor.cpp
  AlwaysTruePredictor.cpp
  TwoBitPredictor.cpp
  GSharePredictor.cpp
  Simulator.cpp
)
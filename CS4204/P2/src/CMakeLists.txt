find_package (Threads)
add_library(para-pat STATIC 
  para-pat/ParaQueue.cpp 
  para-pat/Worker.cpp 
  para-pat/Farm.cpp 
  para-pat/Pipe.cpp 
  para-pat/Node.cpp
)
target_link_libraries(para-pat pthread)
#!/usr/bin/bash

set -u

WORKING_DIR="$(pwd)/build/"
OUTPUT_FILE="$(pwd)/output.csv"

IMAGE_COUNT=100
EXAMPLES=(1 2 3 4 8 12 16 20 30 50 100 200 300 500 1000 2000 3000 5000 10000)

(cd $WORKING_DIR && ninja example)

echo "worker_count, runtime" > $OUTPUT_FILE

for worker_count in ${EXAMPLES[@]}
do
  val=$(cd $WORKING_DIR && ./examples/convolution_fastFlow_farm $IMAGE_COUNT ${worker_count-})
  echo "$worker_count, $val" >> $OUTPUT_FILE
  val=$(cd $WORKING_DIR && ./examples/convolution_fastFlow_farm $IMAGE_COUNT ${worker_count-})
  echo "$worker_count, $val" >> $OUTPUT_FILE
  val=$(cd $WORKING_DIR && ./examples/convolution_fastFlow_farm $IMAGE_COUNT ${worker_count-})
  echo "$worker_count, $val" >> $OUTPUT_FILE
done


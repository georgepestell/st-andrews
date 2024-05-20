#!/usr/bin/bash

set -u

WORKING_DIR="$(pwd)/build/"
OUTPUT_FILE="$(pwd)/output_fine.csv"

MAX_WORKERS=200
RANGE_START=4
RANGE_STEP=1
IMAGE_COUNT=1000

(cd $WORKING_DIR && ninja example)

echo "worker_count, runtime" > $OUTPUT_FILE

for worker_count in $(seq $RANGE_START $RANGE_STEP $MAX_WORKERS);
do
  val=$(cd $WORKING_DIR && ./examples/convolution_parapat $IMAGE_COUNT ${worker_count-})
  echo "$worker_count, $val" >> $OUTPUT_FILE
done


#!/bin/bash

GENERATOR="QueensGenerator"
FOLDER="queens"

END=30;

mkdir $FOLDER

for i in $(seq 1 $END); do
    java -cp target/classes com.example.generators.$GENERATOR $i > $FOLDER/$i.csp
done

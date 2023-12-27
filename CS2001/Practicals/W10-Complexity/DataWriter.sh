#!/bin/bash

javac src/* -d out

cd out

echo "Sorting Metric, Sortedness, Sort Time (millisecs)" > ../data.csv

java DataPrinter >> ../data.csv

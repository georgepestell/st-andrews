#!/bin/bash

set -u


TMP="/tmp/constraintSolver-eval.txt"
OUTPUT="./eval.csv"
TIME_LIMIT=60

while getopts ":o:t:" option; do
   case $option in
      o) OUTPUT=$OPTARG;;
      t) TIME_LIMIT=$OPTARG;;
   esac
done

[[ -f $OUTPUT ]] && echo "Output exists" && exit 1

echo "Instance,SolverNodes,ARCRevisions,TotalTime"  > $OUTPUT

for i in queen-ascending/*.csp; do

    # Emergency exit failsafe
    [[ -f ./end.txt ]] && exit 0

    # Run the solver
    java -cp target/classes com.example.cspsolver.BinarySolver $i > $i.info

    echo $i | cut -d '/' -f2,3 | xargs -I "{}" echo "{}" > $TMP

    # get the info from the file
    cat $i.info | grep  'TotalTime\|SolverNodes\|ARCRevisions' | cut -d " " -f2 >> $TMP

    # get the solution from the file
    # cat $i.solution | tail -n 4 | sed "s/letting.*be //g" | sed "s/\[/\"\[/g" | sed "s/\]/\]\"/g" >> $TMP

    paste -s -d "," $TMP >> $OUTPUT
    
done
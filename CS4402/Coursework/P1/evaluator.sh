#!/bin/bash

set -u

model="model-4.eprime"
TMP="/tmp/model-eval.txt"
OUTPUT="./output.csv"
TIME_LIMIT=60

while getopts ":m:o:t:" option; do
   case $option in
      m) model=$OPTARG;;
      o) OUTPUT=$OPTARG;;
      t) TIME_LIMIT=$OPTARG;;
   esac
done

[[ -f $OUTPUT ]] && echo "Output exists" && exit 1

echo "Size,SolverTotalTime,SavileRowTotalTime,SolverSatisfiable,SolverNodes,SATVars"  > $OUTPUT #EndTime,MainSurgeon,StartTime,TotaDuration"

for i in $(ls instances/*/*.param -v); do

    # Emergency exit failsafe
    [[ -f ./end.txt ]] && exit 0

    # Run the solver
    savilerow $model $i -run-solver -timelimit $((2*TIME_LIMIT)) -sat

    echo $i | cut -d '/' -f2,3 | xargs -I "{}" echo "{}" > $TMP

    # get the info from the file
    cat $i.info | grep  'SolverTotalTime\|SavileRowTotalTime\|SolverSatisfiable\|SolverNodes\|SATVars' | cut -d ":" -f2 >> $TMP

    # get the solution from the file
    # cat $i.solution | tail -n 4 | sed "s/letting.*be //g" | sed "s/\[/\"\[/g" | sed "s/\]/\]\"/g" >> $TMP

    paste -s -d "," $TMP >> $OUTPUT
    
done
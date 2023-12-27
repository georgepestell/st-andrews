package com.example.cspsolver;

import java.util.*;

public final class BinaryConstraint {
  public int firstVar, secondVar;
  private ArrayList<BinaryTuple> tuples;

  /**
   * Creates a BinaryConstraint instance representing accepted value tuples for
   * two variables
   * 
   * @param fv First variable index
   * @param sv Second variable index
   * @param t  List of accepted value tuples for the first and second variables
   */
  public BinaryConstraint(int fv, int sv, ArrayList<BinaryTuple> t) {
    firstVar = fv;
    secondVar = sv;
    tuples = t;
  }

  public String toString() {
    StringBuffer result = new StringBuffer();
    result.append("c(" + firstVar + ", " + secondVar + ")\n");
    for (BinaryTuple bt : tuples)
      result.append(bt + "\n");
    return result.toString();
  }

  // SUGGESTION: You will want to add methods here to reason about the constraint

  /**
   * Verify whether values for the first and second variables match one of the
   * tuples
   * 
   * @param v1 Value for the first variable
   * @param v2 Value for the second variable
   * @return True if the first and second variable values match one of the tuples
   */
  public boolean check(int v1, int v2) {

    // Search the accepted tuples for a match
    for (BinaryTuple t : this.tuples) {
      if (t.matches(v1, v2)) {
        return true;
      }
    }
    return false;

  }

}

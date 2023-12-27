package com.example.cspsolver;

import java.util.*;

public final class BinaryCSP {
  private int[][] domainBounds;
  private ArrayList<BinaryConstraint> constraints;

  /**
   * Creates a BinaryCSP instance from a list of variable domain bounds and
   * constraints
   * 
   * @param db Variable domain bounds, db[x][0] = lower bound, db[x][1] = upper
   *           bound.
   * @param c  Array of BinaryConstraints
   */
  public BinaryCSP(int[][] db, ArrayList<BinaryConstraint> c) {
    domainBounds = db;
    constraints = c;
  }

  public String toString() {
    StringBuffer result = new StringBuffer();
    result.append("CSP:\n");
    for (int i = 0; i < domainBounds.length; i++)
      result.append("Var " + i + ": " + domainBounds[i][0] + " .. " + domainBounds[i][1] + "\n");
    for (BinaryConstraint bc : constraints)
      result.append(bc + "\n");
    return result.toString();
  }

  /**
   * Get the number of decision variables
   * 
   * @return The count of decision variables
   */
  public int getNoVariables() {
    return domainBounds.length;
  }

  /**
   * Get the lower bound for a variable
   * 
   * @param varIndex Variable to get lower bound for. Indexed in the same order as
   *                 given in constructor bounds array.
   * @return Lower bound for the given variable
   */
  public int getLB(int varIndex) {
    return domainBounds[varIndex][0];
  }

  /**
   * Get the upper bound for a variable
   * 
   * @param varIndex Variable to get upper bound for. Indexed in the same order as
   *                 given in constructor bounds array
   * @return Upper bound for the given variable
   */
  public int getUB(int varIndex) {
    return domainBounds[varIndex][1];
  }

  /**
   * Get the list of constraints
   * 
   * @return Array list of BinaryConstraints
   */
  public ArrayList<BinaryConstraint> getConstraints() {
    return constraints;
  }

}

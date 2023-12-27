package com.example.cspsolver;

/**
 * Assumes tuple values are integers
 */
public final class BinaryTuple {
  private int val1, val2;

  public BinaryTuple(int v1, int v2) {
    val1 = v1;
    val2 = v2;
  }

  public String toString() {
    return "<" + val1 + ", " + val2 + ">";
  }

  /**
   * Compares tuple values to given integers
   * 
   * @param v1 The first value in the tuple
   * @param v2 The second value in the tuple
   * @return Wether the two values match the tuple
   */
  public boolean matches(int v1, int v2) {
    return (val1 == v1) && (val2 == v2);
  }
}

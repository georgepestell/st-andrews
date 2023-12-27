package com.example.cspsolver;

import java.util.ArrayList;
import java.util.stream.IntStream;

// TODO:  https://people.eng.unimelb.edu.au/pstuckey/papers/autopt.pdf

public class Domain {
    public enum VAL_HEURISTIC {
        ASCENDING
    }

    private int[] domain;
    private int[] map;
    private int baseLB;
    private int lowerBound;
    public int size;

    public Domain(int lower_bound, int upper_bound) throws IllegalArgumentException {

        // Ensure the bounds are positive
        if (lower_bound < 0 || upper_bound < 0) {
            throw new IllegalArgumentException("Variable domains cannot include negative values");
        }

        // Ensure the bounds are increasing
        if (upper_bound < lower_bound) {
            throw new IllegalArgumentException("Variable domain lower bound cannot be greater than its upper bound");
        }

        this.domain = IntStream.rangeClosed(lower_bound, upper_bound).toArray();
        this.map = IntStream.rangeClosed(0, domain.length - 1).toArray();
        this.size = domain.length;
        this.baseLB = lower_bound;
        this.lowerBound = lower_bound;
    }

    public Boolean contains(int v) {
        return map[v - baseLB] < size;
    }

    public int get(int i) {
        return domain[i];
    }

    private void swap(int i, int j) {
        int tmp_i = domain[i];
        domain[i] = domain[j];
        domain[j] = tmp_i;
        map[domain[i] - baseLB] = i;
        map[domain[j] - baseLB] = j;
    }

    public void bind(int v) {
        if (map[v - baseLB] >= size) {
            size = 0;
        } else {
            swap(map[v - baseLB], 0);
            size = 1;
        }
    }

    private void updateLB() {
        if (!isEmpty()) {
            int lb = domain[0];
            for (int i = 0; i < size; i++) {
                if (domain[i] < lb) {
                    lb = domain[i];
                }
            }
            this.lowerBound = lb;
        }
    }

    /**
     * Restore the domain to a previous state
     * 
     * @param old_size the previous size of the domain
     */
    public void restore(int old_size) {
        // Restore can't remove elements from the domain
        if (size > old_size) {
            throw new IndexOutOfBoundsException("Cannot restore to smaller domain");
        }

        // Check for lower bound
        for (int i = size; i < old_size; i++) {
            if (domain[i] < lowerBound)
                lowerBound = domain[i];
        }

        size = old_size;
    }

    /**
     * Remove value from the domain
     */
    public void remove(int v) {
        if (map[v - baseLB] < size) {
            // Put it at the end of the current domain, and decrement the size to remove
            swap(map[v - baseLB], size - 1);
            size--;

            // Check if it was the lowerBound, and search for a new one if it was
            if (v <= lowerBound) {
                updateLB();
            }

        } else {
            System.err.println("WARN: Value " + v + " already removed from domain");
        }
    }

    public int selectValue(VAL_HEURISTIC heuristic) {

        // Make sure the domain is not already empty
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("Domain is empty");
        }

        // Pick value based on the heuristic FLAG
        // TODO: more heuristics
        switch (heuristic) {
            // Default to ASCENDING (pick smallest value)
            default:
                return lowerBound;
        }

    }

    /**
     * Checks whether the domain is empty
     */
    public boolean isEmpty() {
        return this.size <= 0;
    }

    // Create an array copy of the current domain
    public ArrayList<Integer> toArray() {
        ArrayList<Integer> dArray = new ArrayList<Integer>();
        for (int i = 0; i < this.size; i++) {
            dArray.add(domain[i]);
        }
        return dArray;
    }
}

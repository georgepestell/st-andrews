package com.example.cspsolver;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.cspsolver.Domain.VAL_HEURISTIC;

/**
 * Uses 2-way branching to solve CSPs made of binary constraints
 */
public class BinarySolver {

    // The options for variable selection heuristic
    public enum VAR_HEURISTIC {
        ASCENDING, SMALLEST_DOMAIN_FIRST
    }

    // Problem definition
    private BinaryCSP csp;

    // Current variable and value heuristics
    public VAR_HEURISTIC varHeuristic;
    public VAL_HEURISTIC valHeuristic;

    // Solver workload logging
    public int arcRevisions = 0;
    public int solverNodes = 0;

    // Queue of ARC revisions for MAC
    private ArrayList<BinaryArc> queue = new ArrayList<BinaryArc>();

    // Queue of unassigned variables
    private ArrayList<Integer> varList = new ArrayList<Integer>();

    // Domains represented by sparse-sets
    // for each variable (indexed by var input order)
    private Domain[] domains;

    /**
     * BinarySolver constructor. Ensures valid setup and ensures initial global ARC
     * consistency.
     * 
     * @param csp          CSP configuration
     * @param varHeuristic Variable selection heuristic
     * @param valHeuristic Value selection heuristic
     */
    public BinarySolver(BinaryCSP csp, VAR_HEURISTIC varHeuristic, VAL_HEURISTIC valHeuristic) {

        // Validate input CSP
        if (csp == null) {
            throw new InvalidParameterException("Cannot solve a null CSP");
        }

        // Basic solution requirements
        this.varHeuristic = varHeuristic;
        this.valHeuristic = valHeuristic;
        this.csp = csp;

        // Setup each value domains
        this.initializeDomains();

        // Ensure initial global ARC consistency
        this.initializeARCQueue();
        AC3();

    }

    /**
     * Setup the domains for each decision variable based on the given CSP
     * configuration
     */
    private void initializeDomains() {

        // Make sure the CSP is valid
        if (csp == null) {
            throw new NullPointerException("Cannot initialize domains when CSP is invalid");
        }

        // Setup outer array for variable domains
        domains = new Domain[csp.getNoVariables()];
        // Setup each variable's sparse-set domains
        for (int i = 0; i < csp.getNoVariables(); i++) {
            this.domains[i] = new Domain(csp.getLB(i), csp.getUB(i));
            this.varList.add(i);
        }
    }

    /**
     * Initialize the ARC queue for the first time by propagating all ARCs from the
     * constraints given by the CSP configuraiton
     */
    private void initializeARCQueue() {

        // Make sure the CSP is valid
        if (csp == null) {
            throw new NullPointerException("Cannot initialize ARC queue when CSP is invalid");
        }

        // Find all inconsistent var domain value pairs
        for (BinaryConstraint c : this.csp.getConstraints()) {
            queue.add(new BinaryArc(c, c.firstVar, c.secondVar, true));
            queue.add(new BinaryArc(c, c.secondVar, c.firstVar, false));
        }
    }

    /**
     * Add all the ARCS pointing to a source variable to the queue, ignoring the ARC
     * from the support to the source
     * 
     * @param sourceVar
     * @param supportVar
     */
    public void addArcs(int sourceVar, int supportVar) {
        // Queue all arcs pointing to the source
        for (BinaryConstraint c : csp.getConstraints()) {
            BinaryArc newArc = null;
            if (c.firstVar == sourceVar && c.secondVar != supportVar) {
                newArc = new BinaryArc(c, c.secondVar, c.firstVar, false);
            } else if (c.secondVar == sourceVar && c.firstVar != supportVar) {
                newArc = new BinaryArc(c, c.firstVar, c.secondVar, true);
            }
            if (newArc != null && !queue.contains(newArc)) {
                queue.add(newArc);
            }
        }
    }

    /**
     * Add all ARCS pointing to an updated source variable to the queue and then
     * maintain
     * ARC consistency
     * 
     * @param sourceVar The variable whose domain has been updated. ARCs pointing to
     *                  this variable are added to the queue
     * @return Returns true if no domains are empty, or false if the value
     *         assignation has lead to an
     *         unsolvable sub-CSP.
     */
    public boolean propagate(int sourceVar) {
        // Queue all arcs pointing to the source
        for (BinaryConstraint c : csp.getConstraints()) {
            BinaryArc newArc = null;
            if (c.firstVar == sourceVar) {
                newArc = new BinaryArc(c, c.secondVar, c.firstVar, false);
            } else if (c.secondVar == sourceVar) {
                newArc = new BinaryArc(c, c.firstVar, c.secondVar, true);
            }
            if (newArc != null && !queue.contains(newArc)) {
                queue.add(newArc);
            }
        }
        return AC3();
    }

    /**
     * Maintains ARC consistency by emptying the ARC queue, adding ARCs pointing to
     * a variable when it's domain changes.
     * 
     * @return Whether the CSP is ARC consistent. True if queue is emptied and all
     *         unassigned variables are not empty. False if the CSP is unsolvable.
     */
    public boolean AC3() {
        // Propagate each arc in the queue
        while (!queue.isEmpty()) {

            // Get the front of the queue
            BinaryArc arc = queue.get(0);

            // DEBUG: System.out.println("arc(" + arc.source + ", " + arc.support + ")");

            int sourceVar = arc.source;
            int supportVar = arc.support;

            // Enforce the arc for each item in the source domain
            for (int sourceVal : domains[sourceVar].toArray()) {
                boolean consistent = false;
                for (int supportVal : domains[supportVar].toArray()) {
                    if (arc.check(sourceVal, supportVal)) {
                        consistent = true;
                        break;
                    }
                }

                // Update domain if the source value has no support
                if (!consistent) {
                    // Remove the value from the source domain
                    domains[sourceVar].remove(sourceVal);
                    if (domains[sourceVar].isEmpty()) {
                        // System.err.println("Empty domain for " + sourceVar);
                        return false;
                    }
                    addArcs(sourceVar, supportVar);
                }
            }

            // Remove arc from the queue
            queue.remove(0);
            arcRevisions++;
        }
        return true;
    }

    /**
     * Debugging function - prints the current domain or assigned value for each
     * variable
     */
    public void printDomains() {
        for (int i = 0; i < domains.length; i++) {
            if (varList.contains(i)) {
                System.out.println("Domain for " + i + ": " + domains[i].toArray());
            } else {
                System.out.println("Assigned var " + i + ": " + domains[i].get(0));
            }
        }
    }

    /**
     * Uses the defined variable heuristic to select a variable to assign from the
     * list of currently unassigned variables.
     * 
     * @return the selected variable. Indexed in the order given by the CSP
     *         configuration.
     */
    private int selectVar() {

        // Make sure there is a variable to pick
        if (varList.size() <= 0) {
            throw new IndexOutOfBoundsException("Cannot select var when all are assigned");
        }

        switch (varHeuristic) {
            // Pick variable with the smallest domain
            case SMALLEST_DOMAIN_FIRST:

                int smallestSize = domains[varList.get(0)].size;
                int smallest = varList.get(0);

                for (int var : varList) {

                    int size = domains[var].size;
                    if (size < smallestSize) {
                        smallestSize = size;
                        smallest = var;
                    }
                }

                return smallest;

            // Default to ASCENDING (queue order given by CSP)
            default:
                return varList.get(0);
        }
    }

    /**
     * Print the generated solution
     * 1. Number of nodes checked passed
     * 2. Number of ARC revisions propagated
     * 3. Assigned value for each variable
     */
    private void printSolution() {
        System.out.println("SolverNodes: " + this.solverNodes);
        System.out.println("ARCRevisions: " + this.arcRevisions);
        for (int i = 0; i < csp.getNoVariables(); i++) {
            System.out.println(domains[i].get(0));
        }
    }

    /**
     * Use forward checking and MAC to search for a valid solution
     * 
     */
    public boolean solve() {

        // Check if we have reached a valid solution
        if (varList.isEmpty()) {
            printSolution();
            return true;
        }

        // Store the sizes of all applied var domains (for backtracking)
        HashMap<Integer, Integer> old_sizes = new HashMap<Integer, Integer>(varList.size());
        for (int i = 0; i < varList.size(); i++) {
            old_sizes.put(varList.get(i), domains[varList.get(i)].size);
        }

        /*
         * LEFT BRANCH
         */

        solverNodes++;

        // Pick a variable
        int var = selectVar();

        // Check if the domain is Empty
        if (domains[var].isEmpty()) {
            return false;
        }

        // Pick a value for the variable
        int val = domains[var].selectValue(valHeuristic);

        // Assign the value to the variable
        varList.remove(varList.indexOf(var));
        domains[var].bind(val);

        // Propagate the changes
        if (propagate(var)) {
            // Assign the next variable
            // Do not continue looking for solutions if one is found
            if (solve()) {
                return true;
            }
        }

        /*
         * RIGHT BRANCH
         */

        solverNodes++;

        // Undo propagation domain changes
        for (int v : old_sizes.keySet()) {
            int old_size = old_sizes.get(v);
            domains[v].restore(old_size);
        }

        // Remove the tried value from the domain
        // and unassign value
        // Added to end of the list for ascending binary order
        domains[var].remove(val);
        varList.add(var);

        // Pick another variable/value to try
        return solve();
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java Solver <file.csp>");
            return;
        }

        BinaryCSPReader reader = new BinaryCSPReader();
        BinaryCSP csp = reader.readBinaryCSP(args[0]);

        BinarySolver solver = new BinarySolver(csp, VAR_HEURISTIC.ASCENDING, VAL_HEURISTIC.ASCENDING);

        long startTime = System.currentTimeMillis();
        solver.solve();
        long endTime = System.currentTimeMillis();

        System.out.println("TotalTime: " + (endTime - startTime));
    }

}

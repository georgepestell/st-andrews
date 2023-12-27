package com.example.cspsolver;

/**
 * Class representing a directed constraint ARC from a source to support node.
 */
public class BinaryArc {
    private BinaryConstraint constraint;
    public int source, support;
    private boolean isForwards;

    /**
     * Generates an ARC instance
     * 
     * @param c          The source constraint (one constraint formed of two ARCS)
     * @param source     The source of the ARC. Has it's domain updated
     * @param support    The target of the ARC. Supports the source domain.
     * @param isForwards the direction of the ARC relative to the ordering the order
     *                   defined in the CSP file
     */
    public BinaryArc(BinaryConstraint c, int source, int support, boolean isForwards) {
        this.constraint = c;
        this.source = source;
        this.support = support;
        this.isForwards = isForwards;
    }

    /**
     * Check if a source and support value saisfy the ARC.
     * 
     * @param sourceVal  Value from the source domain
     * @param supportVal Value from the support domain
     * @return Whether the support value supports the source value
     */
    public boolean check(int sourceVal, int supportVal) {
        if (isForwards) {
            return this.constraint.check(sourceVal, supportVal);
        } else {
            return this.constraint.check(supportVal, sourceVal);
        }
    }

    public boolean equals(Object o) {

        if (!(o instanceof BinaryArc)) {
            return false;
        }

        BinaryArc arc = (BinaryArc) o;
        if (this.source == arc.source && this.support == arc.support) {
            return true;
        }

        return false;
    }
}
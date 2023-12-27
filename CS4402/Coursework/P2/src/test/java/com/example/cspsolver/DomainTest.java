package com.example.cspsolver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.cspsolver.Domain.VAL_HEURISTIC;

public class DomainTest {

    private int lb = 0;
    private int ub = 30;
    private int size = ub - lb + 1;
    private Domain d;

    @BeforeEach
    public void setupDomain() {
        d = new Domain(lb, ub);
    }

    @Test
    public void domainSizeIsInclusiveTest() {
        assertEquals(size, d.size);
    }

    @Test
    public void throwsExceptionOnNegativeTest() {
        int lb = -3;
        int ub = 10;
        assertThrows(IllegalArgumentException.class, () -> new Domain(lb, ub));
    }

    @Test
    public void throwsExceptionOnDecrementingDomainTest() {
        int lb = 7;
        int ub = 3;

        assertThrows(IllegalArgumentException.class, () -> new Domain(lb, ub));

    }

    @Test
    public void singleElementDomainOnEqualUBLBTest() {
        int lb = 7;
        int ub = 7;
        Domain d = new Domain(lb, ub);
        assertEquals(1, d.size);
    }

    @Test
    public void domainIncludesCorrectRangeTest() {

        int lb = 3;
        int ub = 10;

        Domain d = new Domain(lb, ub);

        for (int i = lb; i <= ub; i++) {
            assertTrue(d.contains(i));
        }

    }

    @Test
    public void removingValueDecrementsSizeTest() {
        d.remove(0);
        assertEquals(size - 1, d.size);
    }

    // Check that removing the last element makes the domain empty
    @Test
    public void removingLastValueIsEmptyTest() {
        int value = 8;
        d = new Domain(value, value);
        d.remove(value);
        assertTrue(d.isEmpty());
    }

    // Check that removing an element just outside of the range throws an exception
    @Test
    public void throwsExceptionOnRemovingOutOfBoundsTest() {
        assertThrows(IndexOutOfBoundsException.class, () -> d.remove(ub + 1));
    }

    // Check removing many elements till empty
    @Test
    public void removingManyTillEmptyTest() {
        int i = 0;
        for (int v = lb; v <= ub; v++) {
            d.remove(v);
            assertEquals(size - (++i), d.size);
        }
        assertTrue(d.isEmpty());
    }

    // Check that assigning a value to a domain restricts its size
    @Test
    public void bindingValueSetsSizeToOneTest() {
        d.bind(15);
        assertEquals(1, d.size);
    }

    // Check selecting a value from the domain with incremental ordering gets the
    // smallest value
    @Test
    public void selectValueIncrementalTest() {
        int value = d.selectValue(VAL_HEURISTIC.ASCENDING);
        assertEquals(lb, value);
    }

    // Test a more complex series of removals of the lower bound value
    @Test
    void selectValueReturnsSmallestAfterRemovalsTest() {
        // Remove the lower bound, first, and third value from lower bound
        int value = d.selectValue(VAL_HEURISTIC.ASCENDING);
        d.remove(value);
        d.remove(value + 1);
        d.remove(value + 3);

        // Check the second value from lower bound is selected
        value = d.selectValue(VAL_HEURISTIC.ASCENDING);
        assertEquals(lb + 2, value);

        // remove the second value from lower bound
        d.remove(value);

        // Check the fourth smallets value from the lower bound is selected,
        // skipping the already removed third value from the lower bound
        value = d.selectValue(VAL_HEURISTIC.ASCENDING);
        assertEquals(lb + 4, value);

    }

}

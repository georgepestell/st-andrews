package impl;

import interfaces.IDoubleStack;
import interfaces.IStack;

/** Implements a single array of a given size to create two equally sized stacks. */
public class ArrayDoubleStack implements IDoubleStack {

    /** The underlying array holding both stacks. */
    private Object[] stackArray;

    /**
     * The first stack instance which will use the first half of the underlying
     * array.
     */
    private IStack firstStack;

    /**
     * The second stack instance which will use the second half of the underlying
     * array.
     */
    private IStack secondStack;

    /**
     * Constructor creates an array of a given size and then sets the two stacks
     * sizes to an equal even number.
     * 
     * @param maxSize The total size of the underlying array. This is divided into
     *                two evenly sized stacks. "Odd" values will be decremented to prevent erroneous array slots.
     */
    public ArrayDoubleStack(int maxSize) {

        // Odd maxSize values are decremented to prevent erroneous array slots when split into two evenly sized stacks
        if (maxSize % 2 != 0) {
            maxSize--;
        }

        // Create the underlying array of the size of the two stacks which will have a
        // given maximum size
        this.stackArray = new Object[maxSize];

        // Setup both stack instances.
        this.firstStack = new Stack(this.stackArray, true);
        this.secondStack = new Stack(this.stackArray, false);

    }

    @Override
    public IStack getFirstStack() {
        return this.firstStack;
    }

    @Override
    public IStack getSecondStack() {
        return this.secondStack;
    }
}

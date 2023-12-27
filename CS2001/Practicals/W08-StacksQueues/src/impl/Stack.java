package impl;

import common.StackEmptyException;
import common.StackOverflowException;
import interfaces.IStack;

/**
 * Creates a stack using half of an array of an array implementation for a
 * double stack.
 */
public class Stack implements IStack {

    /**
     * Flag indicating which stack is being used. Determines which half of the
     * stackArray is used.
     */
    private final boolean isFirstStack;

    /** Max Size of the single stack. */
    private int maxSize;

    /** The underlying DoubleStack array. */
    private Object[] stackArray;

    /** The current size of the stack. */
    private int currentSize;

    /** The index in the stackArray for this stack's top element. */
    private int topPosition;

    /**
     * Constructor setting up the Stack.
     * 
     * @param stackArray   The underlying array of the double stack.
     * @param isFirstStack Flag determining which half of the {@link stackArray} is
     *                     used.
     */
    public Stack(Object[] stackArray, boolean isFirstStack) {
        this.isFirstStack = isFirstStack;

        // Single stacks use half of the underlying double stack array, rounds down to
        // prevent the stack using more than half the array
        this.maxSize = Math.floorDiv(stackArray.length, 2);
        this.stackArray = stackArray;

        // Set the initial size of a stack to 0
        this.currentSize = 0;

        // First stack fills up from index 0, second from the middle.
        // - 1 is used as topPosition increments before pushing and currentSize is
        // checked before popping so it won't cause exceptions.
        if (isFirstStack) {
            topPosition = -1;
        } else {
            topPosition = this.maxSize - 1;
        }
    }

    @Override
    public void push(Object element) throws StackOverflowException {
        // If the stack is full, nothing can be pushed
        if (currentSize >= this.maxSize) {
            throw new StackOverflowException();
        }

        // Increment topPosition
        topPosition++;

        // Insert the element into the empty topPosition slot
        this.stackArray[this.topPosition] = element;

        // Increment currentSize
        currentSize++;

    }

    @Override
    public Object pop() throws StackEmptyException {
        // If the stack is empty, nothing can be popped
        if (isEmpty()) {
            throw new StackEmptyException();
        }

        // Temporarily store the top element
        Object element = this.top();

        // Remove the top element
        this.stackArray[topPosition] = null;

        // Decrement the currentSize
        this.currentSize--;

        // Decrement top position of the stack
        topPosition--;

        // Return the stored stop element
        return element;
    }

    @Override
    public Object top() throws StackEmptyException {
        if (isEmpty()) {
            throw new StackEmptyException();
        }
        return stackArray[topPosition];
    }

    @Override
    public int size() {
        return currentSize;
    }

    @Override
    public boolean isEmpty() {
        // Return true if the stack contains no elements
        if (currentSize <= 0) {
            return true;
        }

        return false;
    }

    @Override
    public void clear() {
        try {
            while (!isEmpty()) {
                pop();
            }
        } catch (StackEmptyException e) {
            // This should never occur if isEmpty is implemented correctly
            System.err.println("Fatal Error: exception while popping from non-empty stack");
        }
    }

}

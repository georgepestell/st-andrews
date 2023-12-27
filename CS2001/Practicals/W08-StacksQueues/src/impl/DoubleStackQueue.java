package impl;

import common.QueueEmptyException;
import common.QueueFullException;
import common.StackEmptyException;
import common.StackOverflowException;

import interfaces.IDoubleStack;
import interfaces.IQueue;
import interfaces.IStack;

/** Implements a double stack to create a queue with a given maximum size. */
public class DoubleStackQueue implements IQueue {

    /** The input stack. */
    private IStack inputStack;

    /** The output stack. */
    private IStack outputStack;

    /** The maximum number of elements which can be in the queue at one time. */
    private int maxSize;

    /** The current number of elements queued. */
    private int currentSize;

    /**
     * Constructor creates a queue with a given maximum size.
     * 
     * @param maxSize The maximum number of elements which can be enqueued at one
     *                time.
     */
    public DoubleStackQueue(int maxSize) {

        // The queue is empty on initialisation
        this.currentSize = 0;
        this.maxSize = maxSize;

        // Create the underlying double stack. The maxSize is multiplied by two to
        // ensure that the queue can always be filled despite how often dequeue is
        // called.
        IDoubleStack doubleStack = new ArrayDoubleStack(maxSize * 2);

        // Set the input and output stack attributes from the double stack instance
        this.inputStack = doubleStack.getFirstStack();
        this.outputStack = doubleStack.getSecondStack();
    }

    @Override
    public void enqueue(Object element) throws QueueFullException {
        // Makes sure the number of elements in the input and output queues doesn't
        // exceed the max queue size
        if (this.currentSize >= this.maxSize) {
            throw new QueueFullException();
        }

        try {
            // Try to add the element to the queue
            this.inputStack.push(element);
            this.currentSize++;
        } catch (StackOverflowException e) {
            // This should never be possible as the input stack can hold the maxSize number
            // of elements which we have already checked for
            System.err.println("Fatal Error: exception while pushing to input stack");
        }
    }

    @Override
    public Object dequeue() throws QueueEmptyException {
        // If the queue is empty, nothing can be dequeued
        if (isEmpty()) {
            throw new QueueEmptyException();
        }

        // -- If the output stack is empty, pop all the input stack elements to the
        // output stack --

        if (this.outputStack.isEmpty()) {
            // Pop all elements from the input stack to the output stack
            while (!this.inputStack.isEmpty()) {
                try {
                    this.outputStack.push(this.inputStack.pop());
                } catch (StackOverflowException e) {
                    // This should never be possible as we have already made sure the output stack
                    // is empty
                    System.err.println("Fatal Error: exception while pushing to not-full output stack");
                } catch (StackEmptyException e) {
                    // This should never be possible as we have already make sure the input stack is
                    // not empty
                    System.err.println("Fatal Error: exception while popping from non-empty input stack");
                }
            }
        }

        // -- Return the top element from the output stack --

        try {
            // Pop the top element from the output queue and decrement the current size
            Object element = this.outputStack.pop();
            this.currentSize--;

            // Return the popped element
            return element;

        } catch (StackEmptyException e) {
            // This should never be possible as we have already checked that the output
            // stack is not empty
            System.err.println("Fatal Error: exception while popping from non-empty output stack");
            return null;
        }
    }

    @Override
    public int size() {
        return currentSize;
    }

    @Override
    public boolean isEmpty() {
        // Check whether the input and output stacks are both empty
        if (this.inputStack.isEmpty() && this.outputStack.isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        // Clear both input and output stacks
        this.inputStack.clear();
        this.outputStack.clear();

        // Reset the current size attribute
        this.currentSize = 0;
    }
}

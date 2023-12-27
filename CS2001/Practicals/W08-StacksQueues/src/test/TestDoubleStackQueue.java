package test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import common.AbstractFactoryClient;
import common.QueueEmptyException;
import common.QueueFullException;
import interfaces.IQueue;

/**
 * Tests double stack queue implementation.
 */
public class TestDoubleStackQueue extends AbstractFactoryClient {

    /** The constant default maximum size of the queue. */
    private static final int DEFAULT_MAX_SIZE = 10;

    /**
     * The default standard queue instance which is initialised before each test in
     * {@link setupQueue}.
     */
    private IQueue queue;

    /** Sets up a standard empty queue instance before each test. */
    @BeforeEach
    private void setupQueue() {
        queue = getFactory().makeDoubleStackQueue(DEFAULT_MAX_SIZE);
    }

    /**
     * Tests that the factory constructs a non-null queue item.
     */
    @Test
    public void factoryReturnsNonNullDoubleStackQueue() {
        assertNotNull(queue, "Failure: IFactory.makeDoubleStackQueue returns null, expected non-null object");
    }

    /**
     * Tests that the factory throws an exception with a negative maxSize parameter.
     */
    @Test
    public void factoryThrowsExceptionOnNegativeMaxSize() {
        assertThrows(NegativeArraySizeException.class, () -> getFactory().makeDoubleStackQueue(-1));
    }

    /**
     * Tests that the initial size of the queue is 0 and can be retrieved.
     */
    @Test
    public void initialSizeIsZero() {
        assertEquals(0, queue.size());
    }

    /** Tests that the queue is initially classed as empty. */
    @Test
    public void initialDoubleStackIsEmpty() {
        assertTrue(queue.isEmpty());
    }

    /**
     * Tests that an element can be enqueued to the input stack.
     * 
     * @throws QueueFullException Thrown if the enqueue fails.
     */
    @Test
    public void enqueueOneElement() throws QueueFullException {
        // Enqueue a new element
        queue.enqueue(new Object());

        // Make sure the size has increased
        assertEquals(1, queue.size());
    }

    /**
     * Tests that the queue is not considered empty when a single element is
     * enqueued.
     * 
     * @throws QueueFullException Thrown if the enqueue fails.
     */
    @Test
    public void doubleStackNotEmptyOnEnqueue() throws QueueFullException {
        // Enqueue a new element
        queue.enqueue(new Object());

        // Make sure the size has increased
        assertFalse(queue.isEmpty());
    }

    /**
     * Tests that the entire queue can be filled with an odd maxSize value.
     * 
     * @throws QueueFullException Thrown if the enqueue fails.
     */
    @Test
    public void fillQueueWithEvenMaxSize() throws QueueFullException {
        // Set the max size to an even value
        int maxSize = 10;

        // Make the queue with the given "even" size
        this.queue = getFactory().makeDoubleStackQueue(maxSize);

        for (int i = 1; i <= maxSize; i++) {
            queue.enqueue(new Object());
            assertEquals(i, queue.size());
        }

        assertEquals(maxSize, queue.size());
    }

    /**
     * Tests that the entire queue can be filled with an odd maxSize value.
     * 
     * @throws QueueFullException Thrown if the enqueue fails.
     */
    @Test
    public void fillQueueWithOddMaxSize() throws QueueFullException {
        // Set the max size to an odd value
        int maxSize = 9;

        // Make the queue with the given "even" size
        this.queue = getFactory().makeDoubleStackQueue(maxSize);

        for (int i = 1; i <= maxSize; i++) {
            queue.enqueue(new Object());
            assertEquals(i, queue.size());
        }

        assertEquals(maxSize, queue.size());
    }

    /**
     * Tests that an exception is thrown when attempting to enqueue when the queue
     * is full with all elements in the input stack.
     * 
     * @throws QueueFullException Thrown if the initial enqueuing fails which
     *                            shouldn't happen.
     */
    @Test
    public void enqueueOnFullQueueInInputStackThrowsException() throws QueueFullException {
        // Fill the stack
        for (int i = 1; i <= DEFAULT_MAX_SIZE; i++) {
            queue.enqueue(new Object());
        }

        // Check that the queue is full
        assertEquals(DEFAULT_MAX_SIZE, queue.size());

        // Make sure the correct exception is thrown when full
        assertThrows(QueueFullException.class, () -> queue.enqueue(new Object()));
    }

    /**
     * Tests that an item can be dequeued from the list when the output queue is
     * empty.
     * 
     * @throws QueueFullException  Thrown if the initial enqueuing fails.
     * @throws QueueEmptyException Thrown if the dequeuing fails.
     */
    @Test
    public void dequeueElementWithEmptyOutputStack() throws QueueFullException, QueueEmptyException {
        // Create an object to add to the queue
        Object element = new Object();

        // Add a few elements to the input stack
        queue.enqueue(element);
        queue.enqueue(new Object());
        queue.enqueue(new Object());

        assertEquals(3, queue.size());

        // Dequeue to get the first element enqueued
        assertEquals(element, queue.dequeue());

        // Make sure the size decreases
        assertEquals(2, queue.size());
    }

    /**
     * Tests that the queue is considered empty once a single element is enqueued
     * and dequeued.
     * 
     * @throws QueueFullException  Thrown if the initial enqueuing fails.
     * @throws QueueEmptyException Thrown if the dequeuing fails.
     */
    @Test
    public void doubleStackIsEmptyOnDequeue() throws QueueFullException, QueueEmptyException {
        // Enqueue a new element
        queue.enqueue(new Object());

        // Dequeue the element
        queue.dequeue();

        // Make sure the size has increased
        assertTrue(queue.isEmpty());
    }

    /**
     * Tests that an exception is thrown when attempting to enqueue an element with
     * a full queue with elements in both the input and output stacks.
     * 
     * @throws QueueFullException  Thrown if the initial enqueuing fails.
     * @throws QueueEmptyException Thrown if the dequeuing fails.
     */
    @Test
    public void enqueueOnFullQueueBetweenStacksThrowsException() throws QueueFullException, QueueEmptyException {
        // Fill the input stack with the maximum number of elements
        for (int i = 0; i < DEFAULT_MAX_SIZE; i++) {
            queue.enqueue(new Object());
        }

        // Dequeue an element which pops all input stack elements to the output stack
        // and removes the oldest element
        queue.dequeue();

        // Fill the queue by adding one element to the input stack
        queue.enqueue(new Object());

        // Make sure the correct exception is thrown when adding to the full queue
        assertThrows(QueueFullException.class, () -> queue.enqueue(new Object()));

    }

    /**
     * Tests that the oldest item can be dequeued from the list when the output
     * queue is not empty.
     * 
     * @throws QueueFullException  Thrown if the initial enqueuing fails.
     * @throws QueueEmptyException Thrown if the dequeuing fails.
     */
    @Test
    public void dequeueElementWithElementsInOutputStack() throws QueueFullException, QueueEmptyException {
        // Create an object to add to the queue
        Object element = new Object();

        // Add a few items to the input stack
        queue.enqueue(element);
        queue.enqueue(new Object());

        assertEquals(2, queue.size());

        // Dequeue the item added
        assertEquals(element, queue.dequeue());

        // Make sure the size decreases
        assertEquals(1, queue.size());
    }

    /**
     * Tests that a non-empty queue with items only in input stack can be cleared
     * correctly.
     * 
     * @throws QueueFullException Thrown if the initial enqueuing fails.
     */
    @Test
    public void clearQueueWithElementsInInputStack() throws QueueFullException {
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.enqueue(new Object());

        assertEquals(3, queue.size());
        assertFalse(queue.isEmpty());

        queue.clear();

        assertEquals(0, queue.size());
        assertTrue(queue.isEmpty());
    }

    /**
     * Tests that a non-empty queue with items only in output stack can be cleared
     * correctly.
     * 
     * @throws QueueFullException  Thrown if the initial enqueuing fails.
     * @throws QueueEmptyException Thrown if the dequeuing fails.
     */
    @Test
    public void clearQueueWithElementsInOutputStack() throws QueueFullException, QueueEmptyException {
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.enqueue(new Object());

        queue.dequeue();

        assertEquals(3, queue.size());
        assertFalse(queue.isEmpty());

        queue.clear();

        assertEquals(0, queue.size());
        assertTrue(queue.isEmpty());
    }

    /**
     * Tests that a non-empty queue with items in the input and output stacks can be
     * cleared correctly.
     * 
     * @throws QueueFullException  Thrown if the initial enqueuing fails.
     * @throws QueueEmptyException Thrown if the dequeuing fails.
     */
    @Test
    public void clearQueueWithElementsInBothStacks() throws QueueFullException, QueueEmptyException {
        // Enqueue items to the input queue
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.enqueue(new Object());

        // Move all current elements to the output queue and dequeue the oldest
        queue.dequeue();

        // Add an item to the input queue
        queue.enqueue(new Object());

        assertEquals(3, queue.size());
        assertFalse(queue.isEmpty());

        queue.clear();

        assertEquals(0, queue.size());
        assertTrue(queue.isEmpty());
    }

    /** Tests initalising zero-sized queues. */
    @Test
    public void initializeZeroSizedQueue() {
        this.queue = getFactory().makeDoubleStackQueue(0);

        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
    }

    /** Tests enqueuing to zero-sized queues throws an exception. */
    @Test
    public void enqueueToZeroSizedQueueThrowsException() {
        this.queue = getFactory().makeDoubleStackQueue(0);

        assertThrows(QueueFullException.class, () -> queue.enqueue(new Object()));
    }

    /** Tests enqueuing to zero-sized queues throws an exception. */
    @Test
    public void dequeueFromZeroSizedQueueThrowsException() {
        this.queue = getFactory().makeDoubleStackQueue(0);

        assertThrows(QueueEmptyException.class, () -> queue.dequeue());
    }
}

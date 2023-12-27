package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import common.AbstractFactoryClient;
import common.StackEmptyException;
import common.StackOverflowException;
import impl.ArrayDoubleStack;
import interfaces.IDoubleStack;

/**
 * Tests array collection implementation.
 */
public class TestArrayDoubleStack extends AbstractFactoryClient {

    /** The constant for the deafult maximum size of the double stack. */
    private static final int DEFAULT_MAX_SIZE = 10;

    /**
     * The default doubleStack instance which is initialised before every test in
     * {@link setupDoubleStack}.
     */
    private IDoubleStack doubleStack;

    /**
     * Sets up a standard empty doubleStack instance before each test with a default
     * size.
     */
    @BeforeEach
    public void setupDoubleStack() {
        this.doubleStack = getFactory().makeDoubleStack(DEFAULT_MAX_SIZE);
    }

    /**
     * Tests that the factory constructs a non-null double stack.
     */
    @Test
    public void factoryReturnsNonNullDoubleStackObject() {
        assertNotNull(this.doubleStack, "Failure: DoubleStack.getFirstStack returns null, expected non-null element");
    }

    /**
     * Tests that the factory throws an exception with a negative maxSize parameter.
     */
    @Test
    public void factoryThrowsExceptionOnNegativeMaxSize() {
        assertThrows(NegativeArraySizeException.class, () -> getFactory().makeDoubleStack(-1));
    }

    /**
     * Tests that the DoubleStack can return a not-null first stack of an
     * ArrayDoubleStack.
     */
    @Test
    public void doubleStackReturnsNotNullFirstStack() {
        assertNotNull(this.doubleStack.getFirstStack(),
                "Failure: DoubleStack.getFirstStack returns null, expected non-null element");
    }

    /**
     * Tests that the DoubleStack can return a not-null second stack of an
     * ArrayDoubleStack.
     */
    @Test
    public void doubleStackReturnsNotNullSecondStack() {
        assertNotNull(this.doubleStack.getSecondStack(),
                "Failure: DoubleStack.getSecondStack returns null, expected non-null element");
    }

    /** Tests the initial size of the first stack of an ArrayDoubleStack is 0. */
    @Test
    public void initialSizeOfFirstStack() {
        assertEquals(0, this.doubleStack.getFirstStack().size());
    }

    /** Tests the initial size of the second stack is 0. */
    @Test
    public void initialSizeOfSecondStack() {
        assertEquals(0, this.doubleStack.getSecondStack().size());
    }

    /**
     * Tests the isEmpty function for first stack of an ArrayDoubleStack is true
     * after initialisation.
     */
    @Test
    public void initialIsEmptyValueForFirstStack() {
        assertEquals(true, this.doubleStack.getFirstStack().isEmpty());
    }

    /**
     * Tests the isEmpty function for second stack of an ArrayDoubleStack is true
     * after initialisation.
     */
    @Test
    public void initialIsEmptyValueForSecondStack() {
        assertEquals(true, this.doubleStack.getSecondStack().isEmpty());
    }

    /**
     * Tests that an element can be pushed onto the first stack of an
     * ArrayDoubleStack.
     * 
     * @throws StackOverflowException Is thrown if pushing to the first stack fails.
     */
    @Test
    public void pushOneItemToFirstStack() throws StackOverflowException {
        // Try to push an element onto the stack
        this.doubleStack.getFirstStack().push(new Object());

        // Check the size of the stack has increased
        assertEquals(1, this.doubleStack.getFirstStack().size());

    }

    /**
     * Tests that an element can be pushed onto the second stack of an
     * ArrayDoubleStack.
     * 
     * @throws StackOverflowException Thrown if pushing to the second stack fails.
     */
    @Test
    public void pushOneItemToSecondStack() throws StackOverflowException {
        // Try to push an element onto the stack
        this.doubleStack.getSecondStack().push(new Object());

        // Check the size of the stack has increased
        assertEquals(1, this.doubleStack.getSecondStack().size());

    }

    /**
     * Tests that the entire first stack of a ArrayDoubleStack can be filled when
     * the doubleStack maxSize was even.
     * 
     * @throws StackOverflowException Thrown if pushing to the first stack.
     */
    @Test
    public void fillFirstStackWithEvenDoubleStack() throws StackOverflowException {
        int evenStackSize = 10;

        // Calculate the maximum elements that can be pushed onto one stack
        int stackSize = evenStackSize / 2;

        // Make the doublestack with the even stack size
        this.doubleStack = getFactory().makeDoubleStack(evenStackSize);

        for (int i = 0; i < stackSize; i++) {
            this.doubleStack.getFirstStack().push(new Object());
        }

        assertEquals(stackSize, this.doubleStack.getFirstStack().size());
    }

    /**
     * Tests that the entire second stack of a ArrayDoubleStack can be filled when
     * the doubleStack maxSize was even. Even stack sizes should be able to hold the
     * exact max items given.
     * 
     * @throws StackOverflowException Thrown if pushing to the second stack fails.
     */
    @Test
    public void fillSecondStackWithEvenDoubleStackSize() throws StackOverflowException {
        int evenStackSize = 10;

        // Calculate the maximum elements that can be pushed onto one stack
        int stackSize = evenStackSize / 2;

        // Make the doublestack with the even stack size
        this.doubleStack = getFactory().makeDoubleStack(evenStackSize);

        for (int i = 0; i < stackSize; i++) {
            this.doubleStack.getSecondStack().push(new Object());
        }

        assertEquals(stackSize, this.doubleStack.getSecondStack().size());
    }

    /**
     * Tests that the entire first stack of a ArrayDoubleStack can be filled when
     * the doubleStack maxSize was odd. Odd numbers should be able to contain 1 less
     * elements as you can't share the middle index of an array
     * 
     * @throws StackOverflowException Thrown if pushing to the first stack fails.
     */
    @Test
    public void fillFirstStackWithOddDoubleStack() throws StackOverflowException {
        int oddMaxSize = 9;

        // Calculate the maximum elements that can be pushed onto one stack.
        int stackSize = Math.floorDiv(oddMaxSize, 2);

        // Make the doublestack with the odd stack size
        this.doubleStack = getFactory().makeDoubleStack(oddMaxSize);

        for (int i = 0; i < stackSize; i++) {
            this.doubleStack.getFirstStack().push(new Object());
        }

        assertEquals(stackSize, this.doubleStack.getFirstStack().size());
    }

    /**
     * Tests that the entire second stack of a ArrayDoubleStack can be filled when
     * the doubleStack maxSize was even.
     * 
     * @throws StackOverflowException Thrown if pushing to the second stack fails.
     */
    @Test
    public void fillSecondStackWithOddDoubleStackSize() throws StackOverflowException {
        int oddMaxSize = 9;

        // Calculate the maximum elements that can be pushed onto one stack.
        int stackSize = Math.floorDiv(oddMaxSize, 2);

        // Make the doublestack with the odd stack size
        this.doubleStack = getFactory().makeDoubleStack(oddMaxSize);

        for (int i = 0; i < stackSize; i++) {
            this.doubleStack.getSecondStack().push(new Object());
        }

        assertEquals(stackSize, this.doubleStack.getSecondStack().size());
    }

    /**
     * Tests that an exception is thrown when an element is pushed onto a full first
     * stack of a ArrayDoubleStack.
     * 
     * @throws StackOverflowException Thrown if the initial filling of the stack
     *                                doesn't work.
     */
    @Test
    public void pushThrowsExceptionOnFullFirstStack() throws StackOverflowException {
        // Calculate the maximum elements that can be pushed onto one stack
        int stackSize = DEFAULT_MAX_SIZE / 2;

        // Fill the first stack
        for (int i = 0; i < stackSize; i++) {
            this.doubleStack.getFirstStack().push(new Object());
        }

        // Try to add another element
        assertThrows(StackOverflowException.class, () -> this.doubleStack.getFirstStack().push(new Object()));
    }

    /**
     * Tests that an exception is thrown when an element is pushed onto a full
     * second stack of a ArrayDoubleStack.
     * 
     * @throws StackOverflowException Thrown if the initial filling of the stack
     *                                doesn't work.
     */
    @Test
    public void pushThrowsExceptionOnFullSecondStack() throws StackOverflowException {
        // Calculate the maximum elements that can be pushed onto one stack
        int stackSize = DEFAULT_MAX_SIZE / 2;

        // Fill the first stack
        for (int i = 0; i < stackSize; i++) {
            this.doubleStack.getSecondStack().push(new Object());
        }

        // Try to add another element
        assertThrows(StackOverflowException.class, () -> this.doubleStack.getSecondStack().push(new Object()));
    }

    /**
     * Tests that the top element can be retrieved without popping from the first
     * stack of an ArrayDoubleStack.
     * 
     * @throws StackOverflowException Thrown if the initial popping fails.
     * @throws StackEmptyException    Thrown if calling top fails.
     */
    @Test
    public void topReturnsObjectFirstStack() throws StackOverflowException, StackEmptyException {
        // Push an element to stack
        Object element = new Object();
        this.doubleStack.getFirstStack().push(element);

        // Check the top element is correct, and check it wasn't removed
        assertEquals(element, this.doubleStack.getFirstStack().top());
        assertEquals(1, this.doubleStack.getFirstStack().size());
    }

    /**
     * Tests that the top element can be retrieved without popping from the second
     * stack of an ArrayDoubleStack.
     * 
     * @throws StackOverflowException Thrown if the initial popping fails.
     * @throws StackEmptyException    Thrown if calling top fails.
     */
    @Test
    public void topReturnsObjectSecondStack() throws StackOverflowException, StackEmptyException {
        // Push an element to stack
        Object element = new Object();
        this.doubleStack.getSecondStack().push(element);

        // Check the top element is correct
        assertEquals(element, this.doubleStack.getSecondStack().top());

        // Make sure the stack wasn't affected by the call of top
        assertEquals(1, this.doubleStack.getSecondStack().size());
    }

    /**
     * Tests that the top function throws an exception when the first stack of an
     * ArrayDoubleStack is empty.
     */
    @Test
    public void topThrowsExceptionOnEmptyFirstStack() {
        assertThrows(StackEmptyException.class, () -> this.doubleStack.getFirstStack().top());
    }

    /**
     * Tests that the top function throws an exception when the second stack of an
     * ArrayDoubleStack is empty.
     */
    @Test
    public void topThrowsExceptionOnEmptySecondStack() {
        assertThrows(StackEmptyException.class, () -> this.doubleStack.getSecondStack().top());
    }

    /**
     * Tests that the correct element can be popped off the top of the first stack
     * of an ArrayDoubleStack.
     * 
     * @throws StackOverflowException Thrown if the initial pushing fails.
     * @throws StackEmptyException    Thrown if there is an issue with popping the
     *                                element.
     */
    @Test
    public void popCorrectElementFromFirstStack() throws StackOverflowException, StackEmptyException {
        Object element = new Object();

        // Push an element to the stack which increments the stackSize
        this.doubleStack.getFirstStack().push(element);
        assertEquals(1, this.doubleStack.getFirstStack().size());

        // Pop the element off the top and check it's the correct instance
        assertEquals(element, this.doubleStack.getFirstStack().pop());

        // Check the stackSize decrements when an element is popped off
        assertEquals(0, this.doubleStack.getFirstStack().size());

    }

    /**
     * Tests that the correct element can be popped off the top of the first stack
     * of an ArrayDoubleStack.
     * 
     * @throws StackOverflowException Thrown if the initial pushing fails.
     * @throws StackEmptyException    Thrown if there is an issue with popping the
     *                                element.
     */
    @Test
    public void popCorrectElementFromSecondStack() throws StackOverflowException, StackEmptyException {
        Object element = new Object();

        // Push an element to the stack which increments the stackSize
        this.doubleStack.getSecondStack().push(element);
        assertEquals(1, this.doubleStack.getSecondStack().size());

        // Pop the element off the top and check it's the correct instance
        assertEquals(element, this.doubleStack.getSecondStack().pop());

        // Check the stackSize decrements when an element is popped off
        assertEquals(0, this.doubleStack.getSecondStack().size());

    }

    /**
     * Tests that an excepion is thrown when attempting to pop an item from the
     * first stack of an ArrayDoubleStack when the stack is empty.
     */
    @Test
    public void popThrowsExceptionOnEmptyFirstStack() {
        // Make sure the first stack is empty
        assertEquals(0, this.doubleStack.getFirstStack().size());

        // Check that a StackEmptyException is thrown
        assertThrows(StackEmptyException.class, () -> this.doubleStack.getFirstStack().pop());

    }

    /**
     * Tests that an excepion is thrown when attempting to pop an item from the
     * second stack of an ArrayDoubleStack when the stack is empty.
     */
    @Test
    public void popThrowsExceptionOnEmptySecondStack() {
        // Make sure the first stack is empty
        assertEquals(0, this.doubleStack.getSecondStack().size());

        // Check that a StackEmptyException is thrown
        assertThrows(StackEmptyException.class, () -> this.doubleStack.getSecondStack().pop());

    }

    /**
     * Tests that the first stack of an ArrayDoubleStack can be filled using push,
     * and then emptied using pop to get the elements in the opposite order to which
     * they were added.
     * 
     * @throws StackOverflowException Thrown if the initial pushing fails.
     * @throws StackEmptyException    Thrown if the popping fails.
     */
    @Test
    public void fillAndEmptyFirstStackUsingPushAndPop() throws StackOverflowException, StackEmptyException {
        // Make a double stack of size 10 (2 stacks of size 5)
        this.doubleStack = new ArrayDoubleStack(10);

        // Fill the first stack

        Object element_1 = new Object();
        this.doubleStack.getFirstStack().push(element_1);

        Object element_2 = new Object();
        this.doubleStack.getFirstStack().push(element_2);

        Object element_3 = new Object();
        this.doubleStack.getFirstStack().push(element_3);

        Object element_4 = new Object();
        this.doubleStack.getFirstStack().push(element_4);

        Object element_5 = new Object();
        this.doubleStack.getFirstStack().push(element_5);

        assertEquals(5, this.doubleStack.getFirstStack().size());

        // Empty the stack and check the elements are popped in reverse order

        assertEquals(element_5, this.doubleStack.getFirstStack().pop());
        assertEquals(element_4, this.doubleStack.getFirstStack().pop());
        assertEquals(element_3, this.doubleStack.getFirstStack().pop());
        assertEquals(element_2, this.doubleStack.getFirstStack().pop());
        assertEquals(element_1, this.doubleStack.getFirstStack().pop());

        assertEquals(true, this.doubleStack.getFirstStack().isEmpty());

    }

    /**
     * Tests that the second stack of an ArrayDoubleStack can be filled using push,
     * and then emptied using pop to get the elements in the opposite order to which
     * they were added.
     * 
     * @throws StackOverflowException Thrown if the initial pushing fails.
     * @throws StackEmptyException    Thrown if the popping fails.
     */
    @Test
    public void fillAndEmptySecondStackUsingPushAndPop() throws StackOverflowException, StackEmptyException {
        // Make a double stack of size 10 (2 stacks of size 5)
        this.doubleStack = new ArrayDoubleStack(10);

        // Fill the second stack
        Object element_1 = new Object();
        this.doubleStack.getSecondStack().push(element_1);

        Object element_2 = new Object();
        this.doubleStack.getSecondStack().push(element_2);

        Object element_3 = new Object();
        this.doubleStack.getSecondStack().push(element_3);

        Object element_4 = new Object();
        this.doubleStack.getSecondStack().push(element_4);

        Object element_5 = new Object();
        this.doubleStack.getSecondStack().push(element_5);

        assertEquals(5, this.doubleStack.getSecondStack().size());

        // Empty the stack and check the elements are popped in reverse order
        assertEquals(element_5, this.doubleStack.getSecondStack().pop());
        assertEquals(element_4, this.doubleStack.getSecondStack().pop());
        assertEquals(element_3, this.doubleStack.getSecondStack().pop());
        assertEquals(element_2, this.doubleStack.getSecondStack().pop());
        assertEquals(element_1, this.doubleStack.getSecondStack().pop());

        assertEquals(true, this.doubleStack.getSecondStack().isEmpty());

    }

    /**
     * Tests filling and emptying both stacks using push and pop.
     * 
     * @throws StackOverflowException Thrown if the initial pushing fails for either
     *                                stack.
     * @throws StackEmptyException    Thrown if the popping fails for either stack.
     */
    @Test
    public void fillAndEmptyBothStacksUsingPushAndPop() throws StackOverflowException, StackEmptyException {
        // Make a double stack of size 10 (2 stacks of size 5)
        this.doubleStack = new ArrayDoubleStack(10);

        // Fill the first stack
        Object element_1 = new Object();
        this.doubleStack.getFirstStack().push(element_1);
        Object element_3 = new Object();
        this.doubleStack.getFirstStack().push(element_3);
        Object element_5 = new Object();
        this.doubleStack.getFirstStack().push(element_5);
        Object element_7 = new Object();
        this.doubleStack.getFirstStack().push(element_7);
        Object element_9 = new Object();
        this.doubleStack.getFirstStack().push(element_9);

        // Fill the second stack
        Object element_2 = new Object();
        this.doubleStack.getSecondStack().push(element_2);
        Object element_4 = new Object();
        this.doubleStack.getSecondStack().push(element_4);
        Object element_6 = new Object();
        this.doubleStack.getSecondStack().push(element_6);
        Object element_8 = new Object();
        this.doubleStack.getSecondStack().push(element_8);
        Object element_10 = new Object();
        this.doubleStack.getSecondStack().push(element_10);

        assertEquals(5, this.doubleStack.getFirstStack().size());
        assertEquals(5, this.doubleStack.getSecondStack().size());

        // Empty the stacks and check the elements are popped in reverse order
        assertEquals(element_10, this.doubleStack.getSecondStack().pop());
        assertEquals(element_9, this.doubleStack.getFirstStack().pop());
        assertEquals(element_8, this.doubleStack.getSecondStack().pop());
        assertEquals(element_7, this.doubleStack.getFirstStack().pop());
        assertEquals(element_6, this.doubleStack.getSecondStack().pop());
        assertEquals(element_5, this.doubleStack.getFirstStack().pop());
        assertEquals(element_4, this.doubleStack.getSecondStack().pop());
        assertEquals(element_3, this.doubleStack.getFirstStack().pop());
        assertEquals(element_2, this.doubleStack.getSecondStack().pop());
        assertEquals(element_1, this.doubleStack.getFirstStack().pop());

        assertEquals(true, this.doubleStack.getFirstStack().isEmpty());
        assertEquals(true, this.doubleStack.getSecondStack().isEmpty());

    }

    /**
     * Tests that clearing the first stack of a ArrayDoubleStack does nothing when
     * empty.
     */
    @Test
    public void clearEmptyFirstStack() {
        assertEquals(true, this.doubleStack.getFirstStack().isEmpty());

        this.doubleStack.getFirstStack().clear();

        assertEquals(true, this.doubleStack.getFirstStack().isEmpty());

    }

    /**
     * Tests that clearing the first stack of a ArrayDoubleStack does nothing when
     * empty.
     */
    @Test
    public void clearEmptySecondStack() {
        assertEquals(true, this.doubleStack.getSecondStack().isEmpty());

        this.doubleStack.getSecondStack().clear();

        assertEquals(true, this.doubleStack.getSecondStack().isEmpty());
    }

    /**
     * Tests clearing the first stack of a ArrayDoubleStack works when elements are
     * added.
     * 
     * @throws StackOverflowException Thrown if the initial pushing fails.
     */
    @Test
    public void clearNotEmptyFirstStack() throws StackOverflowException {
        this.doubleStack.getFirstStack().push(new Object());
        this.doubleStack.getFirstStack().push(new Object());
        this.doubleStack.getFirstStack().push(new Object());

        assertEquals(false, this.doubleStack.getFirstStack().isEmpty());

        this.doubleStack.getFirstStack().clear();

        assertEquals(true, this.doubleStack.getFirstStack().isEmpty());

    }

    /**
     * Tests clearing the second stack of a ArrayDoubleStack works when elements are
     * added.
     * 
     * @throws StackOverflowException Thrown if the initial pushing fails.
     */
    @Test
    public void clearNotEmptySecondStack() throws StackOverflowException {
        this.doubleStack.getSecondStack().push(new Object());
        this.doubleStack.getSecondStack().push(new Object());
        this.doubleStack.getSecondStack().push(new Object());

        assertEquals(false, this.doubleStack.getSecondStack().isEmpty());

        this.doubleStack.getSecondStack().clear();

        assertEquals(true, this.doubleStack.getSecondStack().isEmpty());

    }

    /**
     * Tests initialising a zero-sized double stack.
     * 
     */
    @Test
    public void zeroSizedDoubleStack() {
        this.doubleStack = getFactory().makeDoubleStack(0);

        // Check both stacks are empty
        assertTrue(doubleStack.getFirstStack().isEmpty());
        assertTrue(doubleStack.getSecondStack().isEmpty());

        // Check both stacks have size 0
        assertEquals(0, doubleStack.getFirstStack().size());
        assertEquals(0, doubleStack.getSecondStack().size());

    }

    /**
     * Tests that zero-sized double stacks cannot be pushed to.
     * 
     */
    @Test
    public void pushingToZeroSizedDoubleStacksThrowsException() {
        this.doubleStack = getFactory().makeDoubleStack(0);

        // Check both stacks throw an exception on pushing
        assertThrows(StackOverflowException.class, () -> this.doubleStack.getFirstStack().push(new Object()));
        assertThrows(StackOverflowException.class, () -> this.doubleStack.getSecondStack().push(new Object()));

    }

    /**
     * Tests that zero-sized double stacks cannot be pushed to.
     * 
     */
    @Test
    public void poppingFromZeroSizedDoubleStacksThrowsException() {
        this.doubleStack = getFactory().makeDoubleStack(0);

        // Check both stacks throw an exception on pushing
        assertThrows(StackEmptyException.class, () -> this.doubleStack.getFirstStack().pop());
        assertThrows(StackEmptyException.class, () -> this.doubleStack.getSecondStack().pop());

    }
}

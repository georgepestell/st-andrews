package test;

import common.OwnerAlreadyRegisteredException;
import common.OwnerNotRegisteredException;
import common.InsufficientPointsException;

import java.security.InvalidParameterException;

import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;


import common.AbstractFactoryClient;
import interfaces.ILoyaltyCardOperator;
import interfaces.ILoyaltyCardOwner;

public class LoyaltyCardOperatorTests extends AbstractFactoryClient {

    /**
     * The fresh loyalty card each test will use
     */
    private ILoyaltyCardOperator loyaltyCardOperator;
    private ILoyaltyCardOwner loyaltyCardOwner;

    /**
     * This makes sure there is a fresh loyaltyCardOperator setup before each test
     * is ran
     */
    @Before
    public void setupOperator() {
        loyaltyCardOperator = getFactory().makeLoyaltyCardOperator();
        // Owner factory creation is tested in LoyaltyCardOwnerTests
        loyaltyCardOwner = getFactory().makeLoyaltyCardOwner("john@example.com", "John");
    }

    // -- Intial Value Tests

    /** This checks that when first initialized, there number of customers is 0 */
    @Test
    public void checkInitializedOperatorHasZeroUsers() {
        assertEquals(loyaltyCardOperator.getNumberOfCustomers(), 0);
    }

    /** This checks that when first initialized, the total number of points is 0 */
    @Test
    public void checkInitializedOperatorHasZeroTotalPoints() {
        assertEquals(loyaltyCardOperator.getTotalNumberOfPoints(), 0);
    }

    /**
     * Checks that an exception is thrown when getMostUsed is called on a new loyaltyCardOperator
     * 
     * @throws OwnerAlreadyRegisteredException
     */
    @Test
    public void checkMostUsedOnInitialisation() throws OwnerAlreadyRegisteredException, OwnerNotRegisteredException {
        assertThrows(OwnerNotRegisteredException.class, () -> loyaltyCardOperator.getMostUsed());
    }


    // -- Operator Owners Tests

    /**
     * This checks that an error is thrown when a null object attempts to register
     * as a customer
     */
    @Test
    public void registerNullOwner() {
        assertThrows(InvalidParameterException.class, () -> loyaltyCardOperator.registerOwner(null));
    }

    /**
     * This makes sure that an owner can be registered with an operator
     * 
     * @throws OwnerAlreadyRegisteredException
     * @throws InvalidParameterException
     */
    @Test
    public void registerOneOwner() throws OwnerAlreadyRegisteredException, InvalidParameterException {

        // Try adding the owner

        loyaltyCardOperator.registerOwner(loyaltyCardOwner);
        assertEquals(loyaltyCardOperator.getNumberOfCustomers(), 1);

    }

    /**
     * This checks adding two different owners to an operator
     * 
     * @throws OwnerAlreadyRegisteredException
     * @throws InvalidParameterException
     */
    @Test
    public void registerTwoDifferentOwners() throws OwnerAlreadyRegisteredException, InvalidParameterException {

        // Owner factory creation is tested in LoyaltyCardOwnerTests
        ILoyaltyCardOwner owner_sam = getFactory().makeLoyaltyCardOwner("sam@example.com", "Sam");

        // Try adding the owner

        loyaltyCardOperator.registerOwner(loyaltyCardOwner);
        loyaltyCardOperator.registerOwner(owner_sam);
        assertEquals(loyaltyCardOperator.getNumberOfCustomers(), 2);

    }

    /**
     * This checks adding the same owner instance twice to an operator
     * 
     * @throws OwnerAlreadyRegisteredException
     * @throws InvalidParameterException
     */
    @Test
    public void registerOwnerTwice() throws OwnerAlreadyRegisteredException, InvalidParameterException {
        // This should work without
        loyaltyCardOperator.registerOwner(loyaltyCardOwner);

        // Adding the same owner should throw an exception
        assertThrows(OwnerAlreadyRegisteredException.class,
                () -> loyaltyCardOperator.registerOwner(loyaltyCardOwner));

    }

    /**
     * This checks that two different owners with the same email cannot be
     * registered
     * 
     * @throws OwnerAlreadyRegisteredException
     * @throws InvalidParameterException
     */
    @Test
    public void registerOwnerWithSameEmail() throws OwnerAlreadyRegisteredException, InvalidParameterException {

        // Create two different owners with the same email but different names
        ILoyaltyCardOwner owner_johnson = getFactory().makeLoyaltyCardOwner("john@example.com", "Johnson");

        // Register the first owner
        loyaltyCardOperator.registerOwner(loyaltyCardOwner);

        // Attempt to register the second with the same email
        assertThrows(OwnerAlreadyRegisteredException.class,
                () -> loyaltyCardOperator.registerOwner(owner_johnson));

    }

    /**
     * This checks that two different owners with different emails but the same name
     * can register without errror
     * 
     * @throws OwnerAlreadyRegisteredException
     * @throws InvalidParameterException
     */
    @Test
    public void registerOwnerWithSameName() throws OwnerAlreadyRegisteredException, InvalidParameterException {

        // Owner factory creation is tested in LoyaltyCardOwnerTests
        ILoyaltyCardOwner owner_johnson = getFactory().makeLoyaltyCardOwner("johnson@example.com", "John");

        // Register the owners with the operator
        loyaltyCardOperator.registerOwner(loyaltyCardOwner);
        loyaltyCardOperator.registerOwner(owner_johnson);

        assertEquals(2, loyaltyCardOperator.getNumberOfCustomers());

    }

    /**
     * Check's that a registered Owner can be unregistered.
     */
    @Test
    public void unregisterRegisteredOwner()
            throws OwnerAlreadyRegisteredException, InvalidParameterException, OwnerNotRegisteredException {
        // Register the owner
        loyaltyCardOperator.registerOwner(loyaltyCardOwner);

        // Unregister the owner
        loyaltyCardOperator.unregisterOwner(loyaltyCardOwner);

        assertEquals(0, loyaltyCardOperator.getNumberOfCustomers());

    }

    /**
     * Checks that the OwnerNotRegisteredException is thrown when a owner not
     * registered tries to unregister
     */
    @Test
    public void unregisterNotRegisteredOwner() throws OwnerAlreadyRegisteredException, InvalidParameterException {
        // Owner factory creation is tested in LoyaltyCardOwnerTests
        ILoyaltyCardOwner owner = getFactory().makeLoyaltyCardOwner("john@example.com", "John");

        // Try to unregister the owner
        assertThrows(OwnerNotRegisteredException.class, () -> loyaltyCardOperator.unregisterOwner(owner));

    }

    /**
     * Checks that OwnerNotRegisteredException is thrown if a registered owner tries
     * to unregister twice
     */
    @Test
    public void registerRegisteredOwnerTwice()
            throws OwnerAlreadyRegisteredException, InvalidParameterException, OwnerNotRegisteredException {
        // Register the owner
        loyaltyCardOperator.registerOwner(loyaltyCardOwner);

        // Unregister the owner
        loyaltyCardOperator.unregisterOwner(loyaltyCardOwner);

        // Try to unregister the owner again
        assertThrows(OwnerNotRegisteredException.class,
                () -> loyaltyCardOperator.unregisterOwner(loyaltyCardOwner));

    }

    // --- Loyalty Card Tests

    /**
     * Processes a non-zero value money payment which would round up, but should not
     */
    @Test
    public void processValidRoundUpMoneyPayment()
            throws OwnerAlreadyRegisteredException, InvalidParameterException, OwnerNotRegisteredException {
        loyaltyCardOperator.registerOwner(loyaltyCardOwner);

        loyaltyCardOperator.processMoneyPurchase(loyaltyCardOwner.getEmail(), 372);

        assertEquals(3, loyaltyCardOperator.getNumberOfPoints(loyaltyCardOwner.getEmail()));
    }

    /**
     * Processes a non-zero value money payment which would round down to a valid
     * owner's card
     */
    @Test
    public void processValidRoundDownPayment()
            throws OwnerAlreadyRegisteredException, InvalidParameterException, OwnerNotRegisteredException {
        loyaltyCardOperator.registerOwner(loyaltyCardOwner);

        loyaltyCardOperator.processMoneyPurchase(loyaltyCardOwner.getEmail(), 222);

        assertEquals(2, loyaltyCardOperator.getNumberOfPoints(loyaltyCardOwner.getEmail()));
    }

    /**
     * Processes a zero value money payment above 100pence to a valid owner's card
     */
    @Test
    public void processZeroValueMoneyPayment()
            throws OwnerAlreadyRegisteredException, InvalidParameterException, OwnerNotRegisteredException {
        loyaltyCardOperator.registerOwner(loyaltyCardOwner);

        loyaltyCardOperator.processMoneyPurchase(loyaltyCardOwner.getEmail(), 0);
        assertEquals(0, loyaltyCardOperator.getNumberOfPoints(loyaltyCardOwner.getEmail()));
    }

    /**
     * Processes a zero value money payment below 100pence to a valid owner's card
     */
    @Test
    public void processLowValueMoneyPayment()
            throws OwnerAlreadyRegisteredException, InvalidParameterException, OwnerNotRegisteredException {
        loyaltyCardOperator.registerOwner(loyaltyCardOwner);

        loyaltyCardOperator.processMoneyPurchase(loyaltyCardOwner.getEmail(), 40);

        assertEquals(0, loyaltyCardOperator.getNumberOfPoints(loyaltyCardOwner.getEmail()));
    }

    /** Processes a valid money payment to an unregistered owner */
    @Test
    public void processMoneyPaymentToNotRegisteredOwner()
            throws OwnerAlreadyRegisteredException, InvalidParameterException {

        assertThrows(OwnerNotRegisteredException.class,
                () -> loyaltyCardOperator.processMoneyPurchase(loyaltyCardOwner.getEmail(), 40));

    }

    /** Process a valid money payment to an owner who has unregistered */
    @Test
    public void processMoneyPaymentToUnregisteredOwner()
            throws OwnerAlreadyRegisteredException, InvalidParameterException, OwnerNotRegisteredException {
        // Register the owner
        loyaltyCardOperator.registerOwner(loyaltyCardOwner);

        // Unregister the owner
        loyaltyCardOperator.unregisterOwner(loyaltyCardOwner);

        assertThrows(OwnerNotRegisteredException.class,
                () -> loyaltyCardOperator.processMoneyPurchase(loyaltyCardOwner.getEmail(), 40));
    }

    /** Process a valid points payment */
    @Test
    public void processValidPointsPayment() throws OwnerAlreadyRegisteredException, InvalidParameterException,
            OwnerNotRegisteredException, InsufficientPointsException {
        loyaltyCardOperator.registerOwner(loyaltyCardOwner);

        loyaltyCardOperator.processMoneyPurchase(loyaltyCardOwner.getEmail(), 10000);
        loyaltyCardOperator.processPointsPurchase(loyaltyCardOwner.getEmail(), 75);

        // Check that the remaining points are 25 (10000/100 = 100 points, 100 - 75 =
        // 25)
        assertEquals(25, loyaltyCardOperator.getNumberOfPoints(loyaltyCardOwner.getEmail()));
    }

    /** Process a valid points payment which will use all a card's points */
    @Test
    public void processTotalPointsPayment() throws OwnerAlreadyRegisteredException, InvalidParameterException,
            OwnerNotRegisteredException, InsufficientPointsException {
        loyaltyCardOperator.registerOwner(loyaltyCardOwner);

        loyaltyCardOperator.processMoneyPurchase(loyaltyCardOwner.getEmail(), 1000);
        loyaltyCardOperator.processPointsPurchase(loyaltyCardOwner.getEmail(), 10);

        // Check that the remaining points are 0
        assertEquals(0, loyaltyCardOperator.getNumberOfPoints(loyaltyCardOwner.getEmail()));
    }

    /** Attempt to make a purchase with insufficient points on the card */
    @Test
    public void processInsufficientPointsPurchase()
            throws OwnerAlreadyRegisteredException, OwnerNotRegisteredException {
        loyaltyCardOperator.registerOwner(loyaltyCardOwner);

        // Add some points to the card
        loyaltyCardOperator.processMoneyPurchase(loyaltyCardOwner.getEmail(), 1000);

        assertThrows(InsufficientPointsException.class,
                () -> loyaltyCardOperator.processPointsPurchase(loyaltyCardOwner.getEmail(), 11));
    }

    /** Processes a valid money payment to an unregistered owner */
    @Test
    public void processPointsPaymentToNotRegisteredOwner()
            throws OwnerAlreadyRegisteredException, InvalidParameterException {

        assertThrows(OwnerNotRegisteredException.class,
                () -> loyaltyCardOperator.processPointsPurchase(loyaltyCardOwner.getEmail(), 0));

    }

    /** Process a valid money payment to an owner who has unregistered */
    @Test
    public void processPointsPaymentToUnregisteredOwner()
            throws OwnerAlreadyRegisteredException, InvalidParameterException, OwnerNotRegisteredException {
        // Register the owner
        loyaltyCardOperator.registerOwner(loyaltyCardOwner);

        // Add enough points to the card
        loyaltyCardOperator.processMoneyPurchase(loyaltyCardOwner.getEmail(), 1000);

        // Unregister the owner
        loyaltyCardOperator.unregisterOwner(loyaltyCardOwner);

        assertThrows(OwnerNotRegisteredException.class,
                () -> loyaltyCardOperator.processPointsPurchase(loyaltyCardOwner.getEmail(), 1));
    }

    /** Checks that a non-zero number of uses for a registered owner can be retrieved 
     * @throws OwnerAlreadyRegisteredException
     * @throws InsufficientPointsException
     * */
    @Test
    public void getNumberOfUsesOnRegisteredOwner() throws OwnerNotRegisteredException, OwnerAlreadyRegisteredException, InsufficientPointsException {
        loyaltyCardOperator.registerOwner(loyaltyCardOwner);

        // Add and Use Points
        loyaltyCardOperator.processMoneyPurchase(loyaltyCardOwner.getEmail(), 10000);
        loyaltyCardOperator.processPointsPurchase(loyaltyCardOwner.getEmail(), 1);
        loyaltyCardOperator.processPointsPurchase(loyaltyCardOwner.getEmail(), 1);
        loyaltyCardOperator.processPointsPurchase(loyaltyCardOwner.getEmail(), 1);

        // Check the usage count
        assertEquals(3, loyaltyCardOperator.getNumberOfUses(loyaltyCardOwner.getEmail()));

    }

    /** Checks that an exception is thrown when getNumberOfUses is called on a non-registered email
     * @throws OwnerAlreadyRegisteredException
     * @throws InsufficientPointsException
     * */
    @Test
    public void getNumberOfUsesOnNonRegisteredOwner() throws OwnerNotRegisteredException, OwnerAlreadyRegisteredException, InsufficientPointsException {
        assertThrows(OwnerNotRegisteredException.class, () -> loyaltyCardOperator.getNumberOfUses("john@example.com"));
    }

    /** Checks that an exception is thrown when getNumberOfUses is called on a email which has unregistered
     * @throws OwnerAlreadyRegisteredException
     * @throws InsufficientPointsException
     * */
    @Test
    public void getNumberOfUsesOnUnregisteredOwner() throws OwnerNotRegisteredException, OwnerAlreadyRegisteredException, InsufficientPointsException {
        
        loyaltyCardOperator.registerOwner(loyaltyCardOwner);
        
        loyaltyCardOperator.unregisterOwner(loyaltyCardOwner);

        assertThrows(OwnerNotRegisteredException.class, () -> loyaltyCardOperator.getNumberOfUses("john@example.com"));
    }

    /**
     * Checks that when obvious, the owner of the most used card
     *  
     * @throws OwnerAlreadyRegisteredException
     * @throws InsufficientPointsException
     */
    @Test
    public void getMostUsedWithClearHighest() throws OwnerAlreadyRegisteredException, OwnerNotRegisteredException, InsufficientPointsException {
        ILoyaltyCardOwner owner_2 = getFactory().makeLoyaltyCardOwner("owner2@example.com", "Owner 2");
        ILoyaltyCardOwner owner_3 = getFactory().makeLoyaltyCardOwner("owner3@example.com", "Owner 3");

        // Register owners
        loyaltyCardOperator.registerOwner(loyaltyCardOwner);
        loyaltyCardOperator.registerOwner(owner_2);
        loyaltyCardOperator.registerOwner(owner_3);

        // Add enough points to be used
        loyaltyCardOperator.processMoneyPurchase(owner_2.getEmail(), 1000000000);
        loyaltyCardOperator.processMoneyPurchase(owner_3.getEmail(), 1000000000);
        
        // Use points mostly by owner 3
        loyaltyCardOperator.processPointsPurchase(owner_2.getEmail(), 1);

        loyaltyCardOperator.processPointsPurchase(owner_3.getEmail(), 1);
        loyaltyCardOperator.processPointsPurchase(owner_3.getEmail(), 1);
        loyaltyCardOperator.processPointsPurchase(owner_3.getEmail(), 1);

        assertEquals(owner_3, loyaltyCardOperator.getMostUsed());
    }

    /**
     * Checks that when two cards have been used the same, a "random" one of the most used will be returned
     *  
     * @throws OwnerAlreadyRegisteredException
     * @throws InsufficientPointsException
     */
    @Test
    public void getMostUsedWithJoinedHighest() throws OwnerAlreadyRegisteredException, OwnerNotRegisteredException, InsufficientPointsException {
        ILoyaltyCardOwner owner_2 = getFactory().makeLoyaltyCardOwner("owner2@example.com", "Owner 2");
        ILoyaltyCardOwner owner_3 = getFactory().makeLoyaltyCardOwner("owner3@example.com", "Owner 3");

        // Register owners
        loyaltyCardOperator.registerOwner(loyaltyCardOwner);
        loyaltyCardOperator.registerOwner(owner_2);
        loyaltyCardOperator.registerOwner(owner_3);

        // Add enough points to be used by the owners
        loyaltyCardOperator.processMoneyPurchase(owner_2.getEmail(), 1000000000);
        loyaltyCardOperator.processMoneyPurchase(owner_3.getEmail(), 1000000000);
        
        // Use points evenly between owners 2 and 3
        loyaltyCardOperator.processPointsPurchase(owner_2.getEmail(), 1);
        loyaltyCardOperator.processPointsPurchase(owner_2.getEmail(), 1);

        loyaltyCardOperator.processPointsPurchase(owner_3.getEmail(), 1);
        loyaltyCardOperator.processPointsPurchase(owner_3.getEmail(), 1);

        assertNotEquals(loyaltyCardOwner, loyaltyCardOperator.getMostUsed());
    }

    /** This checks that the number of customers icrements when an owner is registered 
     * @throws OwnerAlreadyRegisteredException
     */
    @Test
    public void incrementCustomersOnRegistration() throws OwnerAlreadyRegisteredException {
        assertEquals(0, loyaltyCardOperator.getNumberOfCustomers());
        loyaltyCardOperator.registerOwner(loyaltyCardOwner);
        assertEquals(1, loyaltyCardOperator.getNumberOfCustomers());

    }
    /** This checks that the number of customers deincrements when an owner is unregistered */
    /** This checks that the number of customers icrements when an owner is registered 
     * @throws OwnerAlreadyRegisteredException
     * @throws OwnerNotRegisteredException
     */
    @Test
    public void deincrementCustomersOnUnregistration() throws OwnerAlreadyRegisteredException, OwnerNotRegisteredException {
        loyaltyCardOperator.registerOwner(loyaltyCardOwner);
        assertEquals(1, loyaltyCardOperator.getNumberOfCustomers());
        loyaltyCardOperator.unregisterOwner(loyaltyCardOwner);
        assertEquals(0, loyaltyCardOperator.getNumberOfCustomers());

    }
}

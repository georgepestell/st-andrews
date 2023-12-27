package test;

import common.AbstractFactoryClient;
import common.InsufficientPointsException;

import interfaces.ILoyaltyCardOwner;
import interfaces.ILoyaltyCard;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.Before;
import org.junit.Test;

public class LoyaltyCardTests extends AbstractFactoryClient {

    /**
     * This allows the setup function to create a fresh LoyaltyCard before each test
     */
    private ILoyaltyCard loyaltyCard;
    private ILoyaltyCardOwner loyaltyCardOwner;

    /**
     * Setup function which runs before every test to ensure a fresh LoyaltyCard is
     * avaliable
     */
    @Before
    public void setupLoyaltyCard() {
        loyaltyCardOwner = getFactory().makeLoyaltyCardOwner("john@example.com", "John");
        loyaltyCard = getFactory().makeLoyaltyCard(loyaltyCardOwner);
    }

    /** This checks to see that the owner of the LoyaltyCard can be retrieved */
    @Test
    public void retrieveLoyaltyCardOwner() {
        assertEquals(loyaltyCardOwner, loyaltyCard.getOwner());
    }

    /** This checks that the number of uses can be retrieved and is initially 0 */
    @Test
    public void retrieveLoyaltyCardInitialUses() {
        assertEquals(0, loyaltyCard.getNumberOfUses());
    }

    /** This checks that the number of points can be retrieved and is initially 0 */
    @Test
    public void retrieveLoyaltyCardInitialPoints() {
        assertEquals(0, loyaltyCard.getNumberOfPoints());
    }

    /** This checks that positive-integer numbers of points can be added */
    @Test
    public void addValidPointsToPoints() {
        loyaltyCard.addPoints(50);
        assertEquals(50, loyaltyCard.getNumberOfPoints());
    }

    /** This checks that adding zero points will not change anything */
    @Test
    public void addZeroPoints() {
        loyaltyCard.addPoints(0);
        assertEquals(0, loyaltyCard.getNumberOfPoints());
    }

    /** This checks that points added can be used when there's enough points */
    @Test
    public void useValidPoints() throws InsufficientPointsException {
        // Add points
        loyaltyCard.addPoints(32);
        // Use some (but not all) of the points added
        loyaltyCard.usePoints(30);

        // Check there's the right number of points remaining
        assertEquals(2, loyaltyCard.getNumberOfPoints());
    }

    /**
     * This checks that adding negative-value points will not remove points
     * 
     * @throws InsufficientPointsException
     */
    @Test
    public void useAllPoints() throws InsufficientPointsException {
        loyaltyCard.addPoints(32);
        loyaltyCard.usePoints(32);
        assertEquals(0, loyaltyCard.getNumberOfPoints());
    }

    /**
     * Checks that the number of usages increments with each non-zero use
     * 
     * @throws InsufficientPointsException
     */
    @Test
    public void usePointsTwiceCheckUsageCount() throws InsufficientPointsException {
        loyaltyCard.addPoints(100);
        loyaltyCard.usePoints(5);
        assertEquals(1, loyaltyCard.getNumberOfUses());
    }

    /**
     * This checks that the usage count doesn't increment when zero points are used
     * 
     * @throws InsufficientPointsException
     */
    @Test
    public void usePointsOnZeroAmountCheckUsage() throws InsufficientPointsException {
        loyaltyCard.usePoints(0);
        assertEquals(0, loyaltyCard.getNumberOfUses());
    }

}

package test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.Before;
import org.junit.Test;

import common.AbstractFactoryClient;
import interfaces.ILoyaltyCardOwner;

/**
 * This is a JUnit test class for the loyalty card ADT.
 *
 */
public class LoyaltyCardOwnerTests extends AbstractFactoryClient {

    private ILoyaltyCardOwner loyaltyCardOwner;

    @Before
    public void setupLoyaltyCardOwner() {
        loyaltyCardOwner = getFactory().makeLoyaltyCardOwner("john@example.com", "John");
    }

    /**
     * This checks that the loyalty card owner's email can be
     * retrieved
     */
    @Test
    public void loyaltyCardOwnerGetEmail() {
        assertEquals("john@example.com", loyaltyCardOwner.getEmail());
    }

    /**
     * This checks that the loyalty card owner's name can be
     * retrieved
     */
    @Test
    public void loyaltyCardOwnerGetName() {
        assertEquals("John", loyaltyCardOwner.getName());
    }

}

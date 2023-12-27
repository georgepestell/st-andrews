package test;

import java.security.InvalidParameterException;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.Test;

import common.AbstractFactoryClient;
import interfaces.ILoyaltyCard;
import interfaces.ILoyaltyCardOperator;
import interfaces.ILoyaltyCardOwner;

public class FactoryTests extends AbstractFactoryClient {

    // --- OPERATOR TESTS ---

    /**
     * This checks that the factory returns a valid operator when a suitable
     * constructor is called
     */
    @Test
    public void factoryReturnsNotNullOperator() {
        ILoyaltyCardOperator loyaltyCardOperator = getFactory().makeLoyaltyCardOperator();
        assertNotNull(loyaltyCardOperator);
    }

    // --- OWNER TESTS ---

    /**
     * This checks that the factory was able to call a sensible constructor to get a
     * non-null instance of ILoyaltyCardOwner.
     */
    @Test
    public void factoryReturnsNotNullOwner() {
        ILoyaltyCardOwner loyaltyCardOwner = getFactory().makeLoyaltyCardOwner("john@example.com", "John");
        assertNotNull(loyaltyCardOwner);
    }

    /**
     * This checks that the factory throws an error if an owner with a valid name
     * but null email is created
     */
    @Test
    public void factoryThrowsErrorOnOwnerWithNullEmail() {
        assertThrows(InvalidParameterException.class, () -> getFactory().makeLoyaltyCardOwner(null, "John"));
    }

    /**
     * This checks that the factory throws an error if an owner with a null name but
     * valid email is created
     */
    @Test
    public void factoryThrowsErrorOnOwnerWithNullName() {
        assertThrows(InvalidParameterException.class,
                () -> getFactory().makeLoyaltyCardOwner("john@example.com", null));
    }

    /**
     * This checks that the factory throws an error if an owner with a valid name
     * but invaid (but not null) email is created
     */
    @Test
    public void factoryThrowsErrorOnOwnerWithInvalidEmail() {
        assertThrows(InvalidParameterException.class,
                () -> getFactory().makeLoyaltyCardOwner("john.example.com", "John"));
    }

    /**
     * This checks that the factory throws an error if an owner with a null name and
     * email is created
     */
    @Test
    public void factoryThrowsErrorOnOwnerWithNullEmailAndName() {
        assertThrows(InvalidParameterException.class, () -> getFactory().makeLoyaltyCardOwner(null, null));
    }

    // --- CARD TESTS ---

    /**
     * This checks that the factory can call a suitable constructor which returns a
     * not-null LoyaltyCard instance
     */
    @Test
    public void factoryReturnsNotNullCard() {
        ILoyaltyCardOwner loyaltyCardOwner = getFactory().makeLoyaltyCardOwner("john@example.com", "John");
        ILoyaltyCard loyaltyCard = getFactory().makeLoyaltyCard(loyaltyCardOwner);
        assertNotNull(loyaltyCard);
    }

    /**
     * This checks that the factory throws an error if a LoyaltyCard is initialised
     * with a null owner oject
     */
    @Test
    public void factoryThrowsErrorOnCardWithNullOwner() {
        assertThrows(InvalidParameterException.class, () -> getFactory().makeLoyaltyCard(null));
    }
}

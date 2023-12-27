package impl;

import java.security.InvalidParameterException;
import java.util.HashMap;

import common.AbstractFactoryClient;
import common.InsufficientPointsException;
import common.OwnerAlreadyRegisteredException;
import common.OwnerNotRegisteredException;
import interfaces.ILoyaltyCard;
import interfaces.ILoyaltyCardOperator;
import interfaces.ILoyaltyCardOwner;

/**
 * This class represents a simple loyalty card operator.
 *
 */
public class LoyaltyCardOperator extends AbstractFactoryClient implements ILoyaltyCardOperator {

    /**
     * This stores the set of registered owners. As an email cannot be registered
     * twice, a HashMap is used.
     */
    private HashMap<String, ILoyaltyCard> registered_owners;

    public LoyaltyCardOperator() {
        this.registered_owners = new HashMap<String, ILoyaltyCard>();
    }

    @Override
    public void registerOwner(ILoyaltyCardOwner loyaltyCardOwner)
            throws OwnerAlreadyRegisteredException, InvalidParameterException {
        // Check if owner is null
        if (loyaltyCardOwner == null)
            throw new InvalidParameterException("loyaltyCardOwner cannot be null");

        // Attemps to add the owner to the HashMap or throws an error if already
        // registered
        if (registered_owners.putIfAbsent(loyaltyCardOwner.getEmail(),
                getFactory().makeLoyaltyCard(loyaltyCardOwner)) != null)
            throw new OwnerAlreadyRegisteredException();

    }

    @Override
    public void unregisterOwner(ILoyaltyCardOwner loyaltyCardOwner) throws OwnerNotRegisteredException {
        // Make sure the loyaltyCardOwner parameter is not null
        if (loyaltyCardOwner == null)
            throw new OwnerNotRegisteredException();

        // Try to remove the loyaltyCardOwner and throw an exception if nothing was
        // removed
        if (registered_owners.remove(loyaltyCardOwner.getEmail()) == null)
            throw new OwnerNotRegisteredException();

    }

    @Override
    public void processMoneyPurchase(String ownerEmail, int pence) throws OwnerNotRegisteredException {
        // Try to get the LoyaltyCard, or throw an exception if not registered
        ILoyaltyCard loyaltyCard;
        if ((loyaltyCard = registered_owners.get(ownerEmail)) == null)
            throw new OwnerNotRegisteredException();

        // Add the points (integer division truncates to integer)
        loyaltyCard.addPoints(pence / 100);
    }

    @Override
    public void processPointsPurchase(String ownerEmail, int pence)
            throws InsufficientPointsException, OwnerNotRegisteredException {

        // Try to get the LoyaltyCard, or throw an exception if not registered
        ILoyaltyCard loyaltyCard;
        if ((loyaltyCard = registered_owners.get(ownerEmail)) == null)
            throw new OwnerNotRegisteredException();

        // Check the number of points on the card are enough
        if (loyaltyCard.getNumberOfPoints() < pence)
            throw new InsufficientPointsException();

        // Try to use the points
        loyaltyCard.usePoints(pence);

    }

    @Override
    public int getNumberOfCustomers() {
        return registered_owners.size();
    }

    @Override
    public int getTotalNumberOfPoints() {
        // Loop over all cards and tally the points
        int totalPoints = 0;
        for (ILoyaltyCard c : registered_owners.values()) {
            totalPoints += c.getNumberOfPoints();
        }

        // Return the owner of the most used card
        return totalPoints;
    }

    @Override
    public int getNumberOfPoints(String ownerEmail) throws OwnerNotRegisteredException {
        // Try to get the LoyaltyCard, or throw an exception if not registered
        ILoyaltyCard loyaltyCard;
        if ((loyaltyCard = registered_owners.get(ownerEmail)) == null)
            throw new OwnerNotRegisteredException();

        return loyaltyCard.getNumberOfPoints();
    }

    @Override
    public int getNumberOfUses(String ownerEmail) throws OwnerNotRegisteredException {
        // Try to get the LoyaltyCard, or throw an exception if not registered
        ILoyaltyCard loyaltyCard;
        if ((loyaltyCard = registered_owners.get(ownerEmail)) == null)
            throw new OwnerNotRegisteredException();

        // Return the number of uses
        return loyaltyCard.getNumberOfUses();
    }

    @Override
    public ILoyaltyCardOwner getMostUsed() throws OwnerNotRegisteredException {

        // Loop over all cards and store the most used one
        ILoyaltyCard mostUsed = null;
        for (ILoyaltyCard c : registered_owners.values()) {
            if (mostUsed == null)
                mostUsed = c;
            if (c.getNumberOfUses() > mostUsed.getNumberOfUses())
                mostUsed = c;
        }

        // If registered_users is empty, this throws an exception as mostUsed will be
        // null
        if (mostUsed == null)
            throw new OwnerNotRegisteredException();

        // Return the owner of the most used card
        return mostUsed.getOwner();
    }

}

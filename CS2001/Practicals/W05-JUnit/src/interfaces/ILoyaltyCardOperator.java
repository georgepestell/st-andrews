package interfaces;

import common.InsufficientPointsException;
import common.OwnerAlreadyRegisteredException;
import common.OwnerNotRegisteredException;

/**
 * Interface for a simple loyalty card operator ADT.
 *
 */
public interface ILoyaltyCardOperator {


    /**
     * Registers the specified card owner.
     * @param loyaltyCardOwner the owner to register
     * @throws OwnerAlreadyRegisteredException if the owner's email is already registered
     */
    void registerOwner(ILoyaltyCardOwner loyaltyCardOwner) throws OwnerAlreadyRegisteredException;


    /**
     * Unregisters the specified loyalty card owner.
     * @param loyaltyCardOwner the owner to unregister
     * @throws OwnerNotRegisteredException if the given owner's email is not registered
     */
    void unregisterOwner(ILoyaltyCardOwner loyaltyCardOwner) throws OwnerNotRegisteredException;


    /**
     * Processes a monetary purchase for the specified owner and price and adds pence/100 points to the owners loyalty card.
     * As such, purchases for less than 100 pence do not earn any points.
     * @param ownerEmail the loyalty card owner's email
     * @param pence the price of the purchase in pence
     * @throws OwnerNotRegisteredException if the given owner's email is not registered
     */
    void processMoneyPurchase(String ownerEmail, int pence) throws OwnerNotRegisteredException;


    /**
     * Processes a purchase of an item for the specified price in pence using points on the owner's loyalty card, once earned, each point on the card is worth 1 pence.
     * @param ownerEmail the loyalty card owner's email
     * @param pence the price of the item in pence
     * @throws InsufficientPointsException if the card does not contain at least the same number of points as the price
     * @throws OwnerNotRegisteredException if the given owner's email has not been registered
     */
    void processPointsPurchase(String ownerEmail, int pence) throws InsufficientPointsException, OwnerNotRegisteredException;


    /**
     * Gets the number of loyalty card customers.
     *
     * @return the number of loyalty card customers
     */
    int getNumberOfCustomers();


    /**
     * Gets the total count of all points on all loyalty cards in the system.
     *
     * @return the total count of all points on all loyalty cards in the system
     */
    int getTotalNumberOfPoints();


    /**
     * Gets the number of points on the loyalty card for the specified owner.
     *
     * @param ownerEmail the loyalty card owner's email
     * @return the number of points on the specified owner's loyalty card
     * @throws OwnerNotRegisteredException if the given owner's email is not registered
     */
    int getNumberOfPoints(String ownerEmail) throws OwnerNotRegisteredException;


    /**
     * Gets the total number of times that the owner's loyalty card has been used (for money and points purchases).
     *
     * @param ownerEmail the loyalty card owner's email
     * @return the total number of times that the owner's loyalty card has been used
     * @throws OwnerNotRegisteredException if the given owner's email is not registered
     */
    int getNumberOfUses(String ownerEmail) throws OwnerNotRegisteredException;


    /**
     * Gets the owner who has used their loyalty card the most. The behaviour is undefined if there is not a single most used card.
     *
     * @return the owner who has used their loyalty card the most
     * @throws OwnerNotRegisteredException if no owners have been registered
     */
    ILoyaltyCardOwner getMostUsed() throws OwnerNotRegisteredException;

}

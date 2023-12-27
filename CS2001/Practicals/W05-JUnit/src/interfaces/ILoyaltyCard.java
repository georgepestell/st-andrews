package interfaces;

import common.InsufficientPointsException;

/**
 * This is the interface for a Loyalty Card.
 *
 */
public interface ILoyaltyCard {

    /**
     * Returns the owner of this loyalty card.
     * @return the owner of this loyalty card
     */
    ILoyaltyCardOwner getOwner();


    /**
     * Returns the number of times this card has been used (i.e. the number of times addPoints and usePoints have been called).
     * @return the number of times the card was used
     */
    int getNumberOfUses();

    /**
     * Returns the number points available on the card.
     * @return the number of points on the card
     */
    int getNumberOfPoints();

    /**
     * Adds a number of points to the loyalty card.
     * @param points the number of points to add
     */
    void addPoints(int points);


    /**
     * Use (deduct) a number of points from the loyalty card.
     * @param points the number of points to use (deduct) from this card
     * @throws InsufficientPointsException when there are not enough points on the card
     */
    void usePoints(int points) throws InsufficientPointsException;


}

package impl;

import java.security.InvalidParameterException;

import common.InsufficientPointsException;
import interfaces.ILoyaltyCard;
import interfaces.ILoyaltyCardOwner;

/**
 * This class represents a Loyalty Card, recording information relating to an owners use of the card.
 *
 */
public class LoyaltyCard implements ILoyaltyCard {

    private ILoyaltyCardOwner owner;
    private int points;
    private int useCount;

    public LoyaltyCard(ILoyaltyCardOwner loyaltyCardOwner) {
        if (loyaltyCardOwner == null)
            throw new InvalidParameterException("Owner is null");
        this.owner = loyaltyCardOwner;
        this.points = 0;
        this.useCount = 0;
    }

    @Override
    public ILoyaltyCardOwner getOwner() {
        return this.owner;
    }

    @Override
    public int getNumberOfUses() {
        return this.useCount;
    }

    @Override
    public int getNumberOfPoints() {
        return this.points;
    }

    @Override
    public void addPoints(int points) {
        this.points += Math.max(0, points);
    }

    @Override
    public void usePoints(int points) throws InsufficientPointsException {
        // Check the number of points on the card are enough
        if (points > this.points)
            throw new InsufficientPointsException();

        // If using 0 or negative points, don't tally the useCount or spend points
        if (points <= 0)
            return;

        this.points -= points;
        this.useCount ++;
    }

}

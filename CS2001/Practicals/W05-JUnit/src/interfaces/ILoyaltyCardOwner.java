package interfaces;

/**
 * Interface for an owner of a loyalty card.
 *
 */
public interface ILoyaltyCardOwner {

    /**
     * This method returns the owner's email.
     * @return the email for this owner
     */
    String getEmail();

    /**
     * This method returns the owner's name.
     * @return the name of the owner
     */
    String getName();

}

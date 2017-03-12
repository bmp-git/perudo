package perudo.model;

import java.io.Serializable;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

import perudo.utility.ErrorTypeException;

/**
 * Represent a running game.
 */
public interface Game extends Serializable {
    /**
     * Gets the unique id of the game.
     * 
     * @return id value
     */
    int getId();

    /**
     * Gets the set of users in the game.
     * 
     * @return the set of users
     */
    List<User> getUsers();

    /**
     * Gets the status of a user in the game.
     * 
     * @param user
     *            the user of which is desired the status
     * 
     * @return the status of the user
     */
    PlayerStatus getUserStatus(User user);

    /**
     * Makes a bid.
     * 
     * @param bid
     *            the bid to play
     * 
     * @throws ErrorTypeException
     *             if an error occurs during invocation
     * 
     * @param user
     *            the user who plays
     * 
     */
    void play(Bid bid, User user) throws ErrorTypeException;

    /**
     * Doubts the previous bid.
     * 
     * @param user
     *            the user who doubt
     * 
     * @throws ErrorTypeException
     *             if an error occurs during invocation
     * 
     * @return true if the doubt is correct, false otherwise
     */
    Boolean doubt(User user) throws ErrorTypeException;

    /**
     * Calls a urge.
     * 
     * @param user
     *            the user who urge
     * 
     * @throws ErrorTypeException
     *             if an error occurs during invocation
     * 
     * @return true if the urge is correct, false otherwise
     */
    Boolean urge(User user) throws ErrorTypeException;

    /**
     * Calls palifico for this turn.
     * 
     * @throws ErrorTypeException
     *             if an error occurs during invocation
     * 
     * @param user
     *            the caller.
     * 
     */
    void callPalifico(User user) throws ErrorTypeException;

    /**
     * Indicates if the current turn is palifico.
     * 
     * @return true if someone called palifico this turn.
     * 
     */
    boolean turnIsPalifico();

    /**
     * Gets the settings for this game.
     * 
     * @return the settings
     */
    GameSettings getSettings();

    /**
     * Gets the rules for this game.
     * 
     * @return the rules
     */
    GameRules getRules();

    /**
     * Gets the user whose his turn.
     * 
     * @return the user who must play
     */
    User getTurn();

    /**
     * Gets the last bid.
     * 
     * @return the last bid, if the first play of the turn has not yet been made
     *         the optional will be empty
     */
    Optional<Bid> getCurrentBid();

    /**
     * Gets the user who bids.
     * 
     * @return the user who bid, if the first play of the turn has not yet been
     *         made the optional will be empty
     */
    Optional<User> getBidUser();

    /**
     * Gets the remaining time to play.
     * 
     * @return the remaining time
     */
    Duration getTurnRemainingTime();

    /**
     * Remove the selected user.
     * 
     * @throws ErrorTypeException
     *             if an error occurs during invocation
     * 
     * @param user
     *            the user to remove
     * 
     */
    void removeUser(User user) throws ErrorTypeException;

    /**
     * Checks if the game is over.
     * 
     * @return true if the game is over, false otherwise
     */
    boolean isOver();

    /**
     * Checks if a given user has lost. return true if the user isn't in the
     * game.
     * 
     * @param user
     *            the user to check
     * 
     * @return true if the user has lost, false otherwise
     */
    boolean hasLost(final User user);

    /**
     * Gets the sum of the total dice remaining.
     * 
     * @return the total dice sum.
     */
    int getTotalRemainingDice();

    /** UTILITY METHODS **/

    /**
     * Checks if a given user can play a bid.
     * 
     * @param user
     *            the user to check
     * 
     * @return true if the user can play, false otherwise
     */
    default boolean canPlay(final User user) {
        try {
            this.getRules().checkCanBid(user, this);
            return true;
        } catch (ErrorTypeException e) {
            return false;
        }
    }

    /**
     * Checks if a given user can doubt.
     * 
     * @param user
     *            the user to check
     * 
     * @return true if the user can doubt, false otherwise
     */
    default boolean canDoubt(final User user) {
        try {
            this.getRules().checkCanDoubt(user, this);
            return true;
        } catch (ErrorTypeException e) {
            return false;
        }
    }

    /**
     * Checks if a given user can urge.
     * 
     * @param user
     *            the user to check
     * 
     * @return true if the user can urge, false otherwise
     */
    default boolean canUrge(final User user) {
        try {
            this.getRules().checkCanUrge(user, this);
            return true;
        } catch (ErrorTypeException e) {
            return false;
        }
    }

    /**
     * Checks if a given user can call palifico in this current status of the
     * game.
     * 
     * @param user
     *            the user to check
     * 
     * @return true if the user can call palifico, false otherwise
     */
    default boolean canPalifico(final User user) {
        try {
            this.getRules().checkCanPalifico(user, this);
            return true;
        } catch (ErrorTypeException e) {
            return false;
        }
    }

    /**
     * Gets default next bid based on game's rules.
     * 
     * @return the default bid
     */
    default Bid nextBid() {
        if (this.getCurrentBid().isPresent()) {
            return this.getRules().nextBid(this.getCurrentBid().get().getDiceValue(), this);
        }
        return this.getRules().nextBid(1, this);
    }

    /**
     * Gets default next bid based on game's rules.
     * 
     * @param diceValue
     *            the wanted dice value
     * 
     * @return the next bid
     */
    default Bid nextBid(final int diceValue) {
        return this.getRules().nextBid(diceValue, this);
    }

    /**
     * Gets the minimum dice quantity on a given dice value.
     * 
     * @param diceValue
     *            the dice value
     * 
     * @return the minimum dice quantity
     */
    default int getMinDiceQuantity(final int diceValue) {
        return this.getRules().getMinDiceQuantity(diceValue, this);
    }

    /**
     * Gets the maximum dice quantity on a given dice value. (There is no a
     * maximum but more than getTotalRemainingDice() the bid has no sense).
     * 
     * @param diceValue
     *            the dice value
     * 
     * @return the maximum dice quantity
     */
    default int getMaxDiceQuantity(final int diceValue) {
        return this.getRules().getMaxDiceQuantity(diceValue, this);
    }
}

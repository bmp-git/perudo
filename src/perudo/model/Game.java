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
     * Gets the numbers of dice that apply to the current bid.
     * 
     * @return the quantity of valid dice that count for this bid
     */
    Optional<Integer> getRealBidDiceCount();

    /**
     * Checks if the game is over.
     * 
     * @return true if the game is over, false otherwise
     */
    boolean isOver();

    /** UTILITY METHODS **/

    /**
     * Checks if a given user has lost. return true if the user isn't in the
     * game.
     * 
     * @param user
     *            the user to check
     * 
     * @return true if the user has lost, false otherwise
     */
    default boolean hasLost(final User user) {
        return !this.getUsers().contains(user) || this.getUserStatus(user).getRemainingDice() == 0;
    }

    /**
     * Checks if a given user can play a bid.
     * 
     * @param user
     *            the user to check
     * 
     * @return true if the user can play, false otherwise
     */
    default boolean canPlay(final User user) {
        return this.getTurn().equals(user);
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
        return this.getTurn().equals(user) && this.getCurrentBid().isPresent();
    }

    default boolean canUrge(final User user) {
        return !hasLost(user) && !this.getTurn().equals(user) && this.getBidUser().isPresent()
                && !this.getBidUser().get().equals(user);
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
        return this.getUsers().contains(user) && this.getUserStatus(user).getRemainingDice() == 1
                && !this.getUserStatus(user).hasCalledPalifico() && !this.turnIsPalifico()
                && !this.getCurrentBid().isPresent();
    }

    /**
     * Gets the sum of the total dice remaining.
     * 
     * @return the total dice sum.
     */
    default int getTotalRemainingDice() {
        return this.getUsers().stream().mapToInt(u -> this.getUserStatus(u).getRemainingDice()).sum();
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
        if (!this.getCurrentBid().isPresent()) {
            return 1;
        }
        if (this.turnIsPalifico() && diceValue != this.getCurrentBid().get().getDiceValue()) {
            throw new IllegalStateException("You can't play different dice value if the turn is palifico!");
        }
        return this.getCurrentBid().get().nextBid(diceValue).getQuantity();
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
        int minDiceQuantity = this.getMinDiceQuantity(diceValue);
        if (!this.getCurrentBid().isPresent()) {
            return getTotalRemainingDice();
        }
        if (minDiceQuantity >= this.getTotalRemainingDice()) {
            return minDiceQuantity + 1;
        }
        return this.getTotalRemainingDice();
    }
}

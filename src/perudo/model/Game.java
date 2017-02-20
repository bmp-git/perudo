package perudo.model;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import perudo.utility.ErrorTypeException;

public interface Game {
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
     * @return the status of the user
     */
    PlayerStatus getUserStatus(User user);

    /**
     * Makes a bid.
     * 
     * @param bid
     *            the bid to play
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
     * @return true if the doubt is correct, false otherwise
     */
    Boolean doubt(User user) throws ErrorTypeException;

    /**
     * Calls a urge.
     * 
     * @param user
     *            the user who urge
     * 
     * @return true if the urge is correct, false otherwise
     */
    Boolean urge(User user) throws ErrorTypeException;

    /**
     * Calls palifico for this turn.
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
     * Gets the user whose his turn
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
     * Gets the numbers of dice that apply to the current bid
     * 
     * @return the quantity of valid dice that count for this bid
     */
    int getRealBidDiceCount();
    
    /**
     * Gets the remaining time to play.
     * 
     * @return the remaining time
     */
    Duration getTurnRemainingTime();

    /**
     * Checks if the game is over.
     * 
     * @return true if the game is over, false otherwise
     */
    default boolean isOver() {
        return getUsers().stream().filter(u -> getUserStatus(u).getRemainingDice() > 0).count() <= 1;
    }

    /**
     * Remove the selected user
     * 
     * @param user
     *            the user to remove
     * 
     */
    void removeUser(User user) throws ErrorTypeException;
}

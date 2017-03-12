package perudo.model;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

import perudo.utility.ErrorTypeException;

/**
 * This interface has all the method that define a game's set of rules.
 */
public interface GameRules extends Serializable {

    /**
     * Checks if an user can bid in a given game.
     * 
     * @param user
     *            the user to check
     * 
     * @param game
     *            the actual state of the game
     * 
     * @throws ErrorTypeException
     *             if something is wrong an exception with the specified error
     *             type will be thrown
     */
    void checkCanBid(User user, Game game) throws ErrorTypeException;

    /**
     * Checks if an user can urge in a given game.
     * 
     * @param user
     *            the user to check
     * 
     * @param game
     *            the actual state of the game
     * 
     * @throws ErrorTypeException
     *             if something is wrong an exception with the specified error
     *             type will be thrown
     */
    void checkCanUrge(User user, Game game) throws ErrorTypeException;

    /**
     * Checks if an user can doubt in a given game.
     * 
     * @param user
     *            the user to check
     * 
     * @param game
     *            the actual state of the game
     * 
     * @throws ErrorTypeException
     *             if something is wrong an exception with the specified error
     *             type will be thrown
     */
    void checkCanDoubt(User user, Game game) throws ErrorTypeException;

    /**
     * Checks if an user can call palifico in a given game.
     * 
     * @param user
     *            the user to check
     * 
     * @param game
     *            the actual state of the game
     * 
     * @throws ErrorTypeException
     *             if something is wrong an exception with the specified error
     *             type will be thrown
     */
    void checkCanPalifico(User user, Game game) throws ErrorTypeException;

    /**
     * Checks if a bid is valid in a given game.
     * 
     * @param bid
     *            the bid to check
     * 
     * @param game
     *            the actual state of the game
     * 
     * @return true if the bid is valid, false otherwise
     */
    boolean bidValid(Bid bid, Game game);

    /**
     * Gets the next user after a bid.
     * 
     * @param user
     *            the user who had bid
     * @param game
     *            the actual state of the game
     * 
     * @return the new user in turn
     */
    User nextTurnBid(User user, Game game);

    /**
     * Gets the next user after a palifico call.
     * 
     * @param user
     *            the user who had calls palifico
     * @param game
     *            the actual state of the game
     * 
     * @return the new user in turn
     */
    User nextTurnPalifico(User user, Game game);

    /**
     * Gets the next user after doubt.
     * 
     * @param user
     *            the user who had doubt
     * @param game
     *            the actual state of the game
     * @param win
     *            indicates if the user win the doubt
     * 
     * @return the new user in turn
     */
    User nextTurnDoubt(User user, Game game, boolean win);

    /**
     * Gets the next user after a urge call.
     * 
     * @param user
     *            the user who calls had urge
     * @param game
     *            the actual state of the game
     * 
     * @param win
     *            indicates if the user win the call
     * 
     * @return the new user in turn
     */
    User nextTurnUrge(User user, Game game, boolean win);

    /**
     * Checks if a doubt call would win.
     * 
     * @param game
     *            the actual state of the game
     * 
     * @return true if the doubt would win, false otherwise
     */
    boolean doubtWin(Game game);

    /**
     * Checks if a urge call would win.
     * 
     * @param game
     *            the actual state of the game
     * 
     * @return true if the urge would win, false otherwise
     */
    boolean urgeWin(Game game);

    /**
     * Gets the numbers of dice that apply to the current bid.
     * 
     * @param game
     *            the actual state of the game
     * 
     * @return the quantity of valid dice that count for this bid
     */
    Optional<Integer> getRealBidDiceCount(Game game);

    /**
     * Gets the dice that should be added or removed after a urge.
     * 
     * @param user
     *            the user who urge
     * @param game
     *            the actual state of the game
     * @param win
     *            indicates if the player wins the urge
     * 
     * @return a map from User to Integer, each pair represent the user and the
     *         dice to add (negative value if dice to remove)
     */
    Map<User, Integer> changesDiceUrge(User user, Game game, boolean win);

    /**
     * Gets the dice that should be added or removed after a doubt.
     * 
     * @param user
     *            the user who doubt
     * @param game
     *            the actual state of the game
     * @param win
     *            indicates if the player wins the doubt
     * 
     * @return a map from User to Integer, each pair represent the user and the
     *         dice to add (negative value if dice to remove)
     */
    Map<User, Integer> changesDiceDoubt(User user, Game game, boolean win);

    /**
     * Gets the minimum dice quantity that will be allowed to play.
     * 
     * @param diceValue
     *            the desired dice value
     * @param game
     *            the actual state of the game
     * 
     * @return the minimum quantity
     */
    int getMinDiceQuantity(int diceValue, Game game);

    /**
     * Gets the maximum dice quantity that will have sense to play.
     * 
     * @param diceValue
     *            the desired dice value
     * @param game
     *            the actual state of the game
     * 
     * @return the maximum quantity
     */
    int getMaxDiceQuantity(int diceValue, Game game);

    /**
     * Gets the next minimum bid at the actual state of the game.
     * 
     * @param diceValue
     *            the desired dice value
     * @param game
     *            the actual state of the game
     * 
     * @return an instance of bid that indicates the next bid
     */
    Bid nextBid(int diceValue, Game game);
}

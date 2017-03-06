package perudo.model;

import java.io.Serializable;
import java.util.Map;

import perudo.utility.ErrorTypeException;

/**
 * Represent the status of a player in game.
 */
public interface PlayerStatus extends Serializable {
    /**
     * Gets a map of {face value, face value count} of the current player's
     * status. Ex. if the hand is 1,1,3,3,4 with dice of six faces the map would
     * be: [1 - 2] [2 - 0] [3 - 2] [4 - 1] [5 - 0] [6 - 0] (key - value)
     * 
     * @return the count of the dice
     */
    Map<Integer, Integer> getDiceCount();

    /**
     * Gets the amount of dice remaining.
     * 
     * @return the amount of dice
     */
    int getRemainingDice();

    /**
     * Gets the amount of dice's faces.
     * 
     * @return the amount of dice
     */
    int getMaxDiceValue();

    /**
     * Gets if the palifico was called.
     * 
     * @return true if palifico was called, false otherwise
     */
    boolean hasCalledPalifico();

    /**
     * Roll the dice.
     * 
     * @return the PlayerStatus with rolled dice
     */
    PlayerStatus rollDice();

    /**
     * Sets the value of remaining dice.
     * 
     * @param remainingDice
     *            the remaining dice
     * @return the PlayerStatus with the new remaining
     */
    PlayerStatus setRemainingDice(int remainingDice);

    /**
     * Calls palifico, can be called only once.
     * 
     * @throws ErrorTypeException
     *             if an error occurs during invocation
     * 
     * @return the PlayerStatus with palifico called
     */
    PlayerStatus callPalifico() throws ErrorTypeException;
}

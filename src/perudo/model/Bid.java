package perudo.model;

import java.io.Serializable;

/**
 * Represent a bid of the game. (example: 2 dice of 4)
 */
public interface Bid extends Serializable {
    /**
     * Gets the value of the dice face.
     * 
     * @return dice face value
     */
    int getDiceValue();

    /**
     * Gets the amount of dice.
     * 
     * @return amount of dice
     */
    int getQuantity(); 
}
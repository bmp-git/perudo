package perudo.model;

import java.io.Serializable;
import java.time.Duration;

/**
 * Represent a set of settings for a game.
 */
public interface GameSettings extends Serializable {

    /**
     * Gets the maximum players.
     * 
     * @return numbers of players
     */
    int getMaxPlayer();

    /**
     * Gets the maximum dice value.
     * 
     * @return dice value
     */
    int getMaxDiceValue();

    /**
     * Gets the initial amount of dice.
     * 
     * @return amount of dice
     */
    int getInitialDiceNumber();

    /**
     * Gets the amount of time available for one turn.
     * 
     * @return amount of time
     */
    Duration getMaxTurnTime();

    /**
     * Gets the name.
     * 
     * @return the name string
     */
    String getName();
}

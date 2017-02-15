package perudo.model;

import java.time.Duration;

public interface GameSettings {

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
     * Gets the amount of time available for one turn
     * 
     * @return amount of time
     */
    Duration getMaxTurnTime();
}

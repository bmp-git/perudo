package perudo.model.impl;

import java.time.Duration;

import perudo.model.GameSettings;

/**
 * The implementation of GameSettings interface.
 */
public class GameSettingsImpl implements GameSettings {

    private static final long serialVersionUID = 2049188859139952036L;

    /**
     * Minimum players number.
     */
    public static final int MIN_PLAYER_NUMBERS = 2;
    /**
     * Maximum players number.
     */
    public static final int MAX_PLAYER_NUMBERS = 8;

    /**
     * Minimum dice value (number of faces).
     */
    public static final int MIN_DICE_FACES = 3;
    /**
     * Maximum dice value (number of faces).
     */
    public static final int MAX_DICE_FACES = 10;

    /**
     * Minimum starting dice number.
     */
    public static final int MIN_INITIAL_DICE_NUMBERS = 1;
    /**
     * Maximum starting dice number.
     */
    public static final int MAX_INITIAL_DICE_NUMBERS = 8;

    /**
     * Minimum turn duration time.
     */
    public static final Duration MIN_TURN_TIME = Duration.ofSeconds(15);
    /**
     * Maximum turn duration time.
     */
    public static final Duration MAX_TURN_TIME = Duration.ofSeconds(600);

    private final int maxPlayer, initialDiceNumber, maxDiceValue;
    private final Duration maxTurnTime;
    private final String name;

    /**
     * Create a GameSettings with the specified parameters.
     * 
     * @param playerNumbers
     *            the maximum number of player allowed to join
     * 
     * @param diceFaces
     *            the maximum value for the dice
     * 
     * @param initialDiceNumbers
     *            the initial dice number
     * 
     * @param turnTime
     *            the maximum turn time
     * 
     * @param name
     *            the name of this set of settings
     * 
     * @throws IllegalArgumentException
     *             if some of the arguments are not inside the allowed range
     */
    public GameSettingsImpl(final int playerNumbers, final int diceFaces, final int initialDiceNumbers,
            final Duration turnTime, final String name) {
        if (playerNumbers < MIN_PLAYER_NUMBERS) {
            throw new IllegalArgumentException("initialDiceNumber should be at least " + MIN_INITIAL_DICE_NUMBERS);
        }
        if (diceFaces < MIN_DICE_FACES) {
            throw new IllegalArgumentException("maxDiceValue should be at least " + MIN_DICE_FACES);
        }
        if (initialDiceNumbers < MIN_INITIAL_DICE_NUMBERS) {
            throw new IllegalArgumentException("initialDiceNumber should be at least " + MIN_INITIAL_DICE_NUMBERS);
        }
        if (turnTime.compareTo(MIN_TURN_TIME) < 0) {
            throw new IllegalArgumentException("maxTurnTime should be at least " + MIN_TURN_TIME);
        }

        if (playerNumbers > MAX_PLAYER_NUMBERS) {
            throw new IllegalArgumentException("maxPlayer should be at most " + MAX_PLAYER_NUMBERS);
        }
        if (diceFaces > MAX_DICE_FACES) {
            throw new IllegalArgumentException("maxDiceValue should be at most " + MAX_DICE_FACES);
        }
        if (initialDiceNumbers > MAX_INITIAL_DICE_NUMBERS) {
            throw new IllegalArgumentException("initialDiceNumber should be at most " + MAX_INITIAL_DICE_NUMBERS);
        }
        if (turnTime.compareTo(MAX_TURN_TIME) > 0) {
            throw new IllegalArgumentException("maxTurnTime should be at most " + MAX_TURN_TIME);
        }

        this.maxPlayer = playerNumbers;
        this.maxDiceValue = diceFaces;
        this.initialDiceNumber = initialDiceNumbers;
        this.maxTurnTime = turnTime;
        this.name = name == null ? "" : name;
    }

    @Override
    public int getMaxPlayer() {
        return this.maxPlayer;
    }

    @Override
    public int getMaxDiceValue() {
        return this.maxDiceValue;
    }

    @Override
    public int getInitialDiceNumber() {
        return this.initialDiceNumber;
    }

    @Override
    public Duration getMaxTurnTime() {
        return this.maxTurnTime;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + initialDiceNumber;
        result = prime * result + maxDiceValue;
        result = prime * result + maxPlayer;
        result = prime * result + ((maxTurnTime == null) ? 0 : maxTurnTime.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GameSettingsImpl other = (GameSettingsImpl) obj;
        if (initialDiceNumber != other.initialDiceNumber) {
            return false;
        }
        if (maxDiceValue != other.maxDiceValue) {
            return false;
        }
        if (maxPlayer != other.maxPlayer) {
            return false;
        }
        if (maxTurnTime == null) {
            if (other.maxTurnTime != null) {
                return false;
            }
        } else if (!maxTurnTime.equals(other.maxTurnTime)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(79);
        sb.append("Game settings[MAX PLAYER(");
        sb.append(this.getMaxPlayer());
        sb.append("), DICE FACES(");
        sb.append(this.getMaxDiceValue());
        sb.append("), INITIAL DICE(");
        sb.append(this.getInitialDiceNumber());
        sb.append("), TURN TIME(");
        sb.append(this.getMaxTurnTime().getSeconds());
        sb.append("s), NAME(");
        sb.append(this.getName());
        sb.append(")]");
        return sb.toString();
    }

}

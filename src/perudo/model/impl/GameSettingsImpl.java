package perudo.model.impl;

import java.time.Duration;

import perudo.model.GameSettings;

public class GameSettingsImpl implements GameSettings {
    public static final int MIN_MAX_PLAYER = 2;
    public static final int MAX_MAX_PLAYER = 10;

    public static final int MIN_MAX_DICE_VALUE = 3;
    public static final int MAX_MAX_DICE_VALUE = 20;

    public static final int MIN_INITIAL_DICE_NUMBER = 1;
    public static final int MAX_INITIAL_DICE_NUMBER = 10;

    public static final Duration MIN_MAX_TURN_TIME = Duration.ofSeconds(15);
    public static final Duration MAX_MAX_TURN_TIME = Duration.ofSeconds(600);

    private final int maxPlayer, initialDiceNumber, maxDiceValue;
    private final Duration maxTurnTime;

    public GameSettingsImpl(final int maxPlayer, final int maxDiceValue, final int initialDiceNumber,
            final Duration maxTurnTime) {
        if (initialDiceNumber < MIN_INITIAL_DICE_NUMBER) {
            throw new IllegalArgumentException("initialDiceNumber should be at least " + MIN_INITIAL_DICE_NUMBER);
        }
        if (maxDiceValue < MIN_MAX_DICE_VALUE) {
            throw new IllegalArgumentException("maxDiceValue should be at least " + MIN_MAX_DICE_VALUE);
        }
        if (initialDiceNumber < MIN_INITIAL_DICE_NUMBER) {
            throw new IllegalArgumentException("initialDiceNumber should be at least " + MIN_INITIAL_DICE_NUMBER);
        }
        if (maxTurnTime.compareTo(MIN_MAX_TURN_TIME) < 0) {
            throw new IllegalArgumentException("maxTurnTime should be at least " + MIN_MAX_TURN_TIME);
        }

        if (maxPlayer > MAX_MAX_PLAYER) {
            throw new IllegalArgumentException("maxPlayer should be at most " + MAX_MAX_PLAYER);
        }
        if (maxDiceValue > MAX_MAX_DICE_VALUE) {
            throw new IllegalArgumentException("maxDiceValue should be at most " + MAX_MAX_DICE_VALUE);
        }
        if (initialDiceNumber > MAX_INITIAL_DICE_NUMBER) {
            throw new IllegalArgumentException("initialDiceNumber should be at most " + MAX_INITIAL_DICE_NUMBER);
        }
        if (maxTurnTime.compareTo(MAX_MAX_TURN_TIME) > 0) {
            throw new IllegalArgumentException("maxTurnTime should be at most " + MAX_MAX_TURN_TIME);
        }

        this.maxPlayer = maxPlayer;
        this.maxDiceValue = maxDiceValue;
        this.initialDiceNumber = initialDiceNumber;
        this.maxTurnTime = maxTurnTime;
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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + initialDiceNumber;
        result = prime * result + maxDiceValue;
        result = prime * result + maxPlayer;
        result = prime * result + ((maxTurnTime == null) ? 0 : maxTurnTime.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GameSettingsImpl other = (GameSettingsImpl) obj;
        if (initialDiceNumber != other.initialDiceNumber)
            return false;
        if (maxDiceValue != other.maxDiceValue)
            return false;
        if (maxPlayer != other.maxPlayer)
            return false;
        if (maxTurnTime == null) {
            if (other.maxTurnTime != null)
                return false;
        } else if (!maxTurnTime.equals(other.maxTurnTime))
            return false;
        return true;
    }

}

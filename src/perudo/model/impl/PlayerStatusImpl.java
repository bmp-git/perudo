package perudo.model.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import perudo.model.GameSettings;
import perudo.model.PlayerStatus;
import perudo.utility.ErrorType;
import perudo.utility.ErrorTypeException;

public class PlayerStatusImpl implements PlayerStatus {

    public static PlayerStatus fromGameSettings(final GameSettings settings) {
        return new PlayerStatusImpl(settings.getInitialDiceNumber(), settings.getMaxDiceValue(), false);
    }

    private static Random random = new Random();

    private final Map<Integer, Integer> diceValues;
    private final int remainingDice, maxDiceValue;
    private final boolean hasCalledPalifico;

    public PlayerStatusImpl(final int remainingDice, final int maxDiceValue, final boolean hasCalledPalifico) {
        if (remainingDice < 0) {
            throw new IllegalArgumentException("remainingDice should be positive");
        }
        if (maxDiceValue < 0) {
            throw new IllegalArgumentException("maxDiceValue should be positive");
        }
        this.remainingDice = remainingDice;
        this.hasCalledPalifico = hasCalledPalifico;
        this.maxDiceValue = maxDiceValue;

        // roll dice
        this.diceValues = new HashMap<>();
        for (Integer i = 1; i <= this.getMaxDiceValue(); i++) {
            this.diceValues.put(i, 0);
        }
        for (Integer i = 0; i < this.getRemainingDice(); i++) {
            Integer diceV = random.nextInt(this.getMaxDiceValue()) + 1;
            this.diceValues.put(diceV, this.diceValues.get(diceV) + 1);
        }
    }

    public PlayerStatus rollDice() {
        return new PlayerStatusImpl(this.getRemainingDice(), this.getMaxDiceValue(), this.hasCalledPalifico());

    }

    public Map<Integer, Integer> getDiceCount() {
        return Collections.unmodifiableMap(this.diceValues);
    }

    public int getRemainingDice() {
        return this.remainingDice;
    }

    public int getMaxDiceValue() {
        return this.maxDiceValue;
    }

    public PlayerStatus setRemainingDice(final int remainingDice) {
        if (remainingDice < 0) {
            throw new IllegalArgumentException();
        }
        return new PlayerStatusImpl(remainingDice, this.getMaxDiceValue(), this.hasCalledPalifico());
    }

    public boolean hasCalledPalifico() {
        return this.hasCalledPalifico;
    }

    public PlayerStatus callPalifico() throws ErrorTypeException {
        if (this.hasCalledPalifico) {
            throw new ErrorTypeException(ErrorType.GAME_PALIFICO_ALREADY_USED);
        }
        if (this.getRemainingDice() != 1) {
            throw new ErrorTypeException(ErrorType.GAME_CANT_CALL_PALIFICO_NOW);
        }
        return new PlayerStatusImpl(this.getRemainingDice(), this.getMaxDiceValue(), true);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((diceValues == null) ? 0 : diceValues.hashCode());
        result = prime * result + (hasCalledPalifico ? 1231 : 1237);
        result = prime * result + maxDiceValue;
        result = prime * result + remainingDice;
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
        PlayerStatusImpl other = (PlayerStatusImpl) obj;
        if (diceValues == null) {
            if (other.diceValues != null)
                return false;
        } else if (!diceValues.equals(other.diceValues))
            return false;
        if (hasCalledPalifico != other.hasCalledPalifico)
            return false;
        if (maxDiceValue != other.maxDiceValue)
            return false;
        if (remainingDice != other.remainingDice)
            return false;
        return true;
    }

}

package perudo.model.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import perudo.model.GameSettings;
import perudo.model.PlayerStatus;
import perudo.utility.ErrorType;
import perudo.utility.ErrorTypeException;

/**
 * The implementation of PlayerStatus interface.
 */
public class PlayerStatusImpl implements PlayerStatus {

    private static final long serialVersionUID = -9211044365486847226L;

    private static Random random = new Random();
    private final Map<Integer, Integer> diceValues;
    private final int remainingDice, maxDiceValue;
    private final boolean palificoCalled;

    /**
     * Create a PlayerStatus from GameSettings. It is the state of a user when a
     * game starts.
     * 
     * @param settings
     *            the settings of the game
     * 
     * @return an instance of PlayerStatus
     */
    public static PlayerStatus fromGameSettings(final GameSettings settings) {
        return new PlayerStatusImpl(settings.getInitialDiceNumber(), settings.getMaxDiceValue(), false);
    }

    private PlayerStatusImpl(final int remainingDice, final int maxDiceValue, final boolean hasCalledPalifico,
            final Map<Integer, Integer> diceValues) {
        this.remainingDice = remainingDice;
        this.palificoCalled = hasCalledPalifico;
        this.maxDiceValue = maxDiceValue;
        this.diceValues = diceValues.entrySet().stream().collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
    }

    /**
     * Create a PlayerStatus with the specified arguments.
     * 
     * @param remainingDice
     *            the remaining dice number
     * @param maxDiceValue
     *            the maximum dice value (number of dice's faces)
     * @param hasCalledPalifico
     *            Indicate if the palifico was already called
     * 
     */
    public PlayerStatusImpl(final int remainingDice, final int maxDiceValue, final boolean hasCalledPalifico) {
        if (remainingDice < 0) {
            throw new IllegalArgumentException("remainingDice should be positive");
        }
        if (maxDiceValue <= 1) {
            throw new IllegalArgumentException("dice faces should be at least 1");
        }
        this.remainingDice = remainingDice;
        this.palificoCalled = hasCalledPalifico;
        this.maxDiceValue = maxDiceValue;

        this.diceValues = new HashMap<>();
        for (Integer i = 1; i <= this.maxDiceValue; i++) {
            this.diceValues.put(i, 0);
        }
        // roll dice
        for (Integer i = 0; i < this.remainingDice; i++) {
            final Integer diceV = random.nextInt(this.maxDiceValue) + 1;
            this.diceValues.put(diceV, this.diceValues.get(diceV) + 1);
        }

    }

    @Override
    public PlayerStatus rollDice() {
        return new PlayerStatusImpl(this.getRemainingDice(), this.getMaxDiceValue(), this.hasCalledPalifico());
    }

    @Override
    public PlayerStatus setRemainingDice(final int remainingDice) {
        return new PlayerStatusImpl(remainingDice, this.getMaxDiceValue(), this.hasCalledPalifico());
    }

    @Override
    public PlayerStatus callPalifico() throws ErrorTypeException {
        if (this.palificoCalled) {
            throw new ErrorTypeException(ErrorType.GAME_PALIFICO_ALREADY_USED);
        }
        if (this.getRemainingDice() != 1) {
            throw new ErrorTypeException(ErrorType.GAME_CANT_CALL_PALIFICO_NOW);
        }
        return new PlayerStatusImpl(this.getRemainingDice(), this.getMaxDiceValue(), true, this.getDiceCount());
    }

    @Override
    public Map<Integer, Integer> getDiceCount() {
        return Collections.unmodifiableMap(this.diceValues);
    }

    @Override
    public int getRemainingDice() {
        return this.remainingDice;
    }

    @Override
    public int getMaxDiceValue() {
        return this.maxDiceValue;
    }

    @Override
    public boolean hasCalledPalifico() {
        return this.palificoCalled;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        final int h1 = 1231, h2 = 1237;
        int result = 1;
        result = prime * result + ((diceValues == null) ? 0 : diceValues.hashCode());
        result = prime * result + (palificoCalled ? h1 : h2);
        result = prime * result + maxDiceValue;
        result = prime * result + remainingDice;
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
        final PlayerStatusImpl other = (PlayerStatusImpl) obj;
        if (diceValues == null) {
            if (other.diceValues != null) {
                return false;
            }
        } else if (!diceValues.equals(other.diceValues)) {
            return false;
        }
        if (palificoCalled != other.palificoCalled) {
            return false;
        }
        if (maxDiceValue != other.maxDiceValue) {
            return false;
        }
        return remainingDice == other.remainingDice;
    }

}

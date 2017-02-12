package perudo.model;

import java.util.Map;

public interface PlayerStatus {
    Map<Integer, Integer> getDiceCount();

    int getRemainingDice();

    int getMaxDiceValue();

    boolean hasCalledPalifico();

    PlayerStatus rollDice();

    PlayerStatus setRemainingDice(final int remainingDice);

    PlayerStatus setHasCalledPalifico(final boolean hasCalledPalifico);
}

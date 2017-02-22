package perudo.model;

import java.io.Serializable;

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

    /**
     * Check if a bid is valid to bid in the next turn.
     * 
     * @param nextBid
     *            indicates the bid to check
     * @param turnIsPalifico
     *            indicates if the current turn is palifico
     * @param gameSettings
     *            indicates the settings of the game
     * @return true if the bid is valid, false otherwise
     */
    boolean isNextBidValid(Bid nextBid, boolean turnIsPalifico, GameSettings gameSettings);

    /**
     * Gets a new bid with a minimum increase of this bid.
     * 
     * @param diceValue
     *            the desired dice value
     * 
     * @return the new bid increased
     */
    Bid nextBid(int diceValue);

    /**
     * Gets a new bid with a minimum increase of this bid.
     * 
     * @return the new bid increased
     */
    default Bid nextBid() {
        return this.nextBid(this.getDiceValue());
    }
}
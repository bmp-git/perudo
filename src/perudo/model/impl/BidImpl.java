package perudo.model.impl;

import perudo.model.Bid;
import perudo.model.GameSettings;
import perudo.utility.ErrorType;
import perudo.utility.ErrorTypeException;

/**
 * The implementation of Bid interface following the rule of the classic game.
 */
public class BidImpl implements Bid {

    private static final long serialVersionUID = -50636686196288999L;

    private final int diceValue, quantity;

    /**
     * Creates a Bid.
     * 
     * @param quantity
     *            dice quantity
     * 
     * @param diceValue
     *            dice value (the face)
     * 
     * @throws ErrorTypeException
     *             if the arguments are invalid
     */
    public BidImpl(final int quantity, final int diceValue) throws ErrorTypeException {
        if (quantity < 0 || diceValue < 1) {
            throw new ErrorTypeException(ErrorType.GAME_INVALID_BID);
        }
        this.diceValue = diceValue;
        this.quantity = quantity;
    }

    @Override
    public boolean isNextBidValid(final Bid nextBid, final boolean turnIsPalifico, final GameSettings gameSettings) {
        if (nextBid.getDiceValue() > gameSettings.getMaxDiceValue()) {
            return false;
        }
        if (turnIsPalifico) {
            if (nextBid.getDiceValue() != this.getDiceValue()) {
                return false;
            }

            // only more dice
            return nextBid.getQuantity() > this.getQuantity();
        }

        // current bid on jolly
        if (this.getDiceValue() == 1) {
            // bid to check on jolly
            if (nextBid.getDiceValue() == 1) {
                // at least one more jolly
                return nextBid.getQuantity() >= (this.getQuantity() + 1);
            } else {
                // at least twice plus one dice
                return nextBid.getQuantity() >= (this.getQuantity() * 2 + 1);
            }
        }

        // bid to check on jolly
        if (nextBid.getDiceValue() == 1) {
            // current bid is not on jolly for sure
            // at least half dice. ex from 4 to 2 or from 5 to 3
            return nextBid.getQuantity() >= ((this.getQuantity() + 1) / 2);
        }

        // nor actual bid is jolly nor the bid to check

        // dice value is higher
        if (nextBid.getDiceValue() > this.getDiceValue()) {
            // same or more dice
            return nextBid.getQuantity() >= this.getQuantity();
        } else {
            // only more dice
            return nextBid.getQuantity() > this.getQuantity();
        }
    }

    @Override
    public int getDiceValue() {
        return diceValue;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + diceValue;
        result = prime * result + quantity;
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
        BidImpl other = (BidImpl) obj;
        if (diceValue != other.diceValue) {
            return false;
        }
        if (quantity != other.quantity) {
            return false;
        }
        return true;
    }

    @Override
    public Bid nextBid(final int diceValue) throws ErrorTypeException {
        int quantity = 0;
        if (diceValue == 1 && this.diceValue == 1) {
            quantity = this.quantity + 1;
        } else if (diceValue != 1 && this.diceValue == 1) {
            quantity = this.quantity * 2 + 1;
        } else if (diceValue == 1 && this.diceValue != 1) {
            quantity = (this.quantity + 1) / 2;
        } else if (diceValue > this.diceValue) {
            quantity = this.quantity;
        } else if (diceValue <= this.diceValue) {
            quantity = this.quantity + 1;
        }

        return new BidImpl(quantity, diceValue);
    }

}

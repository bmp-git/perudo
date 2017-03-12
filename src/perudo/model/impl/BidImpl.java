package perudo.model.impl;

import perudo.model.Bid;
import perudo.utility.ErrorTypeException;

/**
 * The implementation of Bid interface.
 */
public class BidImpl implements Bid {

    private static final long serialVersionUID = -50636686196288999L;

    /**
     * The minimum playable dice quantity.
     */
    public static final int MIN_DICE_QUANTITY = 0;
    /**
     * The minimum playable dice value.
     */
    public static final int MIN_DICE_VALUE = 1;

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
    public BidImpl(final int quantity, final int diceValue) {
        if (quantity < MIN_DICE_QUANTITY) {
            throw new IllegalArgumentException("Quantity should be at least " + MIN_DICE_QUANTITY);
        }
        if (diceValue < MIN_DICE_VALUE) {
            throw new IllegalArgumentException("Dice value should be at least " + MIN_DICE_VALUE);
        }
        this.diceValue = diceValue;
        this.quantity = quantity;
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
        final BidImpl other = (BidImpl) obj;
        if (diceValue != other.diceValue) {
            return false;
        }
        return quantity == other.quantity;
    }


}

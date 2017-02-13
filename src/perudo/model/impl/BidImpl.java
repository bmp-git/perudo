package perudo.model.impl;

import perudo.model.Bid;

public class BidImpl implements Bid {

    private final int diceValue, quantity;

    public BidImpl(final int quantity, final int diceValue) {
        if (quantity < 0) {
            throw new IllegalArgumentException("quantity should be at least 0");
        }
        if (diceValue < 1) {
            throw new IllegalArgumentException("diceValue should be at least 1");
        }
        this.diceValue = diceValue;
        this.quantity = quantity;
    }

    public boolean isNextBidValid(final Bid nextBid, final boolean turnIsPalifico) {
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

    public int getDiceValue() {
        return diceValue;
    }

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
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BidImpl other = (BidImpl) obj;
        if (diceValue != other.diceValue)
            return false;
        if (quantity != other.quantity)
            return false;
        return true;
    }

}

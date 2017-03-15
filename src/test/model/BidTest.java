package test.model;

import static org.junit.Assert.assertEquals;

import perudo.model.Bid;
import perudo.model.impl.BidImpl;

/**
 * Test for Bid implementations.
 */
public class BidTest {

    /**
     * The main test.
     */
    @org.junit.Test
    public void main() {
        Bid bid = null;
        try {
            bid = new BidImpl(-1, 1);
            throw new IllegalStateException("Under limit");
        } catch (Exception ex) {
            assertEquals(ex.getClass(), IllegalArgumentException.class);
        }
        try {
            bid = new BidImpl(1, 0);
            throw new IllegalStateException("Under limit");
        } catch (Exception ex) {
            assertEquals(ex.getClass(), IllegalArgumentException.class);
        }
        try {
            bid = new BidImpl(1, 1);
        } catch (Exception ex) {
            throw new IllegalStateException("Should be ok");
        }
        assertEquals(bid.getQuantity(), 1);
        assertEquals(bid.getDiceValue(), 1);
    }
}

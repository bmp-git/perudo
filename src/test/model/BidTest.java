package test.model;

import perudo.model.Bid;
import perudo.model.GameSettings;
import perudo.model.impl.BidImpl;
import perudo.model.impl.GameSettingsImpl;
import perudo.utility.ErrorType;
import perudo.utility.ErrorTypeException;

import static org.junit.Assert.*;

import java.time.Duration;

public class BidTest {
    @org.junit.Test
    public void main() {
        Bid bid = null;
        GameSettings setts = new GameSettingsImpl(5, 6, 5, Duration.ofMinutes(2));
        try {
            bid = new BidImpl(-1, 1);
            throw new IllegalStateException("Under limit");
        } catch (ErrorTypeException ex) {
            assertEquals(ex.getErrorType(), ErrorType.GAME_INVALID_BID);
        }
        try {
            bid = new BidImpl(0, 0);
            throw new IllegalStateException("Under limit");
        } catch (ErrorTypeException ex) {
            assertEquals(ex.getErrorType(), ErrorType.GAME_INVALID_BID);
        }

        try {
            bid = new BidImpl(0, 1);
        } catch (Exception ex) {
            throw new IllegalStateException("Should be ok");
        }

        assertEquals(bid.getQuantity(), 0);
        assertEquals(bid.getDiceValue(), 1);

        try {
            assertTrue(bid.isNextBidValid(new BidImpl(1, 1), false, setts));
            assertTrue(bid.isNextBidValid(new BidImpl(1, 6), false, setts));

            bid = new BidImpl(2, 1);

            assertTrue(bid.isNextBidValid(new BidImpl(3, 1), false, setts));
            assertTrue(bid.isNextBidValid(new BidImpl(5, 6), false, setts));

            assertFalse(bid.isNextBidValid(new BidImpl(10, 2), true, setts));
            assertFalse(bid.isNextBidValid(new BidImpl(4, 6), false, setts));

            bid = new BidImpl(5, 5);

            assertTrue(bid.isNextBidValid(new BidImpl(3, 1), false, setts));
            assertTrue(bid.isNextBidValid(new BidImpl(6, 4), false, setts));
            assertTrue(bid.isNextBidValid(new BidImpl(5, 6), false, setts));

            assertFalse(bid.isNextBidValid(new BidImpl(4, 1), true, setts));
            assertFalse(bid.isNextBidValid(new BidImpl(2, 1), false, setts));

            bid = new BidImpl(4, 5);

            assertTrue(bid.isNextBidValid(new BidImpl(2, 1), false, setts));
            assertFalse(bid.isNextBidValid(new BidImpl(1, 1), false, setts));

            assertFalse(bid.isNextBidValid(new BidImpl(10, 7), false, setts));
            
            assertEquals(bid.nextBid(), new BidImpl(5, 5));
            assertEquals(bid.nextBid(6), new BidImpl(4, 6));
            assertEquals(bid.nextBid(4), new BidImpl(5, 4));
            assertEquals(bid.nextBid(1), new BidImpl(2, 1));
            bid = new BidImpl(5, 5);
            assertEquals(bid.nextBid(1), new BidImpl(3, 1));
            bid = new BidImpl(3, 1);
            assertEquals(bid.nextBid(), new BidImpl(4, 1));
            assertEquals(bid.nextBid(2), new BidImpl(7, 2));
        } catch (ErrorTypeException e) {
            throw new IllegalStateException("Should be ok");
        }
    }
}

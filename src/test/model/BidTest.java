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
        GameSettings setts = new GameSettingsImpl(5, 6, 5, Duration.ofMinutes(2), "");
        try {
            bid = new BidImpl(0, 1);
            throw new IllegalStateException("Under limit");
        } catch (IllegalArgumentException ex) {
            //ok
        }
        try {
            bid = new BidImpl(1, 0);
            throw new IllegalStateException("Under limit");
        } catch (IllegalArgumentException ex) {
           //ok
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

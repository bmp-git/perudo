package test.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

import perudo.model.PlayerStatus;
import perudo.model.impl.PlayerStatusImpl;
import perudo.utility.ErrorType;
import perudo.utility.ErrorTypeException;

/**
 * Test for PlayerStatus implementation.
 */
public class PlayerStatusTest {
    /**
     * The main test.
     */
    @org.junit.Test
    public void main() {
        // CHECKSTYLE:OFF: checkstyle:magicnumber
        PlayerStatus s;

        try {
            s = new PlayerStatusImpl(-1, 6, false);
            throw new IllegalStateException("remaining dice can't be -1");
        } catch (IllegalArgumentException ex) {
            assertEquals(ex.getClass(), IllegalArgumentException.class);
        }

        try {
            s = new PlayerStatusImpl(0, 1, false);
            throw new IllegalStateException("dice faces can't be 1 or less");
        } catch (IllegalArgumentException ex) {
            assertEquals(ex.getClass(), IllegalArgumentException.class);
        }

        try {
            s = new PlayerStatusImpl(1, 6, false);
        } catch (Exception ex) {
            throw new IllegalStateException("should be ok");
        }

        assertEquals(s.getRemainingDice(), 1);
        assertEquals(s.getMaxDiceValue(), 6);
        assertFalse(s.hasCalledPalifico());
        try {
            final PlayerStatus s1 = s.callPalifico();
            assertNotEquals(s1, s);
            assertFalse(s.hasCalledPalifico());
            assertTrue(s1.hasCalledPalifico());
        } catch (Exception ex) {
            throw new IllegalStateException("should be ok");
        }

        try {
            s.callPalifico();
        } catch (ErrorTypeException ex) {
            assertEquals(ex.getErrorType(), ErrorType.GAME_PALIFICO_ALREADY_USED);
        }

        s = new PlayerStatusImpl(5, 6, false);
        try {
            s.callPalifico();
        } catch (ErrorTypeException ex) {
            assertEquals(ex.getErrorType(), ErrorType.GAME_CANT_CALL_PALIFICO_NOW);
        }

        s = s.setRemainingDice(0);
        try {
            s = s.setRemainingDice(-1);
            throw new IllegalStateException("remaining dice can't be -1");
        } catch (Exception ex) {
            assertEquals(ex.getClass(), IllegalArgumentException.class);
        }
        assertEquals(s.getRemainingDice(), 0);

        s = s.setRemainingDice(5);
        assertEquals(s.getDiceCount().entrySet().stream().mapToInt(p -> p.getValue()).sum(), 5);
        assertEquals(s.getDiceCount().size(), 6);
        // CHECKSTYLE:ON: checkstyle:magicnumber
    }
}

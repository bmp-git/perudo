package test.model;

import static org.junit.Assert.*;

import perudo.model.PlayerStatus;
import perudo.model.impl.PlayerStatusImpl;
import perudo.utility.ErrorType;
import perudo.utility.ErrorTypeException;

public class PlayerStatusTest {
    @org.junit.Test
    public void main() {
        PlayerStatus s;

        try {
            s = new PlayerStatusImpl(-1, 6, false);
            throw new IllegalStateException("remaining dice can't be -1");
        } catch (IllegalArgumentException ex) {

        }

        try {
            s = new PlayerStatusImpl(0, 0, false);
            throw new IllegalStateException("dice faces can't be 0");
        } catch (IllegalArgumentException ex) {

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
            PlayerStatus s1 = s.callPalifico();
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
            
        }
        assertEquals(s.getRemainingDice(), 0);

        s = s.setRemainingDice(5);
        assertEquals(s.getDiceCount().entrySet().stream().mapToInt(p->p.getValue()).sum(), 5);
        assertEquals(s.getDiceCount().size(), 6);
    }
}

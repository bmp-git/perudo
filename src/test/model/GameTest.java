package test.model;

import perudo.model.Game;
import perudo.model.GameSettings;
import perudo.model.Lobby;
import perudo.model.User;
import perudo.model.impl.BidImpl;
import perudo.model.impl.GameSettingsImpl;
import perudo.model.impl.LobbyImpl;
import perudo.model.impl.UserImpl;
import perudo.utility.ErrorType;
import perudo.utility.ErrorTypeException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.util.Arrays;

/**
 * Test for Game implementation.
 */
public class GameTest {
    /**
     * The main test.
     */
    @org.junit.Test
    public void main() {
        User u1 = null, u2 = null, u3 = null;
        try {
            u1 = UserImpl.createPlayer("u1");
            u2 = UserImpl.createPlayer("u2");
            u3 = UserImpl.createPlayer("u3");
        } catch (ErrorTypeException e2) {
            throw new IllegalStateException("Should be ok, 1");
        }
        // CHECKSTYLE:OFF: checkstyle:magicnumber
        final GameSettings setts = new GameSettingsImpl(3, 6, 5, Duration.ofMinutes(10), "");
        Game game = null;
        try {
            final Lobby l = new LobbyImpl(setts, u1);
            l.addUser(u2);
            l.addUser(u3);
            game = l.startGame(u1);
        } catch (Exception e1) {
            throw new IllegalStateException("Should be ok, 2");
        }

        assertTrue(game.getTurnRemainingTime().getSeconds() <= 600);
        assertTrue(game.getTurnRemainingTime().getSeconds() >= 595);

        assertFalse(game.getCurrentBid().isPresent());
        assertEquals(game.getSettings(), setts);

        assertTrue(game.getUsers().containsAll(Arrays.asList(u1, u2, u3)));

        assertTrue(Arrays.asList(u1, u2, u3).contains(game.getTurn()));

        assertEquals(game.getUserStatus(u1).getMaxDiceValue(), 6);
        assertEquals(game.getUserStatus(u1).getRemainingDice(), 5);

        assertFalse(game.isOver());
        assertFalse(game.turnIsPalifico());

        try {
            game.doubt(game.getTurn());
            throw new IllegalStateException("Can't doubt");
        } catch (ErrorTypeException e) {
            assertEquals(e.getErrorType(), ErrorType.GAME_CANT_DOUBT_NOW);
        }

        try {
            game.play(new BidImpl(2, 7), game.getTurn());
            throw new IllegalStateException("Invalid bid");
        } catch (ErrorTypeException e) {
            assertEquals(e.getErrorType(), ErrorType.GAME_INVALID_BID);
        }

        try {
            game.play(new BidImpl(0, 1), game.getTurn());
            throw new IllegalStateException("Invalid bid");
        } catch (ErrorTypeException e) {
            assertEquals(e.getErrorType(), ErrorType.GAME_INVALID_BID);
        }
        User ut = game.getTurn();
        try {
            game.play(new BidImpl(2, 2), ut);
        } catch (Exception e1) {
            throw new IllegalStateException("Should be ok, 3");
        }

        try {
            game.urge(game.getTurn());
            throw new IllegalStateException("Can't call urge");
        } catch (ErrorTypeException e) {
            assertEquals(e.getErrorType(), ErrorType.GAME_CANT_CALL_URGE_NOW);
        }

        try {
            game.play(new BidImpl(20, 3), game.getTurn());
        } catch (Exception e1) {
            throw new IllegalStateException("Should be ok, 4");
        }

        try {
            game.urge(ut);
        } catch (Exception e1) {
            throw new IllegalStateException("Should be ok, 5");
        }

        assertEquals(game.getUserStatus(ut).getRemainingDice(), 4);

        ut = game.getTurn();
        int utDice = game.getUserStatus(ut).getRemainingDice();
        try {
            game.play(new BidImpl(20, 3), game.getTurn());
            game.doubt(game.getTurn());
            utDice--;
        } catch (Exception e1) {
            throw new IllegalStateException("Should be ok, 6");
        }
        assertEquals(game.getUserStatus(ut).getRemainingDice(), utDice);
        // CHECKSTYLE:ON: checkstyle:magicnumber
    }
}

package test.model;

import static org.junit.Assert.*;

import java.time.Duration;
import java.util.Arrays;

import perudo.model.Lobby;
import perudo.model.User;
import perudo.model.impl.GameSettingsImpl;
import perudo.model.impl.LobbyImpl;
import perudo.model.impl.UserImpl;
import perudo.utility.ErrorType;
import perudo.utility.ErrorTypeException;

public class LobbyTest {
    @org.junit.Test
    public void main() {
        User owner = new UserImpl("host");
        Lobby l, l1;
        try {
            l = new LobbyImpl(new GameSettingsImpl(4, 6, 5, Duration.ofMinutes(1)), owner);
            l1 = new LobbyImpl(new GameSettingsImpl(4, 6, 5, Duration.ofMinutes(1)), owner);
        } catch (Exception ex) {
            throw new IllegalStateException("Should be ok");
        }
        assertNotEquals(l.getId(), l1.getId());
        assertEquals(l.getFreeSpace(), 3);
        assertEquals(owner, l.getOwner());
        assertTrue(l.getUsers().contains(owner));
        assertEquals(l.getInfo().getMaxPlayer(), 4);
        assertEquals(l.getInfo().getMaxDiceValue(), 6);
        assertEquals(l.getInfo().getInitialDiceNumber(), 5);
        assertEquals(l.getInfo().getMaxTurnTime(), Duration.ofMinutes(1));
        try {
            l.removeUser(owner);
        } catch (Exception ex) {
            throw new IllegalStateException("Should be ok");
        }
        assertEquals(l.getFreeSpace(), 4);
        assertTrue(l.getOwner() == null);
        try {
            l.addUser(owner);
        } catch (Exception ex) {
            throw new IllegalStateException("Should be ok");
        }
        assertEquals(owner, l.getOwner());
        assertEquals(l.getFreeSpace(), 3);

        try {
            l.startGame(owner);
            throw new IllegalStateException("At least 2 players");
        } catch (ErrorTypeException ex) {
            assertEquals(ex.getErrorType(), ErrorType.LOBBY_CANT_START_GAME);
        }

        User u1 = new UserImpl("u1");
        User u2 = new UserImpl("u2");
        User u3 = new UserImpl("u3");
        User u4 = new UserImpl("u4");

        try {
            l.removeUser(u1);
            throw new IllegalStateException("Can't remove if not present");
        } catch (ErrorTypeException ex) {
            assertEquals(ex.getErrorType(), ErrorType.LOBBY_USER_NOT_PRESENT);
        }

        try {
            l.addUser(owner);
            throw new IllegalStateException("Can't add if already present");
        } catch (ErrorTypeException ex) {
            assertEquals(ex.getErrorType(), ErrorType.LOBBY_ALREADY_JOINED);
        }
        try {
            l.addUser(u1);
        } catch (Exception ex) {
            throw new IllegalStateException("Should be ok");
        }
        try {
            l.startGame(u1);
            throw new IllegalStateException("Can't start lobby if you are not the owner");
        } catch (ErrorTypeException ex) {
            assertEquals(ex.getErrorType(), ErrorType.LOBBY_USER_NOT_OWNER);
        }
        try {
            l.addUser(u2);
        } catch (Exception ex) {
            throw new IllegalStateException("Should be ok");
        }

        try {
            l.addUser(u3);
        } catch (Exception ex) {
            throw new IllegalStateException("Should be ok");
        }
        try {
            l.addUser(u4);
            throw new IllegalStateException("Lobby should be full");
        } catch (ErrorTypeException ex) {
            assertEquals(ex.getErrorType(), ErrorType.LOBBY_IS_FULL);
        }
        try {
            l.removeUser(u3);
        } catch (Exception ex) {
            throw new IllegalStateException("Should be ok");
        }

        assertTrue(l.getUsers().containsAll(Arrays.asList(owner, u1, u2)));
        assertFalse(l.getUsers().contains(u3));
        assertFalse(l.getUsers().contains(u4));

        try {
            l.startGame(owner);
        } catch (Exception ex) {
            throw new IllegalStateException("Should be ok");
        }

        try {
            l.addUser(u4);
        } catch (ErrorTypeException ex) {
            assertEquals(ex.getErrorType(), ErrorType.LOBBY_ALREADY_STARTED);
        }
        try {
            l.removeUser(owner);
        } catch (ErrorTypeException ex) {
            assertEquals(ex.getErrorType(), ErrorType.LOBBY_ALREADY_STARTED);
        }
        try {
            l.startGame(owner);
        } catch (ErrorTypeException ex) {
            assertEquals(ex.getErrorType(), ErrorType.LOBBY_ALREADY_STARTED);
        }
    }
}

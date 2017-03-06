package test.model;

import perudo.model.Game;
import perudo.model.Lobby;
import perudo.model.Model;
import perudo.model.User;
import perudo.model.impl.GameSettingsImpl;
import perudo.model.impl.LobbyImpl;
import perudo.model.impl.ModelImpl;
import perudo.model.impl.UserImpl;
import perudo.utility.ErrorType;
import perudo.utility.ErrorTypeException;

import static org.junit.Assert.*;

import java.time.Duration;
import java.util.Arrays;

public class ModelTest {
    @org.junit.Test
    public void main() {
        Model model = new ModelImpl();
        User u1 = null, u2 = null, u3 = null, u4 = null, u5 = null, u6 = null;
        try {
            u1 = UserImpl.createPlayer("u1");
            u2 = UserImpl.createPlayer("u2");
            u3 = UserImpl.createPlayer("u3");
            u4 = UserImpl.createPlayer("u4");
            u5 = UserImpl.createPlayer("u5");
            u6 = UserImpl.createPlayer("u6");
        } catch (ErrorTypeException e2) {

        }
        Lobby l1 = null, l2 = null, l3 = null;
        Game g1 = null, g2 = null, g3 = null;
        try {
            l1 = new LobbyImpl(new GameSettingsImpl(4, 6, 5, Duration.ofMinutes(1), ""), u1);
            l2 = new LobbyImpl(new GameSettingsImpl(4, 6, 5, Duration.ofMinutes(1), ""), u2);
            l3 = new LobbyImpl(new GameSettingsImpl(4, 6, 5, Duration.ofMinutes(1), ""), u3);
            l1.addUser(u4);
            l2.addUser(u5);
            l3.addUser(u6);
            g1 = l1.startGame(u1);
            g2 = l2.startGame(u2);
            g3 = l3.startGame(u3);
        } catch (Exception e1) {
            throw new IllegalStateException("Should be ok");
        }

        // USERS
        try {
            model.removeUser(u1);
            throw new IllegalStateException("Can't remove");
        } catch (ErrorTypeException e) {
            assertEquals(e.getErrorType(), ErrorType.USER_DOES_NOT_EXISTS);
        }

        try {
            model.addUser(u1);
        } catch (Exception e) {
            throw new IllegalStateException("Should be ok");
        }

        try {
            model.addUser(u1);
            throw new IllegalStateException("Can't add twice");
        } catch (ErrorTypeException e) {
            assertEquals(e.getErrorType(), ErrorType.USER_ALREADY_EXISTS);
        }

        try {
            model.removeUser(u1);
            model.addUser(u1);
            model.addUser(u2);
            model.addUser(u3);
        } catch (Exception e) {
            throw new IllegalStateException("Should be ok");
        }

        assertTrue(model.getUsers().containsAll(Arrays.asList(u1, u2, u3)));

        // LOBBIES
        try {
            model.removeLobby(l1);
            throw new IllegalStateException("Can't remove");
        } catch (ErrorTypeException e) {
            assertEquals(e.getErrorType(), ErrorType.LOBBY_NOT_EXISTS);
        }

        try {
            model.addLobby(l1);
        } catch (Exception e) {
            throw new IllegalStateException("Should be ok");
        }

        try {
            model.addLobby(l1);
            throw new IllegalStateException("Can't add twice");
        } catch (ErrorTypeException e) {
            assertEquals(e.getErrorType(), ErrorType.LOBBY_ALREADY_EXISTS);
        }

        try {
            model.removeLobby(l1);
            model.addLobby(l1);
            model.addLobby(l2);
            model.addLobby(l3);
        } catch (Exception e) {
            throw new IllegalStateException("Should be ok");
        }

        assertTrue(model.getLobbies().containsAll(Arrays.asList(l1, l2, l3)));

        // GAMES
        try {
            model.removeGame(g1);
            throw new IllegalStateException("Can't remove");
        } catch (ErrorTypeException e) {
            assertEquals(e.getErrorType(), ErrorType.GAME_NOT_EXISTS);
        }

        try {
            model.addGame(g1);
        } catch (Exception e) {
            throw new IllegalStateException("Should be ok");
        }

        try {
            model.addGame(g1);
            throw new IllegalStateException("Can't add twice");
        } catch (ErrorTypeException e) {
            assertEquals(e.getErrorType(), ErrorType.GAME_ALREADY_EXISTS);
        }

        try {
            model.removeGame(g1);
            model.addGame(g1);
            model.addGame(g2);
            model.addGame(g3);
        } catch (Exception e) {
            throw new IllegalStateException("Should be ok");
        }

        assertTrue(model.getGames().containsAll(Arrays.asList(g1, g2, g3)));
    }
}

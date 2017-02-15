package perudo.model.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import perudo.utility.ErrorType;
import perudo.utility.ErrorTypeException;
import perudo.model.Game;
import perudo.model.Lobby;
import perudo.model.Model;
import perudo.model.User;

public class ModelImpl implements Model {

    private final Set<User> users;
    private final Set<Game> games;
    private final Set<Lobby> lobbies;

    public ModelImpl() {
        this.users = new HashSet<>();
        this.games = new HashSet<>();
        this.lobbies = new HashSet<>();
    }

    @Override
    public Set<User> getUsers() {
        return Collections.unmodifiableSet(this.users);
    }

    @Override
    public void addUser(final User user) throws ErrorTypeException {
        if (!this.users.add(user)) {
            throw new ErrorTypeException(ErrorType.USER_ALREADY_EXISTS);
        }
    }

    @Override
    public void removeUser(final User user) throws ErrorTypeException {
        if (!this.users.remove(user)) {
            throw new ErrorTypeException(ErrorType.USER_DOES_NOT_EXISTS);
        }
    }

    @Override
    public Set<Game> getGames() {
        return Collections.unmodifiableSet(this.games);
    }

    @Override
    public void addGame(final Game game) throws ErrorTypeException {
        if (!this.games.add(game)) {
            throw new ErrorTypeException(ErrorType.GAME_ALREADY_EXISTS);
        }
    }

    @Override
    public void removeGame(final Game game) throws ErrorTypeException {
        if (!this.games.remove(game)) {
            throw new ErrorTypeException(ErrorType.GAME_NOT_EXISTS);
        }
    }

    @Override
    public Set<Lobby> getLobbies() {
        return Collections.unmodifiableSet(this.lobbies);
    }

    @Override
    public void addLobby(final Lobby lobby) throws ErrorTypeException {
        if (!this.lobbies.add(lobby)) {
            throw new ErrorTypeException(ErrorType.LOBBY_ALREADY_EXISTS);
        }
    }

    @Override
    public void removeLobby(final Lobby lobby) throws ErrorTypeException {
        if (!this.lobbies.remove(lobby)) {
            throw new ErrorTypeException(ErrorType.LOBBY_NOT_EXISTS);
        }
    }

}

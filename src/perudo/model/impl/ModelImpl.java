package perudo.model.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import perudo.utility.ErrorType;
import perudo.model.Game;
import perudo.model.Lobby;
import perudo.model.Model;
import perudo.model.User;
import perudo.utility.Result;
import perudo.utility.impl.ResultImpl;

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
    public Result addUser(final User user) {
        if (!this.users.add(user)) {
            return ResultImpl.error(ErrorType.USER_ALREADY_EXISTS);
        }
        return ResultImpl.ok();
    }

    @Override
    public Result removeUser(final User user) {
        if (!this.users.remove(user)) {
            return ResultImpl.error(ErrorType.USER_DOES_NOT_EXISTS);
        }
        return ResultImpl.ok();
    }

    @Override
    public Set<Game> getGames() {
        return Collections.unmodifiableSet(this.games);
    }

    @Override
    public Result addGame(final Game game) {
        if (!this.games.add(game)) {
            return ResultImpl.error(ErrorType.GAME_ALREADY_EXISTS);
        }
        return ResultImpl.ok();
    }

    @Override
    public Result removeGame(final Game game) {
        if (!this.games.remove(game)) {
            return ResultImpl.error(ErrorType.GAME_NOT_EXISTS);
        }
        return ResultImpl.ok();
    }

    @Override
    public Set<Lobby> getLobbies() {
        return Collections.unmodifiableSet(this.lobbies);
    }

    @Override
    public Result addLobby(final Lobby lobby) {
        if (!this.lobbies.add(lobby)) {
            return ResultImpl.error(ErrorType.LOBBY_ALREADY_EXISTS);
        }
        return ResultImpl.ok();
    }

    @Override
    public Result removeLobby(final Lobby lobby) {
        if (!this.lobbies.remove(lobby)) {
            return ResultImpl.error(ErrorType.LOBBY_NOT_EXISTS);
        }
        return ResultImpl.ok();
    }

}

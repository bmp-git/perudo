package perudo.model.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import perudo.model.Game;
import perudo.model.GameSettings;
import perudo.model.Lobby;
import perudo.model.User;
import perudo.utility.ErrorType;
import perudo.utility.IdDispenser;
import perudo.utility.Response;
import perudo.utility.Result;
import perudo.utility.impl.ResponseImpl;
import perudo.utility.impl.ResultImpl;

public class LobbyImpl implements Lobby {

    private final int id;
    private final Set<User> userSet;
    private final GameSettings settings;
    private User owner;

    public LobbyImpl(final GameSettings settings, final User owner) {
        if (settings == null || owner == null) {
            throw new IllegalArgumentException();
        }

        this.id = IdDispenser.getLobbyIdDispenser().getNextId();
        this.userSet = new HashSet<>();
        this.owner = owner;
        this.settings = settings;

        this.addUser(owner);
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public Set<User> getUsers() {
        return Collections.unmodifiableSet(userSet);
    }

    @Override
    public Result addUser(final User user) {
        if (this.getFreeSpace() <= 0) {
            return ResultImpl.error(ErrorType.LOBBY_IS_FULL);
        }
        if (!this.userSet.add(user)) {
            return ResultImpl.error(ErrorType.USER_ALREADY_EXISTS);
        }
        if (this.getOwner() == null) {
            this.owner = user;
        }
        return ResultImpl.ok();
    }

    @Override
    public Result removeUser(final User user) {
        if (!this.userSet.remove(user)) {
            return ResultImpl.error(ErrorType.USER_DOES_NOT_EXISTS);
        }
        if (this.getOwner().equals(user)) {
            this.owner = this.userSet.size() == 0 ? null : this.userSet.stream().findAny().get();
        }
        return ResultImpl.ok();
    }

    @Override
    public Response<Game> startGame(final User starter) {
        if (this.getUsers().size() < 2) {
            return ResponseImpl.empty(ErrorType.LOBBY_CANT_START_GAME);
        }
        if (!this.getOwner().equals(starter)) {
            return ResponseImpl.empty(ErrorType.USER_DOES_NOT_OWN_LOBBY);
        }
        return ResponseImpl.of(new GameImpl(this.getInfo(), this.getUsers()));
    }

    @Override
    public GameSettings getInfo() {
        return this.settings;
    }

    @Override
    public User getOwner() {
        return this.owner;
    }

}

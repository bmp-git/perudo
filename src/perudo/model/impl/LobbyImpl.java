package perudo.model.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import perudo.model.Game;
import perudo.model.GameSettings;
import perudo.model.Lobby;
import perudo.model.User;
import perudo.utility.ErrorType;
import perudo.utility.ErrorTypeException;
import perudo.utility.IdDispenser;

public class LobbyImpl implements Lobby {

    private final int id;
    private final Set<User> userSet;
    private final GameSettings settings;
    private User owner;

    public LobbyImpl(final GameSettings settings, final User owner) throws ErrorTypeException{
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
    public void addUser(final User user) throws ErrorTypeException{
        if (this.getFreeSpace() <= 0) {
            throw new ErrorTypeException(ErrorType.LOBBY_IS_FULL);
        }
        if (!this.userSet.add(user)) {
            throw new ErrorTypeException(ErrorType.USER_ALREADY_EXISTS);
        }
        if (this.getOwner() == null) {
            this.owner = user;
        }
    }

    @Override
    public void removeUser(final User user) throws ErrorTypeException{
        if (!this.userSet.remove(user)) {
            throw new ErrorTypeException(ErrorType.USER_DOES_NOT_EXISTS);
        }
        if (this.getOwner().equals(user)) {
            this.owner = this.userSet.size() == 0 ? null : this.userSet.stream().findAny().get();
        }
    }

    @Override
    public Game startGame(final User starter) throws ErrorTypeException{
        if (this.getUsers().size() < 2) {
            throw new ErrorTypeException(ErrorType.LOBBY_CANT_START_GAME);
        }
        if (!this.getOwner().equals(starter)) {
            throw new ErrorTypeException(ErrorType.USER_DOES_NOT_OWN_LOBBY);
        }
        return new GameImpl(this.getInfo(), this.getUsers());
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

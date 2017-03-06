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

/**
 * The implementation of Lobby interface.
 */
public class LobbyImpl implements Lobby {

    private static final long serialVersionUID = -2649977195965292303L;

    private final int id;
    private final Set<User> userSet;
    private final GameSettings settings;
    private User owner;
    private boolean started;

    /**
     * Create a lobby from settings.
     * 
     * @param settings
     *            the settings for this lobby
     * 
     * @param owner
     *            the creator of this lobby, notice that this user will be added
     *            to this lobby after the creation
     */
    public LobbyImpl(final GameSettings settings, final User owner) {
        if (settings == null || owner == null) {
            throw new IllegalArgumentException();
        }
        this.started = false;
        this.id = IdDispenser.getLobbyIdDispenser().getNextId();
        this.userSet = new HashSet<>();
        this.owner = owner;
        this.settings = settings;

        try {
            this.addUser(owner);
        } catch (ErrorTypeException e) {
            e.printStackTrace();
            // can't be possible
            throw new IllegalStateException();
        }
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
    public synchronized void addUser(final User user) throws ErrorTypeException {
        if (started) {
            throw new ErrorTypeException(ErrorType.LOBBY_ALREADY_STARTED);
        }
        if (this.getFreeSpace() <= 0) {
            throw new ErrorTypeException(ErrorType.LOBBY_IS_FULL);
        }
        if (!this.userSet.add(user)) {
            throw new ErrorTypeException(ErrorType.LOBBY_ALREADY_JOINED);
        }
        if (this.getOwner() == null) {
            this.owner = user;
        }
    }

    @Override
    public synchronized void removeUser(final User user) throws ErrorTypeException {
        if (started) {
            throw new ErrorTypeException(ErrorType.LOBBY_ALREADY_STARTED);
        }
        if (!this.userSet.remove(user)) {
            throw new ErrorTypeException(ErrorType.LOBBY_USER_NOT_PRESENT);
        }
        if (this.getOwner().equals(user)) {
            this.owner = this.userSet.size() == 0 ? null : this.userSet.stream().findAny().get();
        }
    }

    @Override
    public synchronized Game startGame(final User starter) throws ErrorTypeException {
        if (started) {
            throw new ErrorTypeException(ErrorType.LOBBY_ALREADY_STARTED);
        }
        if (this.getUsers().size() < 2) {
            throw new ErrorTypeException(ErrorType.LOBBY_CANT_START_GAME);
        }
        if (!this.getOwner().equals(starter)) {
            throw new ErrorTypeException(ErrorType.LOBBY_USER_NOT_OWNER);
        }

        Game game = new GameImpl(this.getInfo(), this.getUsers());
        this.started = true;
        return game;
    }

    @Override
    public GameSettings getInfo() {
        return this.settings;
    }

    @Override
    public synchronized User getOwner() {
        return this.owner;
    }

    @Override
    public synchronized int getFreeSpace() {
        return getInfo().getMaxPlayer() - getUsers().size();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((settings == null) ? 0 : settings.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        LobbyImpl other = (LobbyImpl) obj;
        if (id != other.id) {
            return false;
        }
        if (settings == null) {
            if (other.settings != null) {
                return false;
            }
        } else if (!settings.equals(other.settings)) {
            return false;
        }
        return true;
    }

}

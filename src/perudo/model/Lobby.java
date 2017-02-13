package perudo.model;

import java.util.Set;
import perudo.utility.ErrorTypeException;

public interface Lobby {
    int getId();

    Set<User> getUsers();

    void addUser(User user) throws ErrorTypeException;

    void removeUser(User user) throws ErrorTypeException;

    Game startGame(User starter) throws ErrorTypeException;

    GameSettings getInfo();

    User getOwner();

    // support methods
    default int getFreeSpace() {
        return getInfo().getMaxPlayer() - getUsers().size();
    }
}

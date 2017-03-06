package perudo.model;

import java.io.Serializable;
import java.util.Set;
import perudo.utility.ErrorTypeException;

/**
 * Represent a lobby.
 */
public interface Lobby extends Serializable {
    /**
     * Gets the unique id of the lobby.
     * 
     * @return id value
     */
    int getId();

    /**
     * Gets the set of users in the lobby.
     * 
     * @return the set of users
     */
    Set<User> getUsers();

    /**
     * Adds the user to the lobby.
     * 
     * @throws ErrorTypeException
     *             if an error occurs during invocation
     * 
     * @param user
     *            the user to add
     */
    void addUser(User user) throws ErrorTypeException;

    /**
     * Removes the user to the lobby.
     * 
     * @throws ErrorTypeException
     *             if an error occurs during invocation
     * 
     * @param user
     *            the user to remove
     */
    void removeUser(User user) throws ErrorTypeException;

    /**
     * Starts the lobby.
     * 
     * @param user
     *            the user who start the lobby (must be the owner or an
     *            ErrorTypeException will be thrown)
     * 
     * @throws ErrorTypeException
     *             if an error occurs during invocation
     * 
     * @return return the started game
     */
    Game startGame(User user) throws ErrorTypeException;

    /**
     * Gets the settings for this lobby.
     * 
     * @return the settings
     */
    GameSettings getInfo();

    /**
     * Gets the owner of this lobby.
     * 
     * @return the user owner
     */
    User getOwner();

    /**
     * Gets the remaining seats for this lobby.
     * 
     * @return the free space
     */
    int getFreeSpace();
}

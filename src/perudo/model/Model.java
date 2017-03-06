package perudo.model;

import java.util.Set;
import perudo.utility.ErrorTypeException;

/**
 * Manage all components of the application model.
 */
public interface Model {

    /**
     * Get existing users.
     * 
     * @return the set of users
     */
    Set<User> getUsers();

    /**
     * Adds a new user in the set.
     * 
     * @param user
     *            the user to add
     * 
     * @throws ErrorTypeException
     *             if an error occurs during invocation
     */
    void addUser(User user) throws ErrorTypeException;

    /**
     * Removes an existing user from the set.
     * 
     * @param user
     *            the user to remove
     * 
     * @throws ErrorTypeException
     *             if an error occurs during invocation
     * 
     */
    void removeUser(User user) throws ErrorTypeException;

    /**
     * Get existing games.
     * 
     * @return the set of users
     */
    Set<Game> getGames();

    /**
     * Adds a new game in the set.
     * 
     * @param game
     *            the game to add
     * 
     * @throws ErrorTypeException
     *             if an error occurs during invocation
     * 
     */
    void addGame(Game game) throws ErrorTypeException;

    /**
     * Removes an existing game from the set.
     * 
     * @param game
     *            the game to remove
     * 
     * @throws ErrorTypeException
     *             if an error occurs during invocation
     * 
     */
    void removeGame(Game game) throws ErrorTypeException;

    /**
     * Get existing lobbies.
     * 
     * @return the set of lobbies
     */
    Set<Lobby> getLobbies();

    /**
     * Adds a new lobby in the set.
     * 
     * @param lobby
     *            the lobby to add
     * 
     * @throws ErrorTypeException
     *             if an error occurs during invocation
     */
    void addLobby(Lobby lobby) throws ErrorTypeException;

    /**
     * Removes an existing lobby from the set.
     * 
     * @param lobby
     *            the lobby to remove
     * 
     * @throws ErrorTypeException
     *             if an error occurs during invocation
     * 
     */
    void removeLobby(Lobby lobby) throws ErrorTypeException;
}

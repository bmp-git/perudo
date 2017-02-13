package perudo.model;

import java.util.Set;
import perudo.utility.ErrorTypeException;

public interface Model {

    // User methods
    /**
     * Get existing users.
     * 
     * @return the set of users
     */
    Set<User> getUsers();

    /**
     * Adds a new user in the set.
     * 
     * @return the result value
     */
    void addUser(User user) throws ErrorTypeException;

    /**
     * Removes an existing user from the set.
     * 
     * @return the result value
     */
    void removeUser(User user) throws ErrorTypeException;

    // Game methods
    /**
     * Get existing games.
     * 
     * @return the set of users
     */
    Set<Game> getGames();
    /**
     * Adds a new game in the set.
     * 
     * @return the result value
     */
    void addGame(Game game) throws ErrorTypeException;
    /**
     * Removes an existing game from the set.
     * 
     * @return the result value
     */
    void removeGame(Game game) throws ErrorTypeException;

    // Lobby methods
    /**
     * Get existing lobbies.
     * 
     * @return the set of lobbies
     */
    Set<Lobby> getLobbies();

    /**
     * Adds a new lobby in the set.
     * 
     * @return the result value
     */
    void addLobby(Lobby lobby) throws ErrorTypeException;

    /**
     * Removes an existing lobby from the set.
     * 
     * @return the result value
     */
    void removeLobby(Lobby lobby) throws ErrorTypeException;
}

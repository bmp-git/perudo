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
     */
    void addUser(User user) throws ErrorTypeException;

    /**
     * Removes an existing user from the set.
     * 
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
     */
    void addGame(Game game) throws ErrorTypeException;

    /**
     * Removes an existing game from the set.
     * 
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
     */
    void addLobby(Lobby lobby) throws ErrorTypeException;

    /**
     * Removes an existing lobby from the set.
     * 
     */
    void removeLobby(Lobby lobby) throws ErrorTypeException;
}

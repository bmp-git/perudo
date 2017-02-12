package perudo.model;

import java.util.Set;

import perudo.utility.Result;

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
    Result addUser(User user);

    /**
     * Removes an existing user from the set.
     * 
     * @return the result value
     */
    Result removeUser(User user);

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
    Result addGame(Game game);
    /**
     * Removes an existing game from the set.
     * 
     * @return the result value
     */
    Result removeGame(Game game);

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
    Result addLobby(Lobby lobby);

    /**
     * Removes an existing lobby from the set.
     * 
     * @return the result value
     */
    Result removeLobby(Lobby lobby);
}

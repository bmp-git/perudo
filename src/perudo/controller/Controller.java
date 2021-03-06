package perudo.controller;

import java.io.Closeable;

import perudo.model.Bid;
import perudo.model.Lobby;
import perudo.model.GameSettings;
import perudo.model.User;
import perudo.model.UserType;
import perudo.view.View;

/**
 * 
 * Defines a controller that can operate between a model and multiple views.
 *
 */
public interface Controller extends Closeable {
    /**
     * Initializes a new user.
     * 
     * @param view
     *            the view where the response will be given
     */
    void initializeNewUser(View view);

    /**
     * Changes username with a new one.
     * 
     * @param user
     *            user to change name
     * @param name
     *            new name
     */
    void changeUserName(User user, String name);

    /**
     * Asks for the online users list.
     * 
     * @param user
     *            user requesting
     */
    void getUsers(User user);

    // Possibile user interacts with lobbies
    /**
     * Creates a new lobby.
     * 
     * @param user
     *            user owner
     * @param info
     *            lobby information
     */
    void createLobby(User user, GameSettings info);

    /**
     * Asks for opened lobbies.
     * 
     * @param user
     *            user requesting
     */
    void getLobbies(User user);

    /**
     * Joins a lobby.
     * 
     * @param user
     *            user requesting
     * @param lobby
     *            lobby to join
     */
    void joinLobby(User user, Lobby lobby);

    /**
     * The user adds a bot to a given lobby.
     * 
     * @param user
     *            user requesting to add a bot
     * @param lobby
     *            in which lobby add the bot
     * @param type
     *            strength of the bot (wrong type notifies an error)
     */
    void addBotToLobby(User user, Lobby lobby, UserType type);

    /**
     * Exits from a lobby.
     * 
     * @param user
     *            user requesting
     */
    void exitLobby(User user);

    /**
     * Starts a owned lobby.
     * 
     * @param user
     *            user requesting must be the owner
     */
    void startLobby(User user);

    // Possible user interacts when in game
    /**
     * Asks for started games.
     * 
     * @param user
     *            user requesting
     */
    void getGames(User user);

    /**
     * Makes a play, must be in-game.
     * 
     * @param user
     *            user requesting
     * @param bid
     *            the wanted bid
     */
    void play(User user, Bid bid);

    /**
     * Calls a doubt, must be in-game.
     * 
     * @param user
     *            user requesting
     */
    void doubt(User user);

    /**
     * Makes a urge, must be in-game.
     * 
     * @param user
     *            user requesting
     */
    void urge(User user);

    /**
     * Calls palifico for this turn, must be in-game.
     * 
     * @param user
     *            user requesting
     */
    void callPalifico(User user);

    /**
     * Leaves the current game, must be in-game.
     * 
     * @param user
     *            user requesting
     */
    void exitGame(User user);

    /**
     * User leaves the controller.
     * 
     * @param user
     *            user leaving
     */
    void close(User user);

    /**
     * User leaves the controller now, a notify isn't sent to the user and he
     * leaves his lobby or game.
     * 
     * @param user
     *            user leaving
     */
    void closeNow(User user);
}

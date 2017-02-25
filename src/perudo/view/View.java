package perudo.view;

import java.io.Closeable;
import java.util.Set;
import perudo.model.*;
import perudo.utility.ErrorType;
import perudo.utility.Response;

public interface View extends Closeable {

    /**
     * Tells the view that the new user has been initialized.
     * 
     * @param user
     *            the user initialized
     */
    void initializeNewUserRespond(Response<User> user);

    /**
     * Tells all views that a new user has been initialized.
     * 
     * @param user
     *            the user initialized
     */
    void initializeNewUserNotify(User user);

    /**
     * Tells all views that a user has exited.
     * 
     * @param user
     *            the user who has exited
     */
    void userExitNotify(User user);

    /**
     * Tells all views that the name of a user has changed.
     * 
     * @param oldUser
     *            the old user whose name has been changed
     * @param newUser
     *            the new user whose name has been changed
     */
    void changeNameNotify(User oldUser, User newUser);

    /**
     * Gives to view the set of users.
     * 
     * @param users
     *            the set of users to be given to view
     */
    void getUsersRespond(Response<Set<User>> users);

    /**
     * Tells all views that a new lobby has been created.
     * 
     * @param lobby
     *            the lobby created
     */
    void createLobbyNotify(Lobby lobby);

    /**
     * Tells all views that a lobby has been removed.
     * 
     * @param lobby
     *            the lobby removed
     */
    void removeLobbyNotify(Lobby lobby);

    /**
     * Gives to view the list of lobbies.
     * 
     * @param lobbies
     *            the list of lobbies to be given to view
     */
    void getLobbiesRespond(Response<Set<Lobby>> lobbies);

    /**
     * Tells all views that a user has joined the lobby.
     * 
     * @param lobby
     *            the lobby joined
     * @param user
     *            the user joining
     */
    void joinLobbyNotify(Lobby lobby, User user);

    /**
     * Tells all views that a user has left the lobby.
     * 
     * @param lobby
     *            the lobby exited
     * @param user
     *            the user exiting
     */
    void exitLobbyNotify(Lobby lobby, User user);

    /**
     * Tells all views that a lobby turned in a game.
     * 
     * @param lobby
     *            the lobby started
     * @param game
     *            the game began
     */
    void startLobbyNotify(Lobby lobby, Game game);

    /**
     * Tells all views that a game has been removed.
     * 
     * @param game
     *            the game removed
     */
    void removeGameNotify(Game game);

    /**
     * Gives to view the list of games.
     * 
     * @param games
     *            the list of games to be given to view
     */
    void getGamesRespond(Response<Set<Game>> games);

    /**
     * Tells all views that a user has made a play.
     * 
     * @param game
     *            the game with the bid made
     * @param user
     *            the user who has played
     */
    void playNotify(Game game, User user);

    /**
     * Tells all views that someone has made a doubt.
     * 
     * @param game
     *            the updated game
     * @param user
     *            the user who has doubted
     * @param win
     *            the doubt result
     */
    void doubtNotify(Game game, User user, boolean win);

    /**
     * Tells all views that someone has made a urge.
     * 
     * @param game
     *            the updated game
     * @param user
     *            the user who has urged
     * @param win
     *            the urge result
     */
    void urgeNotify(Game game, User user, boolean win);

    /**
     * Tells all views that a palifico turn has been called.
     * 
     * @param game
     *            the updated game
     * @param user
     *            the user who has called palifico
     */
    void callPalificoNotify(Game game, User user);

    /**
     * Tells all views that the user exited the game.
     * 
     * @param game
     *            the game exited
     * @param user
     *            the user who exited
     */
    void exitGameNotify(Game game, User user);

    /**
     * Tells all views that the game ended.
     * 
     * @param game
     *            the ended game
     */
    void gameEndedNotify(Game game);

    /**
     * Tells the view that an error occurred.
     * 
     * @param errorType
     *            the error type occurred
     */
    void showError(ErrorType errorType);

}

package perudo.view;

import java.util.Set;
import perudo.model.*;
import perudo.utility.ErrorType;
import perudo.utility.Response;
import perudo.utility.Result;

public interface View {

    /*** SETUP ***/
    /**
     * Tells the view that the new user has been initialized.
     * 
     * @param user
     *            the user initialized
     */
    void initializeNewUserRespond(Response<User> user);

    /**
     * Tells the view that the name of user changed.
     * 
     * @param user
     *            the user whose name has been changed
     */
    void changeNameRespond(Response<User> user);

    /**
     * Gives to view the list of users.
     * 
     * @param users
     *            the list of users to be given to view
     */
    void getUsersRespond(Response<Set<User>> users);

    /*** LOBBY ***/
    // response
    /**
     * Tells the view that a new lobby has been created.
     * 
     * @param lobby
     *            the lobby created
     */
    void createLobbyRespond(Response<Lobby> lobby);

    /**
     * Gives to view the list of lobbies.
     * 
     * @param lobbies
     *            the list of lobbies to be given to view
     */
    void getLobbiesRespond(Response<Set<Lobby>> lobbies);

    /**
     * Tells the view that a lobby has been joined.
     * 
     * @param lobby
     *            the lobby joined
     */
    void joinLobbyRespond(Response<Lobby> lobby);

    /**
     * Tells the view that the lobby has been exited.
     * 
     * @param response
     *            result of operation
     */
    void exitLobbyRespond(Result response);

    /**
     * Tells the view that the lobby has started.
     * 
     * @param response
     *            result of operation
     */
    void startLobbyRespond(Result response);

    /*** GAME ***/
    // response
    /**
     * Gives to view the list of games.
     * 
     * @param games
     *            the list of games to be given to view
     */
    void getGamesRespond(Response<Set<Game>> games);

    /**
     * Tells the view that the play has been made or an error occured.
     * 
     * @param response
     *            result of operation
     */
    void playRespond(Result response);

    /**
     * Tells the view that the doubt has been made or an error occured.
     * 
     * @param response
     *            result of operation
     */
    void doubtRespond(Result response);

    /**
     * Tells the view that the urge has been made or an error occured.
     * 
     * @param response
     *            result of operation
     */
    void urgeRespond(Result response);

    /**
     * Tells the view that the palifico turn has been called or an error
     * occured.
     * 
     * @param user
     *            user calling
     */
    void callPalificoRespond(User user);

    /**
     * Tells the view that the game has been exited.
     * 
     * @param response
     *            result of operation
     */
    void exitGameRespond(Result response);

    // notify
    /**
     * Tells the view that a game has started.
     * 
     * @param game
     *            result of operation
     */
    void gameStarted(Game game);

    /**
     * Tells the view that a game changed.
     * 
     * @param game
     *            the changed game
     */
    void gameChanged(Game game);

    /**
     * Tells the view that a game ended.
     * 
     * @param game
     *            the ended game
     */
    void gameEnded(Game game);

    /**
     * Tells the view that an error occurred.
     * 
     * @param errorType
     *            the error type occurred
     */
    void showError(ErrorType errorType);

    /**
     * Tells the view that a lobby changed.
     * 
     * @param lobby
     *            the lobby changed
     */
    void lobbyChanged(Lobby lobby);

}

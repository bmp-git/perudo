package perudo.controller.net.client;

import java.io.IOException;
import java.util.Set;

import perudo.controller.net.DatagramStream;
import perudo.model.Game;
import perudo.model.Lobby;
import perudo.model.User;
import perudo.utility.ErrorType;
import perudo.utility.Response;
import perudo.view.View;

/**
 * A view that intercepts calls to a deeper view. This class is used to set the
 * correct user to the DatagramStream given.
 */
public class ViewClientImpl implements View {

    private final DatagramStream stream;
    private final View view;

    /**
     * Create a ViewClientImpl from another View and a DatagramStream.
     * 
     * @param view
     *            the final view
     * @param stream
     *            the stream to set the user
     */
    public ViewClientImpl(final View view, final DatagramStream stream) {
        this.view = view;
        this.stream = stream;
    }

    @Override
    public void initializeNewUserRespond(final Response<User> user) {
        if (user.isOk()) {
            this.stream.setUser(user.getValue());
        }
        this.view.initializeNewUserRespond(user);
    }

    @Override
    public void initializeNewUserNotify(final User user) {
        this.view.initializeNewUserNotify(user);
    }

    @Override
    public void userExitNotify(final User user) {
        this.view.userExitNotify(user);
    }

    @Override
    public void changeNameNotify(final User oldUser, final User newUser) {
        this.view.changeNameNotify(oldUser, newUser);
    }

    @Override
    public void getUsersRespond(final Response<Set<User>> users) {
        this.view.getUsersRespond(users);
    }

    @Override
    public void createLobbyNotify(final Lobby lobby) {
        this.view.createLobbyNotify(lobby);
    }

    @Override
    public void removeLobbyNotify(final Lobby lobby) {
        this.view.removeLobbyNotify(lobby);
    }

    @Override
    public void getLobbiesRespond(final Response<Set<Lobby>> lobbies) {
        this.view.getLobbiesRespond(lobbies);
    }

    @Override
    public void joinLobbyNotify(final Lobby lobby, final User user) {
        this.view.joinLobbyNotify(lobby, user);
    }

    @Override
    public void exitLobbyNotify(final Lobby lobby, final User user) {
        this.view.exitLobbyNotify(lobby, user);
    }

    @Override
    public void startLobbyNotify(final Lobby lobby, final Game game) {
        this.view.startLobbyNotify(lobby, game);
    }

    @Override
    public void removeGameNotify(final Game game) {
        this.view.removeGameNotify(game);
    }

    @Override
    public void getGamesRespond(final Response<Set<Game>> games) {
        this.view.getGamesRespond(games);
    }

    @Override
    public void playNotify(final Game game, final User user) {
        this.view.playNotify(game, user);
    }

    @Override
    public void doubtNotify(final Game game, final User user, final boolean win) {
        this.view.doubtNotify(game, user, win);
    }

    @Override
    public void urgeNotify(final Game game, final User user, final boolean win) {
        this.view.urgeNotify(game, user, win);
    }

    @Override
    public void callPalificoNotify(final Game game, final User user) {
        this.view.callPalificoNotify(game, user);
    }

    @Override
    public void exitGameNotify(final Game game, final User user) {
        this.view.exitGameNotify(game, user);
    }

    @Override
    public void gameEndedNotify(final Game game) {
        this.view.gameEndedNotify(game);
    }

    @Override
    public void showError(final ErrorType errorType) {
        this.view.showError(errorType);
    }

    @Override
    public void close() throws IOException {
        this.view.close();
        this.stream.close();
    }

}

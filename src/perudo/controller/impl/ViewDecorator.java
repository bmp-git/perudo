package perudo.controller.impl;

import java.io.IOException;
import java.util.Set;

import perudo.model.Game;
import perudo.model.Lobby;
import perudo.model.User;
import perudo.utility.ErrorType;
import perudo.utility.Response;
import perudo.view.View;

/**
 * A decorator of a View.
 */
public abstract class ViewDecorator implements View {

    private final View decoratedView;

    /**
     * Create a ViewDecorator.
     * 
     * @param view
     *            the view to decorate
     */
    public ViewDecorator(final View view) {
        this.decoratedView = view;
    }

    @Override
    public void initializeNewUserRespond(final Response<User> user) {
        this.decoratedView.initializeNewUserRespond(user);
    }

    @Override
    public void initializeNewUserNotify(final User user) {
        this.decoratedView.initializeNewUserNotify(user);
    }

    @Override
    public void userExitNotify(final User user) {
        this.decoratedView.userExitNotify(user);
    }

    @Override
    public void changeNameNotify(final User oldUser, final User newUser) {
        this.decoratedView.changeNameNotify(oldUser, newUser);
    }

    @Override
    public void getUsersRespond(final Response<Set<User>> users) {
        this.decoratedView.getUsersRespond(users);
    }

    @Override
    public void createLobbyNotify(final Lobby lobby) {
        this.decoratedView.createLobbyNotify(lobby);
    }

    @Override
    public void removeLobbyNotify(final Lobby lobby) {
        this.decoratedView.removeLobbyNotify(lobby);
    }

    @Override
    public void getLobbiesRespond(final Response<Set<Lobby>> lobbies) {
        this.decoratedView.getLobbiesRespond(lobbies);
    }

    @Override
    public void joinLobbyNotify(final Lobby lobby, final User user) {
        this.decoratedView.joinLobbyNotify(lobby, user);
    }

    @Override
    public void exitLobbyNotify(final Lobby lobby, final User user) {
        this.decoratedView.exitLobbyNotify(lobby, user);
    }

    @Override
    public void startLobbyNotify(final Lobby lobby, final Game game) {
        this.decoratedView.startLobbyNotify(lobby, game);
    }

    @Override
    public void removeGameNotify(final Game game) {
        this.decoratedView.removeGameNotify(game);
    }

    @Override
    public void getGamesRespond(final Response<Set<Game>> games) {
        this.decoratedView.getGamesRespond(games);
    }

    @Override
    public void playNotify(final Game game, final User user) {
        this.decoratedView.playNotify(game, user);
    }

    @Override
    public void doubtNotify(final Game game, final User user, final boolean win) {
        this.decoratedView.doubtNotify(game, user, win);
    }

    @Override
    public void urgeNotify(final Game game, final User user, final boolean win) {
        this.decoratedView.urgeNotify(game, user, win);
    }

    @Override
    public void callPalificoNotify(final Game game, final User user) {
        this.decoratedView.callPalificoNotify(game, user);
    }

    @Override
    public void exitGameNotify(final Game game, final User user) {
        this.decoratedView.exitGameNotify(game, user);
    }

    @Override
    public void gameEndedNotify(final Game game) {
        this.decoratedView.gameEndedNotify(game);
    }

    @Override
    public void showError(final ErrorType errorType) {
        this.decoratedView.showError(errorType);
    }

    @Override
    public void close() throws IOException {
        this.decoratedView.close();
    }

}

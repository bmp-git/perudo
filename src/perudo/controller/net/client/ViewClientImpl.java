package perudo.controller.net.client;

import java.util.Set;

import perudo.controller.net.DatagramStream;
import perudo.model.Game;
import perudo.model.Lobby;
import perudo.model.User;
import perudo.utility.ErrorType;
import perudo.utility.Response;
import perudo.view.View;

public class ViewClientImpl implements View {

    private final DatagramStream stream;
    private final View view;

    public ViewClientImpl(View view, DatagramStream stream) {
        this.view = view;
        this.stream = stream;
    }

    @Override
    public void initializeNewUserRespond(Response<User> user) {
        if (user.isOk()) {
            this.stream.setUser(user.getValue());
        }
        this.view.initializeNewUserRespond(user);
    }

    @Override
    public void initializeNewUserNotify(User user) {
        this.view.initializeNewUserNotify(user);
    }

    @Override
    public void userExitNotify(User user) {
        this.view.userExitNotify(user);
    }

    @Override
    public void changeNameNotify(User oldUser, User newUser) {
        this.view.changeNameNotify(oldUser, newUser);
    }

    @Override
    public void getUsersRespond(Response<Set<User>> users) {
        this.view.getUsersRespond(users);
    }

    @Override
    public void createLobbyNotify(Lobby lobby) {
        this.view.createLobbyNotify(lobby);
    }

    @Override
    public void removeLobbyNotify(Lobby lobby) {
        this.view.removeLobbyNotify(lobby);
    }

    @Override
    public void getLobbiesRespond(Response<Set<Lobby>> lobbies) {
        this.view.getLobbiesRespond(lobbies);
    }

    @Override
    public void joinLobbyNotify(Lobby lobby, User user) {
        this.view.joinLobbyNotify(lobby, user);
    }

    @Override
    public void exitLobbyNotify(Lobby lobby, User user) {
        this.view.exitLobbyNotify(lobby, user);
    }

    @Override
    public void startLobbyNotify(Lobby lobby, Game game) {
        this.view.startLobbyNotify(lobby, game);
    }

    @Override
    public void removeGameNotify(Game game) {
        this.view.removeGameNotify(game);
    }

    @Override
    public void getGamesRespond(Response<Set<Game>> games) {
        this.view.getGamesRespond(games);
    }

    @Override
    public void playNotify(Game game, User user) {
        this.view.playNotify(game, user);
    }

    @Override
    public void doubtNotify(Game game, User user, boolean win) {
        this.view.doubtNotify(game, user, win);
    }

    @Override
    public void urgeNotify(Game game, User user, boolean win) {
        this.view.urgeNotify(game, user, win);
    }

    @Override
    public void callPalificoNotify(Game game, User user) {
        this.view.callPalificoNotify(game, user);
    }

    @Override
    public void exitGameNotify(Game game, User user) {
        this.view.exitGameNotify(game, user);
    }

    @Override
    public void gameEndedNotify(Game game) {
        this.view.gameEndedNotify(game);
    }

    @Override
    public void showError(ErrorType errorType) {
        this.view.showError(errorType);
    }

}

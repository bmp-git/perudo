package perudo.controller.impl;

import java.io.IOException;

import perudo.controller.Controller;
import perudo.model.Bid;
import perudo.model.GameSettings;
import perudo.model.Lobby;
import perudo.model.User;
import perudo.model.UserType;
import perudo.view.View;

/**
 * A decorator of a Controller.
 */
public abstract class ControllerDecorator implements Controller {
    private final Controller decoratedController;

    /**
     * Creates a new decorator of a given Controller.
     * 
     * @param decoratedController
     *            the given controller to decorate
     */
    public ControllerDecorator(final Controller decoratedController) {
        this.decoratedController = decoratedController;
    }

    @Override
    public void initializeNewUser(final View view) {
        this.decoratedController.initializeNewUser(view);

    }

    @Override
    public void changeUserName(final User user, final String name) {
        this.decoratedController.changeUserName(user, name);

    }

    @Override
    public void getUsers(final User user) {
        this.decoratedController.getUsers(user);

    }

    @Override
    public void createLobby(final User user, final GameSettings info) {
        this.decoratedController.createLobby(user, info);

    }

    @Override
    public void getLobbies(final User user) {
        this.decoratedController.getLobbies(user);

    }

    @Override
    public void joinLobby(final User user, final Lobby lobby) {
        this.decoratedController.joinLobby(user, lobby);

    }

    @Override
    public void addBotToLobby(final User user, final Lobby lobby, final UserType type) {
        this.decoratedController.addBotToLobby(user, lobby, type);
    }

    @Override
    public void exitLobby(final User user) {
        this.decoratedController.exitLobby(user);

    }

    @Override
    public void startLobby(final User user) {
        this.decoratedController.startLobby(user);

    }

    @Override
    public void getGames(final User user) {
        this.decoratedController.getGames(user);

    }

    @Override
    public void play(final User user, final Bid bid) {
        this.decoratedController.play(user, bid);

    }

    @Override
    public void doubt(final User user) {
        this.decoratedController.doubt(user);

    }

    @Override
    public void urge(final User user) {
        this.decoratedController.urge(user);

    }

    @Override
    public void callPalifico(final User user) {
        this.decoratedController.callPalifico(user);

    }

    @Override
    public void exitGame(final User user) {
        this.decoratedController.exitGame(user);

    }

    @Override
    public void close(final User user) {
        this.decoratedController.close(user);
    }

    @Override
    public void closeNow(final User user) {
        this.decoratedController.closeNow(user);
    }

    @Override
    public void close() throws IOException {
        this.decoratedController.close();
    }

}

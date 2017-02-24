package perudo.view.console;

import java.util.Set;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.DefaultWindowManager;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogBuilder;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.screen.Screen;

import perudo.controller.Controller;
import perudo.model.Game;
import perudo.model.Lobby;
import perudo.model.User;
import perudo.utility.ErrorType;
import perudo.utility.Response;
import perudo.view.View;

public class ViewImpl implements View {

    private final MultiWindowTextGUI textGUI;
    private User user;

    private final GameMenuForm menuForm;
    private final LobbyForm lobbyForm;
    private final GameForm gameForm;
    private final Controller controller;

    public ViewImpl(final Screen screen, final Controller controller) {
        this.controller = controller;
        this.textGUI = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));
        this.menuForm = new GameMenuForm(controller, this.textGUI);
        this.lobbyForm = new LobbyForm(controller, textGUI);
        this.gameForm = new GameForm(controller, textGUI);

        textGUI.addWindow(lobbyForm.getWindow());
        textGUI.addWindow(menuForm.getWindow());
        textGUI.addWindow(gameForm.getWindow());

        this.setUser(null);
        this.controller.initializeNewUser(this);
        textGUI.setActiveWindow(menuForm.getWindow());

    }

    public void waitEnd() {
        textGUI.waitForWindowToClose(menuForm.getWindow());
        this.gameForm.close();
    }

    private void runOnGui(final Runnable run) {
        textGUI.getGUIThread().invokeLater(run);
    }

    private void setUser(final User user) {
        this.user = user;
        this.runOnGui(() -> {
            this.menuForm.setUser(this.user);
            this.lobbyForm.setUser(this.user);
            this.gameForm.setUser(this.user);
        });
    }

    @Override
    public void initializeNewUserRespond(final Response<User> user) {
        if (user.isOk()) {
            setUser(user.getValue());
            this.controller.getUsers(this.user);
            this.controller.getLobbies(this.user);
            this.controller.getGames(this.user);
        } else {
            this.closeNotify();
        }
    }

    @Override
    public void initializeNewUserNotify(final User user) {
        this.runOnGui(() -> {
            this.menuForm.addUser(user);
        });
    }

    @Override
    public void userExitNotify(final User user) {
        this.runOnGui(() -> {
            if (user == null || user.equals(this.user)) {
                this.menuForm.close();
            }
            this.menuForm.removeUser(user);
        });
    }

    @Override
    public void changeNameNotify(final User oldUser, final User newUser) {
        this.runOnGui(() -> {
            if (oldUser.equals(this.user)) {
                this.setUser(newUser);
            }
            this.menuForm.removeUser(oldUser);
            this.menuForm.addUser(newUser);
        });
    }

    @Override
    public void getUsersRespond(final Response<Set<User>> users) {
        this.runOnGui(() -> {
            if (users.isOk()) {
                this.menuForm.setUsers(users.getValue());
            } else {
                this.showError(users.getErrorType());
            }
        });

    }

    @Override
    public void createLobbyNotify(final Lobby lobby) {
        this.runOnGui(() -> {
            this.menuForm.addLobby(lobby);
        });

    }

    @Override
    public void removeLobbyNotify(final Lobby lobby) {
        this.runOnGui(() -> {
            this.menuForm.removeLobby(lobby);
        });

    }

    @Override
    public void getLobbiesRespond(final Response<Set<Lobby>> lobbies) {
        this.runOnGui(() -> {
            if (lobbies.isOk()) {
                this.menuForm.setLobbies(lobbies.getValue());
            } else {
                this.showError(lobbies.getErrorType());
            }
        });

    }

    @Override
    public void joinLobbyNotify(final Lobby lobby, final User user) {
        this.runOnGui(() -> {
            if (user.equals(this.user)) {
                // start lobby in view
                this.lobbyForm.setLobby(lobby);
                this.textGUI.setActiveWindow(this.lobbyForm.getWindow());
            }

            this.lobbyForm.updateLobby(lobby);
            this.menuForm.removeLobby(lobby);
            this.menuForm.addLobby(lobby);

        });

    }

    @Override
    public void exitLobbyNotify(final Lobby lobby, final User user) {
        this.runOnGui(() -> {
            if (user.equals(this.user)) {
                this.textGUI.setActiveWindow(this.menuForm.getWindow());
            }

            this.lobbyForm.updateLobby(lobby);
            this.menuForm.removeLobby(lobby);
            this.menuForm.addLobby(lobby);
        });

    }

    @Override
    public void startLobbyNotify(final Lobby lobby, final Game game) {
        this.runOnGui(() -> {
            if (lobby.getUsers().contains(this.user)) {
                this.gameForm.setGame(game);
                this.textGUI.setActiveWindow(this.gameForm.getWindow());
            }
            this.menuForm.removeLobby(lobby);
            this.menuForm.addGame(game);
        });

    }

    @Override
    public void getGamesRespond(final Response<Set<Game>> games) {
        this.runOnGui(() -> {
            if (games.isOk()) {
                this.menuForm.setGames(games.getValue());
            } else {
                this.showError(games.getErrorType());
            }
        });

    }

    @Override
    public void playNotify(final Game game, final User user) {
        this.runOnGui(() -> {
            this.gameForm.playNotify(game, user);
        });

    }

    @Override
    public void doubtNotify(final Game game, final User user, final boolean win) {
        this.runOnGui(() -> {
            this.gameForm.doubtNotify(game, user, win);
        });

    }

    @Override
    public void urgeNotify(final Game game, final User user, final boolean win) {
        this.runOnGui(() -> {
            this.gameForm.urgeNotify(game, user, win);
        });

    }

    @Override
    public void callPalificoNotify(final Game game, final User user) {
        this.runOnGui(() -> {
            this.gameForm.callPalificoNotify(game, user);
        });

    }

    @Override
    public void exitGameNotify(final Game game, final User user) {
        this.runOnGui(() -> {
            if (user.equals(this.user)) {
                Utils.showMessageBox("Left game", "You left the game", this.textGUI);
                this.textGUI.setActiveWindow(this.menuForm.getWindow());
            }
            this.gameForm.exitGameNotify(game, user);
        });

    }

    @Override
    public void gameEndedNotify(final Game game) {
        this.runOnGui(() -> {
            if (game.equals(this.gameForm.getGame())) {
                String win = (game.getUserStatus(this.user) != null
                        && game.getUserStatus(this.user).getRemainingDice() > 0) ? "win" : "lose";
                Utils.showMessageBox("Game ended", "The game is ended\nYou " + win + "\nExit game please",
                        this.textGUI);
            }
        });

    }

    @Override
    public void removeGameNotify(final Game game) {
        this.runOnGui(() -> {
            this.menuForm.removeGame(game);
        });
    }

    @Override
    public void showError(final ErrorType errorType) {
        this.runOnGui(() -> {
            new MessageDialogBuilder().setTitle("Error - " + errorType.name()).setText(errorType.getMessage())
                    .addButton(MessageDialogButton.OK).build().showDialog(this.textGUI);
        });

    }

    public void closeNotify() {
        this.menuForm.close();

        synchronized (this) {
            this.notify();
        }
    }

}

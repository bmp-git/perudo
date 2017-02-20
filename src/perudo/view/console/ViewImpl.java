package perudo.view.console;

import java.io.IOException;
import java.util.Set;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.DefaultWindowManager;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogBuilder;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import perudo.controller.Controller;
import perudo.controller.impl.StandardControllerImpl;
import perudo.model.Game;
import perudo.model.Lobby;
import perudo.model.User;
import perudo.utility.ErrorType;
import perudo.utility.Response;
import perudo.view.View;

public class ViewImpl implements View {

    public static void main(String[] args) throws IOException, InterruptedException {
        StandardControllerImpl controller = new StandardControllerImpl();
        Terminal terminal1 = new DefaultTerminalFactory().createTerminal();
        Screen screen1 = new TerminalScreen(terminal1);
        screen1.startScreen();
        ViewImpl w2 = new ViewImpl(screen1, controller);

        Thread th = new Thread(() -> {
            Screen screen = null;
            try {
                Terminal terminal = new DefaultTerminalFactory().createTerminal();
                screen = new TerminalScreen(terminal);
                screen.startScreen();
            } catch (IOException e) {
            }
            ViewImpl w1 = new ViewImpl(screen, controller);
            w1.waitEnd();
            try {
                screen.stopScreen();
            } catch (IOException e) {
            }
        });
        th.start();

        w2.waitEnd();
        screen1.stopScreen();
        th.join();
        controller.close();
        System.out.println("bye");
    }

    private final MultiWindowTextGUI textGUI;
    private User user;

    private final GameMenuForm menuForm;
    private final LobbyForm lobbyInForm;
    private final GameForm gameForm;
    private final Controller controller;

    public ViewImpl(final Screen screen, final Controller controller) {
        this.controller = controller;
        this.textGUI = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));
        this.menuForm = new GameMenuForm(controller, this.textGUI);
        this.lobbyInForm = new LobbyForm(controller, textGUI);
        this.gameForm = new GameForm(controller, textGUI);

        textGUI.addWindow(lobbyInForm.getWindow());
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

    private void runOnGui(Runnable run) {
        textGUI.getGUIThread().invokeLater(run);
    }

    private void setUser(User user) {
        this.user = user;
        this.runOnGui(() -> {
            this.menuForm.setUser(this.user);
            this.lobbyInForm.setUser(this.user);
            this.gameForm.setUser(this.user);
        });
    }

    @Override
    public void initializeNewUserRespond(Response<User> user) {
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
    public void initializeNewUserNotify(User user) {
        this.runOnGui(() -> {
            this.menuForm.addUser(user);
        });
    }

    @Override
    public void userExitNotify(User user) {
        this.runOnGui(() -> {
            if (user.equals(this.user)) {
                this.menuForm.close();
            }
            this.menuForm.removeUser(user);
        });
    }

    @Override
    public void changeNameNotify(User user) {
        this.runOnGui(() -> {
            if (user.equals(this.user)) {
                this.setUser(user);
            }
            this.menuForm.removeUser(user);
            this.menuForm.addUser(user);

        });
    }

    @Override
    public void getUsersRespond(Response<Set<User>> users) {
        this.runOnGui(() -> {
            if (users.isOk()) {
                this.menuForm.setUsers(users.getValue());
            } else {
                this.showError(users.getErrorType());
            }
        });

    }

    @Override
    public void createLobbyNotify(Lobby lobby) {
        this.runOnGui(() -> {
            this.menuForm.addLobby(lobby);
        });

    }

    @Override
    public void removeLobbyNotify(Lobby lobby) {
        this.runOnGui(() -> {
            this.menuForm.removeLobby(lobby);
        });

    }

    @Override
    public void getLobbiesRespond(Response<Set<Lobby>> lobbies) {
        this.runOnGui(() -> {
            if (lobbies.isOk()) {
                this.menuForm.setLobbies(lobbies.getValue());
            } else {
                this.showError(lobbies.getErrorType());
            }
        });

    }

    @Override
    public void joinLobbyNotify(Lobby lobby, User user) {
        this.runOnGui(() -> {
            if (user.equals(this.user)) {
                // start lobby in view
                this.lobbyInForm.setLobby(lobby);
                this.textGUI.setActiveWindow(this.lobbyInForm.getWindow());
            }

            this.lobbyInForm.updateLobby(lobby);
            this.menuForm.removeLobby(lobby);
            this.menuForm.addLobby(lobby);

        });

    }

    @Override
    public void exitLobbyNotify(Lobby lobby, User user) {
        this.runOnGui(() -> {
            if (user.equals(this.user)) {
                this.textGUI.setActiveWindow(this.menuForm.getWindow());
            }

            this.lobbyInForm.updateLobby(lobby);
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
    public void getGamesRespond(Response<Set<Game>> games) {
        this.runOnGui(() -> {
            if (games.isOk()) {
                this.menuForm.setGames(games.getValue());
            } else {
                this.showError(games.getErrorType());
            }
        });

    }

    @Override
    public void playNotify(Game game, User user) {
        this.runOnGui(() -> {
            this.gameForm.playNotify(game, user);
        });

    }

    @Override
    public void doubtNotify(Game game, User user, boolean win) {
        this.runOnGui(() -> {
            this.gameForm.doubtNotify(game, user, win);
        });

    }

    @Override
    public void urgeNotify(Game game, User user, boolean win) {
        this.runOnGui(() -> {
            this.gameForm.urgeNotify(game, user, win);
        });

    }

    @Override
    public void callPalificoNotify(Game game, User user) {
        this.runOnGui(() -> {
            this.gameForm.callPalificoNotify(game, user);
        });

    }

    @Override
    public void exitGameNotify(Game game, User user) {
        this.runOnGui(() -> {
            
            
            if (user.equals(this.user)) {
                Utils.showMessageBox("Left game", "You left the game", this.textGUI);
                this.textGUI.setActiveWindow(this.menuForm.getWindow());
            }
            System.out.println("exitGameNotify start "+this.user.getName());
            
            this.gameForm.exitGameNotify(game, user);
            
            System.out.println("exitGameNotify end "+this.user.getName());
        });

    }

    @Override
    public void gameEndedNotify(Game game) {
        this.runOnGui(() -> {
            System.out.println("gameEndedNotify start "+this.user.getName());
            if (game.equals(this.gameForm.getGame())) {
                String win = (game.getUserStatus(this.user) != null
                        && game.getUserStatus(this.user).getRemainingDice() > 0) ? "win" : "lose";
                Utils.showMessageBox("Game ended", "The game is ended\nYou " + win + "\nExit game please",
                        this.textGUI);
            }
            System.out.println("gameEndedNotify end "+this.user.getName());
        });

    }

    @Override
    public void removeGameNotify(Game game) {
        this.runOnGui(() -> {
            System.out.println("removeGameNotify start"+this.user.getName());
            this.menuForm.removeGame(game);
            System.out.println("removeGameNotify end"+this.user.getName());
        });
    }

    @Override
    public void showError(ErrorType errorType) {
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

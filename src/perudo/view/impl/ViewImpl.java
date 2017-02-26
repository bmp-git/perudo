package perudo.view.impl;

import java.io.IOException;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import perudo.controller.Controller;
import perudo.model.Game;
import perudo.model.Lobby;
import perudo.model.User;
import perudo.utility.ErrorType;
import perudo.utility.Response;
import perudo.view.GUIFactory;
import perudo.view.View;

public class ViewImpl implements View {

    private static final String TITLE = "Perudo";

    private final Controller controller;
    private final GUIFactory factory;
    private User user;
    private final JFrame mainFrame;

    /* Application panels */
    private final MenuPanel menuPanel;

    public ViewImpl() {
        this.controller = ControllerSingleton.getController();
        this.factory = new StandardGUIFactory();
        this.mainFrame = this.factory.createFrame(TITLE);
        this.mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.menuPanel = (MenuPanel) this.factory.createMenuPanel();
        this.controller.initializeNewUser(this);
        this.mainFrame.setLocationByPlatform(true);
        this.showFrame();
        this.showPanel(menuPanel);
    }

    private void showFrame() {
        GUIUtility.fitFrame(this.mainFrame, 2);
    }

    private void showPanel(final JPanel panel) {
        this.mainFrame.getContentPane().removeAll();
        this.mainFrame.getContentPane().add(panel);
        this.mainFrame.getContentPane().revalidate();
    }

    @Override
    public void initializeNewUserRespond(final Response<User> user) {
        if (!user.isOk()) {
            System.exit(1);
        }
        this.user = user.getValue();
        /* setto i pannelli */
        this.menuPanel.setUser(this.user);
        this.menuPanel.updateAll();
        this.menuPanel.updateUsers(this.user);
        showPanel(menuPanel);
    }

    @Override
    public void initializeNewUserNotify(final User user) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                menuPanel.updateUsers(user);
                showPanel(menuPanel);
            }
        });
    }

    @Override
    public void userExitNotify(final User user) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (user.equals(ViewImpl.this.user)) {
                    mainFrame.dispose();
                }
                menuPanel.removeUser(user);
                showPanel(menuPanel);
            }
        });
    }

    @Override
    public void changeNameNotify(final User oldUser, final User newUser) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (user.equals(oldUser)) {
                    menuPanel.setUser(newUser);
                    user = newUser;
                }
                menuPanel.updateUsers(newUser);
                showPanel(menuPanel);
            }
        });
    }

    @Override
    public void getUsersRespond(final Response<Set<User>> users) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (users.isOk()) {
                    users.getValue().forEach(e -> menuPanel.updateUsers(e));
                    showPanel(menuPanel);
                }
            }
        });
    }

    @Override
    public void createLobbyNotify(final Lobby lobby) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                menuPanel.addLobby(lobby);
                showPanel(menuPanel);
            }
        });

    }

    @Override
    public void removeLobbyNotify(final Lobby lobby) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                menuPanel.removeLobby(lobby);
                showPanel(menuPanel);
            }
        });
    }

    @Override
    public void getLobbiesRespond(final Response<Set<Lobby>> lobbies) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                lobbies.getValue().forEach(l -> menuPanel.updateLobby(l));
                showPanel(menuPanel);
            }
        });
    }

    @Override
    public void joinLobbyNotify(final Lobby lobby, final User user) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                menuPanel.updateLobby(lobby);
                showPanel(menuPanel);
            }
        });
    }

    @Override
    public void exitLobbyNotify(final Lobby lobby, final User user) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (lobby.getUsers().size() < 1) {
                    menuPanel.removeLobby(lobby);
                } else {
                    menuPanel.updateLobby(lobby);
                }
                showPanel(menuPanel);
            }
        });
    }

    @Override
    public void startLobbyNotify(final Lobby lobby, final Game game) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (lobby.getUsers().contains(user)) {
                    // menuPanel.start(game);
                    // showPanel(gamePanel);
                } else {
                    menuPanel.removeLobby(lobby);
                    // menuPanel.addGame(game);
                    showPanel(menuPanel);
                }
            }
        });
    }

    @Override
    public void removeGameNotify(final Game game) {
        // TODO Auto-generated method stub

    }

    @Override
    public void getGamesRespond(final Response<Set<Game>> games) {
        // TODO Auto-generated method stub
    }

    @Override
    public void playNotify(final Game game, final User user) {
        // TODO Auto-generated method stub

    }

    @Override
    public void doubtNotify(final Game game, final User user, final boolean win) {
        // TODO Auto-generated method stub

    }

    @Override
    public void urgeNotify(final Game game, final User user, final boolean win) {
        // TODO Auto-generated method stub

    }

    @Override
    public void callPalificoNotify(final Game game, final User user) {
        // TODO Auto-generated method stub

    }

    @Override
    public void exitGameNotify(final Game game, final User user) {
        // TODO Auto-generated method stub

    }

    @Override
    public void gameEndedNotify(final Game game) {
        // TODO Auto-generated method stub

    }

    @Override
    public void showError(final ErrorType errorType) {
        this.menuPanel.showError(errorType);
        System.out.println(errorType);
    }

    @Override
    public void close() throws IOException {
        this.mainFrame.dispose();
    }

}

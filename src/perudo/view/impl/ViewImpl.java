package perudo.view.impl;

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

    private Controller controller;
    private final GUIFactory factory;
    private User user;
    private JFrame mainFrame;

    /* Application panels */
    private MenuPanel menuPanel;

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

    private void showPanel(JPanel panel) {
        this.mainFrame.getContentPane().removeAll();
        this.mainFrame.getContentPane().add(panel);
        this.mainFrame.getContentPane().revalidate();
    }

    @Override
    public void initializeNewUserRespond(Response<User> user) {
        if (!user.isOk()) {
            System.exit(1);
        }
        this.user = user.getValue();
        /* setto i pannelli */
        this.menuPanel.setUser(this.user);
        this.menuPanel.updateAll();
        menuPanel.updateUsers(this.user);
        showPanel(menuPanel);
    }

    @Override
    public void initializeNewUserNotify(User user) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                menuPanel.updateUsers(user);
                showPanel(menuPanel);
            }
        });
    }

    @Override
    public void userExitNotify(User user) {
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
    public void changeNameNotify(User oldUser, User newUser) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (user.equals(oldUser)) {
                    menuPanel.setUser(newUser);
                }
                menuPanel.updateUsers(newUser);
                showPanel(menuPanel);
            }
        });
    }

    @Override
    public void getUsersRespond(Response<Set<User>> users) {
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
    public void createLobbyNotify(Lobby lobby) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                menuPanel.addLobby(lobby);
                showPanel(menuPanel);
            }
        });

    }

    @Override
    public void removeLobbyNotify(Lobby lobby) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                menuPanel.removeLobby(lobby);
                showPanel(menuPanel);
            }
        });
    }

    @Override
    public void getLobbiesRespond(Response<Set<Lobby>> lobbies) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                    lobbies.getValue().forEach(l -> menuPanel.updateLobby(l));
                    showPanel(menuPanel);
            }
        });
    }

    @Override
    public void joinLobbyNotify(Lobby lobby, User user) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                menuPanel.updateLobby(lobby);
                showPanel(menuPanel);
            }
        });
    }

    @Override
    public void exitLobbyNotify(Lobby lobby, User user) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                menuPanel.removeLobby(lobby);
                showPanel(menuPanel);
            }
        });
    }

    @Override
    public void startLobbyNotify(Lobby lobby, Game game) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                menuPanel.removeLobby(lobby);
                showPanel(menuPanel);
            }
        });
    }

    @Override
    public void removeGameNotify(Game game) {
        // TODO Auto-generated method stub

    }

    @Override
    public void getGamesRespond(Response<Set<Game>> games) {
        // TODO Auto-generated method stub
    }

    @Override
    public void playNotify(Game game, User user) {
        // TODO Auto-generated method stub

    }

    @Override
    public void doubtNotify(Game game, User user, boolean win) {
        // TODO Auto-generated method stub

    }

    @Override
    public void urgeNotify(Game game, User user, boolean win) {
        // TODO Auto-generated method stub

    }

    @Override
    public void callPalificoNotify(Game game, User user) {
        // TODO Auto-generated method stub

    }

    @Override
    public void exitGameNotify(Game game, User user) {
        // TODO Auto-generated method stub

    }

    @Override
    public void gameEndedNotify(Game game) {
        // TODO Auto-generated method stub

    }

    @Override
    public void showError(ErrorType errorType) {
        this.menuPanel.showError(errorType);
        System.out.println(errorType);
    }

}

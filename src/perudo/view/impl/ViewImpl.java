package perudo.view.impl;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import perudo.model.Game;
import perudo.model.Lobby;
import perudo.model.User;
import perudo.utility.ErrorType;
import perudo.utility.LogSeverity;
import perudo.utility.Response;
import perudo.utility.impl.LoggerSingleton;
import perudo.view.GUIFactory;
import perudo.view.View;

/**
 * Class implementing the View interface.
 */
public class ViewImpl implements View {

    private static final String TITLE = "Perudo";
    private static final String EXIT_NAME = "Quitting..";
    private static final String EXIT_TEXT = "Do you really want to quit?";

    private User user;
    private final JFrame mainFrame;
    private final CountDownLatch latch;

    /* Application panels */
    private final MenuPanel menuPanel;
    private GamePanel gamePanel;

    /**
     * Initialize the application view and show the menuPanel in frame.
     */
    public ViewImpl() {
        final GUIFactory factory = GUIFactorySingleton.getFactory();
        this.latch = new CountDownLatch(1);
        this.mainFrame = factory.createFrame(TITLE);
        this.mainFrame.setIconImage(Icon.APPLICATION_ICON.getIcon().getImage());
        this.mainFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.mainFrame.setLocationByPlatform(true);
        this.mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent event) {
                final int n = JOptionPane.showConfirmDialog(mainFrame, EXIT_TEXT, EXIT_NAME, JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION) {
                    ControllerSingleton.getController().closeNow(user);
                }
            }
        });
        this.menuPanel = new MenuPanel();
        GUIUtility.fitFrame(this.mainFrame, 2);
        this.showPanel(this.menuPanel);
        ControllerSingleton.getController().initializeNewUser(this);

    }

    private void showPanel(final JPanel panel) {
        this.mainFrame.getContentPane().removeAll();
        this.mainFrame.getContentPane().add(panel);
        this.mainFrame.getContentPane().revalidate();
    }

    private boolean isMyGame(final Game game) {
        return this.gamePanel.getGame().equals(game);
    }

    @Override
    public void initializeNewUserRespond(final Response<User> user) {
        if (!user.isOk()) {
            try {
                this.close();
            } catch (IOException e) {
                LoggerSingleton.get().add(LogSeverity.ERROR_UNEXPECTED, this.getClass(), "User is not ok");
            }
        }
        this.user = user.getValue();
        this.menuPanel.setUser(this.user);
        this.menuPanel.updateAll();
        this.menuPanel.updateUsers(this.user);
        showPanel(this.menuPanel);
    }

    @Override
    public void initializeNewUserNotify(final User user) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                menuPanel.updateUsers(user);
                mainFrame.getContentPane().revalidate();
            }
        });
    }

    @Override
    public void userExitNotify(final User user) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (ViewImpl.this.user.equals(user)) {
                    mainFrame.dispose();
                }
                menuPanel.removeUser(user);
                mainFrame.getContentPane().revalidate();
            }
        });
    }

    @Override
    public void changeNameNotify(final User oldUser, final User newUser) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (ViewImpl.this.user.equals(oldUser)) {
                    menuPanel.setUser(newUser);
                    user = newUser;
                }
                menuPanel.updateUsers(newUser);
                mainFrame.getContentPane().revalidate();
            }
        });
    }

    @Override
    public void getUsersRespond(final Response<Set<User>> users) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (users.isOk()) {
                    users.getValue().forEach(e -> menuPanel.updateUsers(e));
                    mainFrame.getContentPane().revalidate();
                }
            }
        });
    }

    @Override
    public void createLobbyNotify(final Lobby lobby) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                menuPanel.addLobby(lobby);
                mainFrame.getContentPane().revalidate();
            }
        });

    }

    @Override
    public void removeLobbyNotify(final Lobby lobby) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                menuPanel.removeLobby(lobby);
                mainFrame.getContentPane().revalidate();
            }
        });
    }

    @Override
    public void getLobbiesRespond(final Response<Set<Lobby>> lobbies) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (lobbies.isOk()) {
                    lobbies.getValue().forEach(l -> menuPanel.updateLobby(l));
                    mainFrame.getContentPane().revalidate();
                }
            }
        });
    }

    @Override
    public void joinLobbyNotify(final Lobby lobby, final User user) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                menuPanel.updateLobby(lobby);
                mainFrame.getContentPane().revalidate();
            }
        });
    }

    @Override
    public void exitLobbyNotify(final Lobby lobby, final User user) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (lobby.getUsers().size() < 1 || lobby.getUsers().stream().allMatch(u -> u.getType().isBot())) {
                    menuPanel.removeLobby(lobby);
                } else {
                    menuPanel.updateLobby(lobby);
                }
                mainFrame.getContentPane().revalidate();
            }
        });
    }

    @Override
    public void startLobbyNotify(final Lobby lobby, final Game game) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (lobby.getUsers().contains(user)) {
                    gamePanel = new GamePanel();
                    gamePanel.setUser(user);
                    gamePanel.setGame(game);
                    showPanel(gamePanel);
                } else if (mainFrame.getContentPane().equals(menuPanel)) {
                    menuPanel.removeLobby(lobby);
                    // menuPanel.addGame(game);
                    showPanel(menuPanel);
                }
            }
        });
    }

    @Override
    public void removeGameNotify(final Game game) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // menuPanel.removeGame(game);
                mainFrame.getContentPane().revalidate();
            }
        });
    }

    @Override
    public void getGamesRespond(final Response<Set<Game>> games) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (games.isOk()) {
                    // games.getValue().forEach(g -> menuPanel.updateGame(g));
                    mainFrame.getContentPane().revalidate();
                }
            }
        });
    }

    @Override
    public void playNotify(final Game game, final User user) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (isMyGame(game)) {
                    gamePanel.playNotify(game, user);
                    mainFrame.getContentPane().revalidate();
                }
            }
        });
    }

    @Override
    public void doubtNotify(final Game game, final User user, final boolean win) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (isMyGame(game)) {
                    gamePanel.doubtNotify(game, user, win);
                    mainFrame.getContentPane().revalidate();
                }
            }
        });
    }

    @Override
    public void urgeNotify(final Game game, final User user, final boolean win) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (isMyGame(game)) {
                    gamePanel.urgeNotify(game, user, win);
                    mainFrame.getContentPane().revalidate();
                }
            }
        });
    }

    @Override
    public void callPalificoNotify(final Game game, final User user) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (isMyGame(game)) {
                    gamePanel.callPalificoNotify(game, user);
                    mainFrame.getContentPane().revalidate();
                }
            }
        });
    }

    @Override
    public void exitGameNotify(final Game game, final User user) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (isMyGame(game)) {
                    if (ViewImpl.this.user.equals(user)) {
                        showPanel(menuPanel);
                        menuPanel.repaint();
                    } else {
                        gamePanel.exitGameNotify(game, user);
                    }
                    mainFrame.getContentPane().revalidate();
                }
            }
        });
    }

    @Override
    public void gameEndedNotify(final Game game) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (isMyGame(game)) {
                    gamePanel.gameEndedNotify(game);
                    mainFrame.getContentPane().revalidate();
                }
            }
        });
    }

    @Override
    public void showError(final ErrorType errorType) {
        JOptionPane.showMessageDialog(mainFrame, errorType.getMessage(), "Error: " + errorType.getId(),
                JOptionPane.ERROR_MESSAGE);
        System.out.println(errorType);
    }

    @Override
    public void close() throws IOException {
        this.mainFrame.setVisible(false);
        this.mainFrame.dispose();
        latch.countDown();
    }

    /**
     * Return if the user want to return to menu.
     * 
     * @return true if the user want to return to start menu, false otherwise
     */
    public boolean isReturnMenu() {
        return this.menuPanel.isReturnMenu();
    }

    /**
     * Wait for the View closing.
     */
    public void await() {
        try {
            this.latch.await();
        } catch (InterruptedException e) {
            LoggerSingleton.get().add(LogSeverity.ERROR_UNEXPECTED, this.getClass(), "Await method crashed.");
        }
    }
}

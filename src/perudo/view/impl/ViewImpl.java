package perudo.view.impl;

import java.time.Duration;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import perudo.controller.Controller;
import perudo.model.Game;
import perudo.model.Lobby;
import perudo.model.User;
import perudo.model.impl.GameSettingsImpl;
import perudo.model.impl.LobbyImpl;
import perudo.model.impl.UserImpl;
import perudo.utility.ErrorType;
import perudo.utility.ErrorTypeException;
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

        try {
            this.createLobbyNotify(new LobbyImpl(new GameSettingsImpl(4, 6, 5, Duration.ofMinutes(1), "Lobby1"),this.user));
            this.createLobbyNotify(new LobbyImpl(new GameSettingsImpl(4, 6, 5, Duration.ofMinutes(1), "Lobby2"),this.user));
            this.createLobbyNotify(new LobbyImpl(new GameSettingsImpl(4, 6, 5, Duration.ofMinutes(1), "Lobby3"),this.user));
            this.createLobbyNotify(new LobbyImpl(new GameSettingsImpl(4, 6, 5, Duration.ofMinutes(1), "Lobby4"), new UserImpl("Simone")));
        } catch (ErrorTypeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void initializeNewUserNotify(User user) {
        // TODO Auto-generated method stub
    }
    @Override
    public void userExitNotify(User user) {
        // TODO Auto-generated method stub
    }

    @Override
    public void changeNameNotify(User oldUser, User newUser) {
        if(this.user.equals(oldUser)) {
            this.menuPanel.setUser(newUser);
        }
    }

    @Override
    public void getUsersRespond(Response<Set<User>> users) {
        // TODO Auto-generated method stub

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
        // TODO Auto-generated method stub
    }


    @Override
    public void getLobbiesRespond(Response<Set<Lobby>> lobbies) {
        // TODO Auto-generated method stub

    }

    @Override
    public void joinLobbyNotify(Lobby lobby, User user) {
        this.menuPanel.updateLobby(lobby);

    }

    @Override
    public void exitLobbyNotify(Lobby lobby, User user) {
        this.menuPanel.updateLobby(lobby);

    }

    @Override
    public void startLobbyNotify(Lobby lobby, Game game) {
        this.menuPanel.removeLobby(lobby);

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

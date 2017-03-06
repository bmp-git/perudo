package perudo.view.impl.components;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Optional;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import perudo.model.User;
import perudo.view.GUIFactory;
import perudo.view.impl.ControllerSingleton;
import perudo.view.impl.GUIUtility;
import perudo.view.impl.StandardGUIFactory;
import perudo.view.impl.panels.ChangeNamePanel;
import perudo.view.impl.panels.CreateLobbyPanel;

public class TopMenu extends JMenuBar {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String MENU_USER = "User";
    private static final String MENU_LOBBY = "Lobby";
    private static final String MENU_USER_CHANGENAME = "Change user name";
    private static final String MENU_USER_CHANGENAME_TOOLTIP = "Change your user name..";
    private static final String MENU_LOBBY_CREATELOBBY = "Create lobby";
    private static final String MENU_LOBBY_CREATELOBBY_TOOLTIP = "Create a new lobby..";

    private final GUIFactory factory;
    private final CreateLobbyPanel pnlcreatelobby;
    private final ChangeNamePanel pnlchangename;

    private Optional<User> user;

    public TopMenu() {
        super();
        this.factory = new StandardGUIFactory();
        this.user = Optional.empty();
        this.pnlcreatelobby = (CreateLobbyPanel) this.factory.createCreateLobbyPanel();
        this.pnlchangename = (ChangeNamePanel) this.factory.createChangeNamePanel();
        JMenu perudoMenu = new JMenu("Perudo");
        perudoMenu.setMnemonic(KeyEvent.VK_P);
        JMenuItem miChangeType = new JMenuItem("Offline/Online");
        miChangeType.setMnemonic(KeyEvent.VK_C);
        miChangeType.setToolTipText("Change status from Offline/Online");
        miChangeType.addActionListener(e -> {
            if (ControllerSingleton.getControllerType() == ControllerSingleton.ControllerType.MULTIPLAYER) {
                ControllerSingleton.setSingleplayerController();
            } else if (ControllerSingleton.getControllerType() == ControllerSingleton.ControllerType.SINGLEPLAYER) {
                try {
                    ControllerSingleton.setMultiplayerController("2.224.173.8", 45555);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            // ControllerSingleton.getController().initializeNewUser(view);
        });
        perudoMenu.add(miChangeType);

        JMenu userMenu = new JMenu(MENU_USER);
        userMenu.setMnemonic(KeyEvent.VK_U);
        JMenuItem miChangeName = new JMenuItem(MENU_USER_CHANGENAME);
        miChangeName.setMnemonic(KeyEvent.VK_C);
        miChangeName.setToolTipText(MENU_USER_CHANGENAME_TOOLTIP);
        miChangeName.addActionListener(e -> {
            int n = JOptionPane.showConfirmDialog(TopMenu.this, this.pnlchangename, ChangeNamePanel.TITLE,
                    JOptionPane.OK_CANCEL_OPTION);
            if (n == JOptionPane.YES_OPTION && this.user.isPresent() && this.pnlchangename.getName().trim().length() > 0) {
                ControllerSingleton.getController().changeUserName(this.user.get(), this.pnlchangename.getName());
            }
        });
        userMenu.add(miChangeName);

        JMenu lobbyMenu = new JMenu(MENU_LOBBY);
        lobbyMenu.setMnemonic(KeyEvent.VK_L);
        JMenuItem miCreateLobby = new JMenuItem(MENU_LOBBY_CREATELOBBY);
        miCreateLobby.setMnemonic(KeyEvent.VK_C);
        miCreateLobby.setToolTipText(MENU_LOBBY_CREATELOBBY_TOOLTIP);
        miCreateLobby.addActionListener(e -> {
            int n = JOptionPane.showConfirmDialog(TopMenu.this, this.pnlcreatelobby, CreateLobbyPanel.TITLE,
                    JOptionPane.OK_CANCEL_OPTION, 0, GUIUtility.getIcon(CreateLobbyPanel.ICON_RESPATH));
            if (n == JOptionPane.YES_OPTION && this.user.isPresent() && this.pnlcreatelobby.getName().trim().length() > 0) {
                ControllerSingleton.getController().createLobby(this.user.get(), this.pnlcreatelobby.getGameSettings());
            }
        });
        lobbyMenu.add(miCreateLobby);

        this.add(userMenu);
        this.add(lobbyMenu);
    }

    public void setUser(User user) {
        this.user = Optional.ofNullable(user);
    }

}

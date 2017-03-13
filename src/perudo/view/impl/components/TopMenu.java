package perudo.view.impl.components;

import java.awt.event.KeyEvent;
import java.util.Optional;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import perudo.model.User;
import perudo.view.GUIFactory;
import perudo.view.impl.ControllerSingleton;
import perudo.view.impl.GUIFactorySingleton;
import perudo.view.impl.Icon;
import perudo.view.impl.panels.ChangeNamePanel;
import perudo.view.impl.panels.CreateLobbyPanel;

/**
 * A customized MenuBar for menuPanel.
 */
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

    private Optional<User> user;

    /**
     * Initialize the menu with menu items.
     */
    public TopMenu() {
        super();
        this.user = Optional.empty();
        final GUIFactory factory = GUIFactorySingleton.getFactory();
        final CreateLobbyPanel pnlcreatelobby = new CreateLobbyPanel();
        final ChangeNamePanel pnlchangename = new ChangeNamePanel();

        /*
         * JMenu perudoMenu = (JMenu) this.factory.createMenu("Perudo");
         * perudoMenu.setMnemonic(KeyEvent.VK_P); JMenuItem miChangeType =
         * (JMenuItem) this.factory.createMenuItem("Offline/Online",
         * KeyEvent.VK_C, "Change status from Offline/Online");
         * miChangeType.addActionListener(e -> { if
         * (ControllerSingleton.getControllerType() ==
         * ControllerSingleton.ControllerType.MULTIPLAYER) {
         * ControllerSingleton.setSingleplayerController(); } else if
         * (ControllerSingleton.getControllerType() ==
         * ControllerSingleton.ControllerType.SINGLEPLAYER) { try {
         * ControllerSingleton.setMultiplayerController("2.224.173.8", 45555); }
         * catch (IOException e1) { e1.printStackTrace(); } }
         * ControllerSingleton.getController().initializeNewUser(view); });
         * perudoMenu.add(miChangeType);
         */

        final JMenu userMenu = (JMenu) factory.createMenu(MENU_USER);
        userMenu.setMnemonic(KeyEvent.VK_U);
        final JMenuItem miChangeName = (JMenuItem) factory.createMenuItem(MENU_USER_CHANGENAME, KeyEvent.VK_C,
                MENU_USER_CHANGENAME_TOOLTIP);
        miChangeName.addActionListener(e -> {
            final int n = JOptionPane.showConfirmDialog(TopMenu.this, pnlchangename, ChangeNamePanel.TITLE,
                    JOptionPane.OK_CANCEL_OPTION);
            if (n == JOptionPane.YES_OPTION && this.user.isPresent() && pnlchangename.getName().trim().length() > 0) {
                ControllerSingleton.getController().changeUserName(this.user.get(), pnlchangename.getName());
            }
        });
        userMenu.add(miChangeName);

        final JMenu lobbyMenu = (JMenu) factory.createMenu(MENU_LOBBY);
        lobbyMenu.setMnemonic(KeyEvent.VK_L);
        final JMenuItem miCreateLobby = (JMenuItem) factory.createMenuItem(MENU_LOBBY_CREATELOBBY, KeyEvent.VK_C,
                MENU_LOBBY_CREATELOBBY_TOOLTIP);
        miCreateLobby.addActionListener(e -> {
            final int n = JOptionPane.showConfirmDialog(TopMenu.this, pnlcreatelobby, CreateLobbyPanel.TITLE,
                    JOptionPane.OK_CANCEL_OPTION, 0, Icon.APPLICATION_ICON.getIcon());
            if (n == JOptionPane.YES_OPTION && this.user.isPresent()) {
                ControllerSingleton.getController().createLobby(this.user.get(), pnlcreatelobby.getGameSettings());
            }
        });
        lobbyMenu.add(miCreateLobby);

        this.add(userMenu);
        this.add(lobbyMenu);
    }

    /**
     * Set user to menu.
     * 
     * @param user
     *            the user to set
     */
    public void setUser(final User user) {
        this.user = Optional.ofNullable(user);
    }

}

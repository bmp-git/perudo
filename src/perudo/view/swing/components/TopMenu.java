package perudo.view.swing.components;

import java.awt.event.KeyEvent;
import java.util.Optional;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import perudo.model.User;
import perudo.view.swing.panels.menu.ChangeNamePanel;
import perudo.view.swing.panels.menu.CreateLobbyPanel;
import perudo.view.swing.utility.ControllerSingleton;
import perudo.view.swing.utility.GUIFactory;
import perudo.view.swing.utility.GUIFactorySingleton;
import perudo.view.swing.utility.Icon;

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
    private static final String MENU_PERUDO = "Perudo";
    private static final String MENU_USER_CHANGENAME = "Change user name";
    private static final String MENU_USER_CHANGENAME_TOOLTIP = "Change your user name..";
    private static final String MENU_LOBBY_CREATELOBBY = "Create lobby";
    private static final String MENU_LOBBY_CREATELOBBY_TOOLTIP = "Create a new lobby..";
    private static final String MENU_PERUDO_BACKTOMENU = "Back to menu";
    private static final String MENU_PERUDO_BACKTOMENU_TOOLTIP = "Back to main menu...";

    private Optional<User> user;
    private boolean returnMenu;

    /**
     * Initialize the menu with menu items.
     */
    public TopMenu() {
        super();
        this.user = Optional.empty();
        this.returnMenu = false;
        final GUIFactory factory = GUIFactorySingleton.getFactory();
        final CreateLobbyPanel pnlcreatelobby = new CreateLobbyPanel();
        final ChangeNamePanel pnlchangename = new ChangeNamePanel();

        final JMenu perudoMenu = (JMenu) factory.createMenu(MENU_PERUDO);
        perudoMenu.setMnemonic(KeyEvent.VK_P);
        final JMenuItem miBackMenu = (JMenuItem) factory.createMenuItem(MENU_PERUDO_BACKTOMENU, KeyEvent.VK_B,
                MENU_PERUDO_BACKTOMENU_TOOLTIP);
        miBackMenu.addActionListener(e -> {
            if (this.user.isPresent()) {
                this.returnMenu = true;
                ControllerSingleton.getController().closeNow(this.user.get());
            }
        });
        perudoMenu.add(miBackMenu);

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
            if (n == JOptionPane.YES_OPTION && this.user.isPresent() && pnlcreatelobby.getName().trim().length() > 0) {
                try {
                    ControllerSingleton.getController().createLobby(this.user.get(), pnlcreatelobby.getGameSettings());
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), ex.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        lobbyMenu.add(miCreateLobby);

        this.add(perudoMenu);
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

    /**
     * Return true if the user want to return to menu.
     * 
     * @return true if the user want to return to start menu, false otherwise
     */
    public boolean isReturnMenu() {
        return this.returnMenu;
    }

}

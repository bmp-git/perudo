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
import perudo.view.impl.GUIUtility;
import perudo.view.impl.StandardGUIFactory;
import perudo.view.impl.panels.CreateLobbyPanel;

public class TopMenu extends JMenuBar {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private static final String ICON_RESPATH = "/images/perudo-logo.png";
    private static final String MENU_USER = "User";
    private static final String MENU_LOBBY = "Lobby";
    private static final String MENU_USER_CHANGENAME = "Change user name";
    private static final String MENU_USER_CHANGENAME_TOOLTIP = "Change your user name..";
    private static final String CREATE_LOBBY_PANEL_TITLE = "Create new lobby";
    private static final String MENU_LOBBY_CREATELOBBY = "Create lobby";
    private static final String MENU_LOBBY_CREATELOBBY_TOOLTIP = "Create a new lobby..";

    private final GUIFactory factory;
    private final CreateLobbyPanel pnlcreatelobby;
    private Optional<User> user;
    
    public TopMenu() {
        super();
        this.factory = new StandardGUIFactory();
        this.user = Optional.empty();
        this.pnlcreatelobby = (CreateLobbyPanel) this.factory.createCreateLobbyPanel();
        JMenu userMenu = new JMenu(MENU_USER);
        userMenu.setMnemonic(KeyEvent.VK_U);
        JMenuItem miChangeName = new JMenuItem(MENU_USER_CHANGENAME);
        miChangeName.setMnemonic(KeyEvent.VK_C);
        miChangeName.setToolTipText(MENU_USER_CHANGENAME_TOOLTIP);
        miChangeName.addActionListener(e -> {
            //TODO implementation
        });
        userMenu.add(miChangeName);
        
        JMenu lobbyMenu = new JMenu(MENU_LOBBY);
        lobbyMenu.setMnemonic(KeyEvent.VK_L);
        JMenuItem miCreateLobby = new JMenuItem(MENU_LOBBY_CREATELOBBY);
        miCreateLobby.setMnemonic(KeyEvent.VK_C);
        miCreateLobby.setToolTipText(MENU_LOBBY_CREATELOBBY_TOOLTIP);
        miCreateLobby.addActionListener(e -> {
            int n = JOptionPane.showConfirmDialog(null, this.pnlcreatelobby, CREATE_LOBBY_PANEL_TITLE, JOptionPane.OK_CANCEL_OPTION,0,GUIUtility.getIcon(ICON_RESPATH));
            if(n == JOptionPane.YES_OPTION && this.user.isPresent()) {
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

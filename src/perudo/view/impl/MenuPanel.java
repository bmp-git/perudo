package perudo.view.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Optional;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import perudo.model.Lobby;
import perudo.model.User;
import perudo.view.GUIFactory;
import perudo.view.impl.components.TopMenu;
import perudo.view.impl.panels.LobbyInfoPanel;
import perudo.view.impl.panels.LobbyListPanel;
import perudo.view.impl.panels.LobbyPanel;
import perudo.view.impl.panels.MenuBottomPanel;
import perudo.view.impl.panels.UserListPanel;

/**
 * Models a JPanel made for manage lobbies and users.
 *
 */
public class MenuPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final int LOBBIES_LIST_ELEMENTS_BORDER_SIZE = 7;
    private final GUIFactory factory;

    private final TopMenu pnlTopMenu;
    private final MenuBottomPanel pnlBottomMenu;
    private final LobbyListPanel pnlLobbyList;
    private final UserListPanel pnlUserList;
    private final JPanel pnlCenter;

    private Optional<LobbyInfoPanel> pnlLobbyInfoActive;
    private Optional<User> user;

    /**
     * Create all menu sub panels.
     */
    public MenuPanel() {
        super();
        this.factory = GUIFactorySingleton.getFactory();
        this.setLayout(new BorderLayout());
        this.user = Optional.empty();
        this.pnlLobbyInfoActive = Optional.empty();
        this.pnlTopMenu = new TopMenu();
        this.pnlBottomMenu = new MenuBottomPanel();
        this.pnlLobbyList = new LobbyListPanel();
        this.pnlUserList = new UserListPanel();
        this.pnlCenter = this.factory.createPanel(new BorderLayout());
        this.pnlCenter.setBorder(new TitledBorder("Lobby Info"));

        this.add(pnlLobbyList, BorderLayout.WEST);
        this.add(pnlTopMenu, BorderLayout.NORTH);
        this.add(pnlCenter, BorderLayout.CENTER);
        this.add(pnlUserList, BorderLayout.EAST);
        this.add(pnlBottomMenu, BorderLayout.SOUTH);
    }

    /**
     * Set the user using the panel.
     * 
     * @param user
     *            the user to set
     */
    public void setUser(final User user) {
        this.user = Optional.ofNullable(user);
        this.pnlTopMenu.setUser(user);
        this.pnlBottomMenu.setUser(user);
        this.pnlLobbyList.setUser(user);
        this.pnlUserList.setUser(user);
        //need to update lobbies becouse user is changed
        ControllerSingleton.getController().getLobbies(this.user.get());
    }

    /**
     * Update all list in panel: lobbies, games and users.
     */
    public void updateAll() {
        if (this.user.isPresent()) {
            ControllerSingleton.getController().getUsers(this.user.get());
            ControllerSingleton.getController().getLobbies(this.user.get());
            ControllerSingleton.getController().getGames(this.user.get());
        }
    }

    private void showPanelCenter(final JPanel panel, final Object constraints) {
        this.pnlCenter.removeAll();
        this.pnlCenter.add(panel, constraints);
        this.pnlCenter.repaint();
        this.pnlCenter.revalidate();
    }

    /**
     * Add a lobby to the panel.
     * 
     * @param lobby
     *            the lobby to add
     */
    public void addLobby(final Lobby lobby) {
        final LobbyPanel p = new LobbyPanel(lobby);
        class ML implements MouseListener {
            @Override
            public void mouseClicked(final MouseEvent event) {
                pnlCenter.removeAll();
                pnlLobbyInfoActive = Optional
                        .of(new LobbyInfoPanel(((LobbyPanel) event.getSource()).getLobby(), MenuPanel.this.user.get()));
                pnlCenter.add(pnlLobbyInfoActive.get(), BorderLayout.CENTER);
                pnlCenter.repaint();
                pnlCenter.revalidate();
            }

            @Override
            public void mousePressed(final MouseEvent event) {
                pnlCenter.removeAll();
                pnlLobbyInfoActive = Optional
                        .of(new LobbyInfoPanel(((LobbyPanel) event.getSource()).getLobby(), MenuPanel.this.user.get()));
                pnlCenter.add(pnlLobbyInfoActive.get(), BorderLayout.CENTER);
                pnlCenter.repaint();
                pnlCenter.revalidate();
            }

            @Override
            public void mouseReleased(final MouseEvent event) {
            }

            @Override
            public void mouseEntered(final MouseEvent event) {
                ((LobbyPanel) event.getSource()).setBorder(factory.createBorder(Color.GRAY, LOBBIES_LIST_ELEMENTS_BORDER_SIZE));
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(final MouseEvent event) {
                ((LobbyPanel) event.getSource()).setBorder(factory.createBorder(Color.BLACK, LOBBIES_LIST_ELEMENTS_BORDER_SIZE));
                setCursor(Cursor.getDefaultCursor());
            }

        }
        p.addMouseListener(new ML());
        this.pnlLobbyList.addLobby(p);
        if (this.user.isPresent() && lobby.getOwner().equals(this.user.get())) {
            this.pnlLobbyInfoActive = Optional.of(new LobbyInfoPanel(lobby, this.user.get()));
            showPanelCenter(this.pnlLobbyInfoActive.get(), BorderLayout.CENTER);
        }
    }

    /**
     * Remove a lobby from the panel.
     * 
     * @param lobby
     *            the lobby to remove
     */
    public void removeLobby(final Lobby lobby) {
        if (this.pnlLobbyInfoActive.isPresent() && this.pnlLobbyInfoActive.get().getLobby().equals(lobby)) {
            this.pnlLobbyInfoActive = Optional.empty();
            this.pnlCenter.removeAll();
            this.pnlCenter.repaint();
            this.pnlCenter.revalidate();
        }
        this.pnlLobbyList.removeLobby(lobby);
    }

    /**
     * Update lobby in the panel, if not present get added.
     * 
     * @param lobby
     *            the lobby to update
     */
    public void updateLobby(final Lobby lobby) {
        if (this.pnlLobbyInfoActive.isPresent() && this.pnlLobbyInfoActive.get().getLobby().equals(lobby)) {
            this.pnlLobbyInfoActive = Optional.of(new LobbyInfoPanel(lobby, this.user.get()));
            showPanelCenter(this.pnlLobbyInfoActive.get(), BorderLayout.CENTER);
        }
        if (this.pnlLobbyList.getLobbies().contains(lobby)) {
            this.pnlLobbyList.updateLobby(lobby);
        } else {
            this.addLobby(lobby);
        }
    }

    /**
     * Add a user to the panel.
     * 
     * @param user
     *            the user to add
     */
    public void addUser(final User user) {
        this.pnlUserList.addUser(user);
    }

    /**
     * Remove a user from the panel.
     * 
     * @param user
     *            the user to remove
     */
    public void removeUser(final User user) {
        this.pnlUserList.removeUser(user);
    }

    /**
     * Update user in the panel, if not present get added.
     * 
     * @param user
     *            the user to update
     */
    public void updateUsers(final User user) {
        this.pnlUserList.updateUser(user);
    }
}

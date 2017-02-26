package perudo.view.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Optional;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import perudo.model.Lobby;
import perudo.model.User;
import perudo.utility.ErrorType;
import perudo.view.GUIFactory;
import perudo.view.impl.components.TopMenu;
import perudo.view.impl.panels.LobbyInfoPanel;
import perudo.view.impl.panels.LobbyListPanel;
import perudo.view.impl.panels.LobbyPanel;
import perudo.view.impl.panels.MenuBottomPanel;
import perudo.view.impl.panels.UserListPanel;

public class MenuPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final GUIFactory factory;

    private final TopMenu pnlTopMenu;
    private final MenuBottomPanel pnlBottomMenu;
    private final LobbyListPanel pnlLobbyList;
    private final UserListPanel pnlUserList;
    private final JPanel pnlCenter;

    private Optional<LobbyInfoPanel> pnlLobbyInfoActive;
    private Optional<User> user;

    public MenuPanel() {
        super();
        this.factory = new StandardGUIFactory();
        this.setLayout(new BorderLayout());
        this.user = Optional.empty();
        this.pnlLobbyInfoActive = Optional.empty();
        this.pnlTopMenu = (TopMenu) this.factory.createTopMenu();
        this.pnlBottomMenu = (MenuBottomPanel) this.factory.createMenuBottomPanel();
        this.pnlLobbyList = new LobbyListPanel();
        this.pnlUserList = new UserListPanel();
        this.pnlCenter = this.factory.createPanel();
        this.pnlCenter.setLayout(new BorderLayout());
        this.pnlCenter.setBorder(new TitledBorder("Lobby Info"));

        this.add(pnlLobbyList, BorderLayout.WEST);
        this.add(pnlTopMenu, BorderLayout.NORTH);
        this.add(pnlCenter, BorderLayout.CENTER);
        this.add(pnlUserList, BorderLayout.EAST);
        this.add(pnlBottomMenu, BorderLayout.SOUTH);
    }

    public void setUser(final User user) {
        this.user = Optional.of(user);
        this.pnlTopMenu.setUser(user);
        this.pnlBottomMenu.setUser(user);
        this.pnlLobbyList.setUser(user);
        this.pnlUserList.setUser(user);
        // this.updateUsers(user);
    }

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

    public void addLobby(final Lobby lobby) {
        LobbyPanel p = (LobbyPanel) this.factory.createLobbyPanel(lobby);
        class ML implements MouseListener {
            @Override
            public void mouseClicked(MouseEvent e) {
                pnlCenter.removeAll();
                pnlLobbyInfoActive = Optional
                        .of(new LobbyInfoPanel(((LobbyPanel) e.getSource()).getLobby(), MenuPanel.this.user.get()));
                pnlCenter.add(pnlLobbyInfoActive.get(), BorderLayout.CENTER);
                pnlCenter.repaint();
                pnlCenter.revalidate();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                pnlCenter.removeAll();
                pnlLobbyInfoActive = Optional
                        .of(new LobbyInfoPanel(((LobbyPanel) e.getSource()).getLobby(), MenuPanel.this.user.get()));
                pnlCenter.add(pnlLobbyInfoActive.get(), BorderLayout.CENTER);
                pnlCenter.repaint();
                pnlCenter.revalidate();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                ((LobbyPanel) e.getSource()).setBorder(factory.createBorder(Color.GRAY, 7));
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ((LobbyPanel) e.getSource()).setBorder(factory.createBorder(Color.BLACK, 7));
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

    public void removeLobby(final Lobby lobby) {
        if (this.pnlLobbyInfoActive.isPresent() && this.pnlLobbyInfoActive.get().getLobby().equals(lobby)) {
            this.pnlLobbyInfoActive = Optional.empty();
            this.pnlCenter.removeAll();
            this.pnlCenter.repaint();
            this.pnlCenter.revalidate();
        }
        this.pnlLobbyList.removeLobby(lobby);
    }

    public void updateLobby(final Lobby lobby) {
        if (this.pnlLobbyInfoActive.isPresent() && this.pnlLobbyInfoActive.get().getLobby().equals(lobby)) {
            this.pnlLobbyInfoActive = Optional.of(new LobbyInfoPanel(lobby, this.user.get()));
            showPanelCenter(this.pnlLobbyInfoActive.get(), BorderLayout.CENTER);
        }
        this.pnlLobbyList.updateLobby(lobby);
    }

    public void addUser(final User user) {
        this.pnlUserList.addUser(user);
    }

    public void removeUser(final User user) {
        this.pnlUserList.removeUser(user);
    }

    public void updateUsers(final User user) {
        this.pnlUserList.updateUser(user);
    }

    public void showError(final ErrorType errorType) {
        JOptionPane.showMessageDialog(this, errorType.getMessage(), "Error: " + errorType.getId(), JOptionPane.ERROR_MESSAGE);
    }
}

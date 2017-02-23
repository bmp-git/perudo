package perudo.view.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
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
import perudo.view.impl.panels.UserPanel;

public class MenuPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final GUIFactory factory;

    private JPanel pnlLobbyInfoExtern;

    private final TopMenu pnlTopMenu;
    private final MenuBottomPanel pnlBottomMenu;
    private final LobbyListPanel pnlLobbyList;
    private final UserListPanel pnlUserList;

    private Optional<LobbyInfoPanel> pnlLobbyInfoActive; // DA riguardare//
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

        createLobbyInfoPanel();

        this.add(pnlLobbyList, BorderLayout.WEST);
        this.add(pnlTopMenu, BorderLayout.NORTH);
        this.add(pnlLobbyInfoExtern, BorderLayout.CENTER);
        this.add(pnlUserList, BorderLayout.EAST);
        this.add(pnlBottomMenu, BorderLayout.SOUTH);
        if (this.user.isPresent()) {
            ControllerSingleton.getController().getUsers(this.user.get());
        }
    }

    public void setUser(User user) {
        this.user = Optional.of(user);
        this.pnlTopMenu.setUser(user);
        this.pnlBottomMenu.setUser(user);
        this.pnlLobbyList.setUser(user);
        ControllerSingleton.getController().getUsers(this.user.get());
        this.updateUsers(user);
    }

    private void createLobbyInfoPanel() {
        pnlLobbyInfoExtern = factory.createPanel();
        GridLayout gl = new GridLayout(1, 1);
        pnlLobbyInfoExtern.setLayout(gl);
        pnlLobbyInfoExtern.setBorder(new TitledBorder("Lobby Info"));
    }

    public void addLobby(Lobby lobby) {
        LobbyPanel p = (LobbyPanel) this.factory.createLobbyPanel(lobby);
        class ML implements MouseListener {

            @Override
            public void mouseClicked(MouseEvent e) {
                pnlLobbyInfoExtern.removeAll();
                pnlLobbyInfoActive = Optional
                        .of(new LobbyInfoPanel(((LobbyPanel) e.getSource()).getLobby(), MenuPanel.this.user.get()));
                pnlLobbyInfoExtern.add(pnlLobbyInfoActive.get());
                pnlLobbyInfoExtern.repaint();
                pnlLobbyInfoExtern.revalidate();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                pnlLobbyInfoExtern.removeAll();
                pnlLobbyInfoActive = Optional
                        .of(new LobbyInfoPanel(((LobbyPanel) e.getSource()).getLobby(), MenuPanel.this.user.get()));
                pnlLobbyInfoExtern.add(pnlLobbyInfoActive.get());
                pnlLobbyInfoExtern.repaint();
                pnlLobbyInfoExtern.revalidate();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                ((LobbyPanel) e.getSource()).setBorder(BorderFactory
                        .createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY), new EmptyBorder(7, 7, 7, 7)));
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ((LobbyPanel) e.getSource()).setBorder(BorderFactory
                        .createCompoundBorder(BorderFactory.createLineBorder(Color.WHITE), new EmptyBorder(7, 7, 7, 7)));
                setCursor(Cursor.getDefaultCursor());
            }

        }
        p.addMouseListener(new ML());
        this.pnlLobbyList.addLobby(p);
    }

    public void removeLobby(Lobby lobby) {
        this.pnlLobbyList.removeLobby(lobby);
    }

    public void updateLobby(Lobby lobby) {
        this.pnlLobbyList.updateLobby(lobby);
    }

    public void addUser(User user) {
        UserPanel p = (UserPanel) this.factory.createUserPanel(user);
        this.pnlUserList.addUser(p);
    }

    public void removeUser(User user) {
        this.pnlUserList.removeUser(user);
    }

    public void updateUsers(User user) {
        this.pnlUserList.updateUser(user);
    }

    public void showError(ErrorType errorType) {
        JOptionPane.showMessageDialog(this, errorType.getMessage(), "Error: " + errorType.getId(), JOptionPane.ERROR_MESSAGE);
    }
}

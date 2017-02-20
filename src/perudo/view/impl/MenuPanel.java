package perudo.view.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.TitledBorder;

import perudo.model.Lobby;
import perudo.model.User;
import perudo.view.GUIFactory;
import perudo.view.impl.components.TopMenu;
import perudo.view.impl.panels.LobbyInfoPanel;
import perudo.view.impl.panels.LobbyPanel;

public class MenuPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final GUIFactory factory;
    private JSplitPane splitPane;
    private JPanel lobbypanel;
    private JPanel lobbyinfo;
    private JPanel bottompanel;
    private JLabel lblname;
    private JLabel lblonline;
    private TopMenu pnlTopMenu;
    private Optional<User> user;

    public MenuPanel() {
        super();
        this.factory = new StandardGUIFactory();
        this.setLayout(new BorderLayout());
        this.user = Optional.empty();
        this.pnlTopMenu = (TopMenu) this.factory.createTopMenu();
        
        createLobbyListPanel();
        createLobbyInfoPanel();
        createBottomPanel();
        createSplitPanel();

        
        this.add(pnlTopMenu, BorderLayout.NORTH);
        this.add(splitPane, BorderLayout.CENTER);

    }

    public void setUser(User user) {
        this.user = Optional.of(user);
        this.pnlTopMenu.setUser(user);
        this.lblname.setText("Logged as "+this.user.get().getName());
        this.lblonline.setText("Online.");
    }
    
    private void createBottomPanel() {
        this.bottompanel = this.factory.createPanel();
        if (user.isPresent()) {
            this.lblname = (JLabel) this.factory.createLabel("Logged as " + user.get().getName());
            this.lblonline = (JLabel) this.factory.createLabel("Online.");
        } else {
            this.lblname = (JLabel) this.factory.createLabel("Not logged yet..");
            this.lblonline = (JLabel) this.factory.createLabel("Offline.");
        }
        this.bottompanel.setLayout(new BorderLayout());
        this.bottompanel.add(this.lblname, BorderLayout.LINE_START);
        this.bottompanel.add(this.lblonline, BorderLayout.LINE_END);
        this.add(this.bottompanel, BorderLayout.SOUTH);
    }

    private void createLobbyListPanel() {
        lobbypanel = factory.createPanel();
        GridLayout gl = new GridLayout(1, 1);
        lobbypanel.setLayout(gl);
        lobbypanel.setBorder(new TitledBorder("Lobbies list"));

    }

    private void createLobbyInfoPanel() {
        lobbyinfo = factory.createPanel();
        GridLayout gl = new GridLayout(1, 1);
        lobbyinfo.setLayout(gl);
        lobbyinfo.setBorder(new TitledBorder("Lobby Info"));
    }

    private void createSplitPanel() {
        splitPane = (JSplitPane) factory.createVerticalSplitPane();
        final JScrollPane scroll = (JScrollPane) factory.createVerticalScrollPanel();
        scroll.getViewport().add(lobbypanel);
        scroll.setPreferredSize(getPreferredSize());
        splitPane.setLeftComponent(scroll);
        splitPane.setRightComponent(lobbyinfo);
    }

    public void addLobby(Lobby lobby) {
        LobbyPanel p = new LobbyPanel(lobby);
        class ML implements MouseListener {

            @Override
            public void mouseClicked(MouseEvent e) {
                lobbyinfo.removeAll();
                lobbyinfo.add(new LobbyInfoPanel(((LobbyPanel) e.getSource()).getLobby(), MenuPanel.this.user.get()));
                lobbyinfo.repaint();
                lobbyinfo.revalidate();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                lobbyinfo.removeAll();
                lobbyinfo.add(new LobbyInfoPanel(((LobbyPanel) e.getSource()).getLobby(), MenuPanel.this.user.get()));
                lobbyinfo.repaint();
                lobbyinfo.revalidate();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                ((LobbyPanel) e.getSource()).setBorder(BorderFactory.createLineBorder(Color.GRAY));
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ((LobbyPanel) e.getSource()).setBorder(BorderFactory.createLineBorder(Color.WHITE));
                setCursor(Cursor.getDefaultCursor());
            }

        }
        p.addMouseListener(new ML());
        ((GridLayout) lobbypanel.getLayout()).setRows(this.lobbypanel.getComponentCount() + 1);
        this.lobbypanel.add(p);
    }

    public void removeLobby(Lobby lobby) {

        for (int i = 0; i < this.lobbypanel.getComponentCount(); i++) {
            if (((LobbyPanel) this.lobbypanel.getComponent(i)).getLobby().equals(lobby)) {
                this.lobbypanel.remove(this.lobbypanel.getComponent(i));
                break;
            }
        }

    }

    public void updateLobby(Lobby lobby) {
        for (int i = 0; i < this.lobbypanel.getComponentCount(); i++) {
            if (((LobbyPanel) this.lobbypanel.getComponent(i)).getLobby().equals(lobby)) {
                ((LobbyPanel) this.lobbypanel.getComponent(i)).setLobby(lobby);
            }
        }

        if (lobbyinfo.getComponentCount() > 0) {
            ((LobbyInfoPanel) lobbyinfo.getComponent(0)).setLobby(lobby);
        }
    }
}

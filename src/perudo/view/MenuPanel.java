package perudo.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.TitledBorder;

import perudo.model.Lobby;
import perudo.model.User;
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

    private User user;
    
    
    public MenuPanel() {
        this.factory = new StandardGUIFactory();
        this.setLayout(new BorderLayout());
        
        createLobbyListPanel();
        createLobbyInfoPanel();
        createSplitPanel();

        this.add(splitPane, BorderLayout.CENTER);
        
        setUser(null);
    }

    private void setUser(User user){
        this.user = user;
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

        splitPane.setLeftComponent(scroll);
        splitPane.setRightComponent(lobbyinfo);
    }
    
    public void addLobby(Lobby lobby) {
        LobbyPanel p = new LobbyPanel(lobby);
        class ML implements MouseListener{

            @Override
            public void mouseClicked(MouseEvent e) {
                lobbyinfo.removeAll();
                lobbyinfo.add(new LobbyInfoPanel(((LobbyPanel)e.getSource()).getLobby()));   
                lobbyinfo.repaint();
                lobbyinfo.revalidate();  
            }

            @Override
            public void mousePressed(MouseEvent e) {                
            }

            @Override
            public void mouseReleased(MouseEvent e) {              
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                ((LobbyPanel)e.getSource()).setBorder(BorderFactory.createLineBorder(Color.black));
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ((LobbyPanel)e.getSource()).setBorder(BorderFactory.createLineBorder(Color.WHITE));
                setCursor(Cursor.getDefaultCursor());
            }
            
        }
        p.addMouseListener(new ML());
        ((GridLayout)lobbypanel.getLayout()).setRows(this.lobbypanel.getComponentCount() + 1);
        this.lobbypanel.add(p);
    }
    public void removeLobby(Lobby lobby) {
        
        for(int i = 0;i<this.lobbypanel.getComponentCount();i++){
            if(((LobbyPanel)this.lobbypanel.getComponent(i)).getLobby().equals(lobby)){
                this.lobbypanel.remove(this.lobbypanel.getComponent(i));
                break;
            }
        }
        
    }

    public void updateLobby(Lobby lobby) {
        for(int i = 0;i<this.lobbypanel.getComponentCount();i++){
            if(((LobbyPanel)this.lobbypanel.getComponent(i)).getLobby().equals(lobby)){
                ((LobbyPanel)this.lobbypanel.getComponent(i)).setLobby(lobby);
            }
        }
        
        if(lobbyinfo.getComponentCount() > 0){
            ((LobbyInfoPanel)lobbyinfo.getComponent(0)).setLobby(lobby);
        }
    }
}

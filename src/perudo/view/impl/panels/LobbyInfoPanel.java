package perudo.view.impl.panels;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import perudo.model.Lobby;
import perudo.model.User;
import perudo.view.GUIFactory;
import perudo.view.impl.StandardGUIFactory;

public class LobbyInfoPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String ENTER_LOBBY = "Enter lobby";
    private static final String EXIT_LOBBY = "Exit lobby";
    private static final String START_LOBBY = "Start lobby";
    private static final int MINUMUM_PLAYERS = 2;
    
    private final GUIFactory factory;
    private Lobby lobby;
    
    private final JButton btnEnterLobby;
    private final JButton btnExitLobby;
    private final JButton btnStartLobby;

    public LobbyInfoPanel(Lobby lobby, User user) {
        this.factory = new StandardGUIFactory();
        this.lobby = lobby;
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.add(Box.createRigidArea(new Dimension(0,10)));
        if(this.lobby.getOwner().equals(user)) {
            this.add(this.factory.createLabel("YOU ARE THE OWNER"),Color.BLUE);
        }
        this.add(Box.createRigidArea(new Dimension(0,10)));
        this.add(factory.createLabel(("Name: "+this.lobby.getInfo().getName())));
        this.add(Box.createRigidArea(new Dimension(0,10)));
        this.add(factory.createLabel(("Owner: "+this.lobby.getOwner().getName())));
        this.add(Box.createRigidArea(new Dimension(0,10)));
        this.add(factory.createLabel(("Players: ")));
        this.lobby.getUsers().forEach(u -> this.add(factory.createLabel(u.getName())));
        this.add(Box.createRigidArea(new Dimension(0,10)));
        this.add(factory.createLabel(this.lobby.getInfo().toString()));
        this.add(Box.createRigidArea(new Dimension(0,10)));
        this.btnEnterLobby = (JButton)factory.createButton(ENTER_LOBBY);
        this.add(this.btnEnterLobby);
        this.add(Box.createRigidArea(new Dimension(0,10)));
        this.btnExitLobby = (JButton)factory.createButton(EXIT_LOBBY);
        this.add(this.btnExitLobby);
        this.add(Box.createRigidArea(new Dimension(0,10)));
        this.btnStartLobby = (JButton)factory.createButton(START_LOBBY);
        this.add(this.btnStartLobby);
        if(this.lobby.getOwner().equals(user) && this.lobby.getUsers().size() >= MINUMUM_PLAYERS) {
            this.btnStartLobby.setVisible(true);
        } else {
            this.btnStartLobby.setVisible(false);
        }
        if (this.lobby.getUsers().contains(user)) {
            this.btnExitLobby.setVisible(true);
            this.btnEnterLobby.setVisible(false);
        } else {
            this.btnExitLobby.setVisible(false);
            this.btnEnterLobby.setVisible(true);
        }
    }
    
    public Lobby getLobby(){
        return this.lobby;
    }

    public void setLobby(Lobby lobby){
        this.lobby = lobby;
    }
}

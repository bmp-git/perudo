package perudo.view.impl.panels;



import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import perudo.model.Lobby;
import perudo.view.GUIFactory;
import perudo.view.impl.StandardGUIFactory;

public class LobbyPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final GUIFactory factory;
    private Lobby lobby;
    private JLabel lblLobby;
    private JLabel lblPlayers;

    public LobbyPanel(Lobby lobby) {
        this.lobby = lobby;
        this.factory = new StandardGUIFactory();
        this.setLayout(new GridLayout(1,2));
        this.lblLobby = (JLabel) this.factory.createLabel("Lobby: "+(this.lobby.getInfo().getName()));
        int players = this.lobby.getInfo().getMaxPlayer() - this.lobby.getFreeSpace();
        this.lblPlayers = (JLabel) this.factory.createLabel("Players: "+ players+"/"+this.lobby.getInfo().getMaxPlayer());

        this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        this.add(this.lblLobby); 
        this.add(this.lblPlayers);

    }
    
    public Lobby getLobby(){
        return this.lobby;
    }
    
    public void setLobby(Lobby lobby){
        this.lobby = lobby;
        this.lblLobby.setText("Lobby: "+(this.lobby.getInfo().getName()));
        int players = this.lobby.getInfo().getMaxPlayer() - this.lobby.getFreeSpace();
        this.lblPlayers.setText("Players: "+ players+"/"+this.lobby.getInfo().getMaxPlayer());
    }
}

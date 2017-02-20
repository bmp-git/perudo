package perudo.view.impl.panels;



import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
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
    

    public LobbyPanel(Lobby lobby) {
        this.lobby = lobby;
        this.factory = new StandardGUIFactory();
        this.setLayout(new GridLayout(1,2));
        this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        this.add(factory.createLabel("Lobby: "+(this.lobby.getInfo().getName())));
        
        int players = this.lobby.getInfo().getMaxPlayer() - this.lobby.getFreeSpace();
        this.add(factory.createLabel("Players: "+ players+"/"+this.lobby.getInfo().getMaxPlayer()));

    }
    
    public Lobby getLobby(){
        return this.lobby;
    }
    
    public void setLobby(Lobby lobby){
        this.lobby = lobby;
    }
}

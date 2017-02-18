package perudo.view.impl.panels;

import java.awt.GridLayout;

import javax.swing.JPanel;

import perudo.model.Lobby;
import perudo.view.GUIFactory;
import perudo.view.StandardGUIFactory;

public class LobbyInfoPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final GUIFactory factory;
    private Lobby lobby;
    

    public LobbyInfoPanel(Lobby lobby) {
        this.factory = new StandardGUIFactory();
        this.lobby = lobby;
        this.setLayout(new GridLayout(10,1));
        this.add(factory.createLabel(("Name: "+this.lobby.getInfo().getName())));
        this.add(factory.createLabel(("Owner: "+this.lobby.getOwner().getName())));

        
    }
    
    public Lobby getLobby(){
        return this.lobby;
    }

    public void setLobby(Lobby lobby){
        this.lobby = lobby;
    }
}

package perudo.view.impl.panels;

import java.awt.Font;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import perudo.model.Game;
import perudo.model.User;
import perudo.view.GUIFactory;
import perudo.view.impl.StandardGUIFactory;

public class GameTurnPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private final GUIFactory factory;
    private final JLabel lblTurn;
    
    public GameTurnPanel(Game game, User myUser) {
        super();
        this.setLayout(new GridBagLayout());
        this.factory = new StandardGUIFactory();
        this.lblTurn = (JLabel) this.factory.createLabel(game.getTurn().equals(myUser) ? "It's your turn, make a play." : "It's "+game.getTurn().getName()+" turn.");
        this.lblTurn.setFont(new Font("Consolas", Font.PLAIN, 30));

        this.add(this.lblTurn);
    }
    
    public void setTurnPanel(Game game, User myUser) {
        this.lblTurn.setText(game.getTurn().equals(myUser) ? "It's your turn, make a play." : "It's "+game.getTurn().getName()+" turn.");
    }
    
}

package perudo.view.impl.panels;

import java.awt.FlowLayout;
import javax.swing.JPanel;
import perudo.model.Game;
import perudo.model.User;
import perudo.view.GUIFactory;
import perudo.view.impl.StandardGUIFactory;
import perudo.view.impl.components.DiceLabel;

public class PlayerHandPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private static final int DICE_SIZE = 64;

	private final GUIFactory factory;
	private final Game game;
	private final User user;
	
	public PlayerHandPanel(Game game, User user) {
		super();
		this.factory = new StandardGUIFactory();
		this.game = game;
		this.user = user;
		this.setLayout(new FlowLayout());
		this.game.getUserStatus(this.user).getDiceCount().entrySet().forEach(d -> addDice(d.getKey(),d.getValue()));
	}
	
	private void addDice(int dice, int value) {
		for(int i = 0; i < value; i++) {
			this.add(new DiceLabel(dice,DICE_SIZE));
		}
	}
	
	public void setGame(Game game) {
		this.removeAll();
		this.game.getUserStatus(this.user).getDiceCount().entrySet().forEach(d -> addDice(d.getKey(),d.getValue()));
	}

}

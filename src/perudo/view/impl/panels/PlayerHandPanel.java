package perudo.view.impl.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JLabel;
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
    private final JPanel pnlDice;

    public PlayerHandPanel(final Game game, final User user, final boolean myUser) {
        super();
        this.factory = new StandardGUIFactory();
        this.game = game;
        this.user = user;
        this.setLayout(new BorderLayout());
        this.pnlDice = this.factory.createPanel(new FlowLayout());
        this.setBorder(myUser ? this.factory.createBorder(Color.BLUE, 7) : this.factory.createBorder(Color.BLACK, 7));
        this.game.getUserStatus(this.user).getDiceCount().entrySet().forEach(d -> addDice(d.getKey(), d.getValue()));
        JLabel name = myUser ? (JLabel) this.factory.createLabel(this.user.getName(), Color.GREEN)
                : (JLabel) this.factory.createLabel(this.user.getName());
        this.add(name, BorderLayout.NORTH);
        this.add(pnlDice, BorderLayout.CENTER);
    }

    private void addDice(final int dice, final int value) {
        for (int i = 0; i < value; i++) {
            this.pnlDice.add(new DiceLabel(dice, DICE_SIZE));
        }
    }

    public void setGame(final Game game) {
        this.removeAll();
        this.game.getUserStatus(this.user).getDiceCount().entrySet().forEach(d -> addDice(d.getKey(), d.getValue()));
    }

    public User getUser() {
        return this.user;
    }

}

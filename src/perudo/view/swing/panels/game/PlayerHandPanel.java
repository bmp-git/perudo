package perudo.view.swing.panels.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import perudo.model.Game;
import perudo.model.User;
import perudo.view.swing.components.DiceLabel;
import perudo.view.swing.utility.GUIFactory;
import perudo.view.swing.utility.GUIFactorySingleton;
import perudo.view.swing.utility.GUIUtility;
import perudo.view.swing.utility.Icon;

/**
 * Panel representing the hand of a player.
 */
public class PlayerHandPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final int DICE_SIZE = 64;
    private static final int BORDER_SIZE = 7;

    private final Game game;
    private final User user;
    private final JPanel pnlDice;
    private final boolean myUser;

    /**
     * Initialize the hand with the needed parameters.
     * 
     * @param game
     *            the current game
     * @param user
     *            the user playing
     * @param myUser
     *            true if the user is the user using the panel, false otherwise
     */
    public PlayerHandPanel(final Game game, final User user, final boolean myUser) {
        super();
        final GUIFactory factory = GUIFactorySingleton.getFactory();
        this.game = game;
        this.user = user;
        this.myUser = myUser;
        this.setLayout(new BorderLayout());
        this.pnlDice = factory.createPanel(new FlowLayout());
        this.setBorder(myUser ? factory.createBorder(Color.BLUE, BORDER_SIZE) : factory.createBorder(Color.BLACK, BORDER_SIZE));
        if (this.myUser) {
            this.game.getUserStatus(this.user).getDiceCount().entrySet().forEach(d -> addDice(d.getKey(), d.getValue()));
        } else {
            this.game.getUserStatus(this.user).getDiceCount().entrySet().forEach(d -> addInvisibleDice(d.getValue()));
        }
        final JLabel name = myUser ? (JLabel) factory.createLabel(this.user.getName(), Color.GREEN)
                : (JLabel) factory.createLabel(this.user.getName());
        this.add(name, BorderLayout.NORTH);
        this.add(pnlDice, BorderLayout.CENTER);
    }

    private void addDice(final int dice, final int value) {
        for (int i = 0; i < value; i++) {
            final DiceLabel lbl = new DiceLabel();
            lbl.setValue(dice, DICE_SIZE);
            this.pnlDice.add(lbl);
        }
    }

    private void addInvisibleDice(final int value) {
        for (int i = 0; i < value; i++) {
            final JLabel lbl = new JLabel(new ImageIcon(GUIUtility.getScaledImage(
                    new ImageIcon(Icon.class.getResource("/images/dices/di.jpg")).getImage(), DICE_SIZE, DICE_SIZE)));
            this.pnlDice.add(lbl);
        }
    }

    /**
     * Set the current game to modify hand.
     * 
     * @param game
     *            the current game
     */
    public void setGame(final Game game) {
        this.removeAll();
        if (this.myUser) {
            this.game.getUserStatus(this.user).getDiceCount().entrySet().forEach(d -> addDice(d.getKey(), d.getValue()));
        } else {
            this.game.getUserStatus(this.user).getDiceCount().entrySet().forEach(d -> addInvisibleDice(d.getValue()));
        }
    }
}

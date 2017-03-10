package perudo.view.impl.panels;

import java.awt.Font;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import perudo.model.Game;
import perudo.model.User;
import perudo.view.GUIFactory;
import perudo.view.impl.GUIFactorySingleton;

/**
 * Panel rappresenting the current turn in game.
 */
public class GameTurnPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final int FONT_SIZE = 30;

    private final JLabel lblTurn;

    /**
     * Initialize a panel with empty content.
     */
    public GameTurnPanel() {
        super();
        this.setLayout(new GridBagLayout());
        final GUIFactory factory = GUIFactorySingleton.getFactory();
        this.lblTurn = (JLabel) factory.createLabel("");
        this.lblTurn.setFont(new Font("Consolas", Font.PLAIN, FONT_SIZE));

        this.add(this.lblTurn);
    }

    /**
     * Set the current game and the user using the panel.
     * 
     * @param game
     *            the current game
     * @param myUser
     *            the user using the panel
     */
    public void setTurnPanel(final Game game, final User myUser) {
        this.lblTurn.setText(
                game.getTurn().equals(myUser) ? "It's your turn, make a play." : "It's " + game.getTurn().getName() + " turn.");
    }

    /**
     * Set the panel text when game end.
     * 
     * @param win
     *            true if the user using panel wins, false otherwise
     */
    public void youWin(final boolean win) {
        this.lblTurn.setText(win ? "Congratulations, you win!" : "You lose");
    }
}

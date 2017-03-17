package perudo.view.swing.panels.game;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JPanel;
import perudo.model.Game;
import perudo.model.User;
import perudo.view.swing.components.DiceLabel;
import perudo.view.swing.utility.GUIFactory;
import perudo.view.swing.utility.GUIFactorySingleton;

/**
 * Panel representing a bid summary after a play.
 */
public class TurnBidSummary extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final int DICE_SIZE = 16;

    private final GUIFactory factory;

    /**
     * Initialize and create the bid summary panel.
     * 
     * @param game
     *            the game to set in summary
     * @param user
     *            the user bidding
     * @param play
     *            the play made " doubt " or " urge "
     * @param win
     *            true if user won
     */
    public TurnBidSummary(final Game game, final User user, final String play, final boolean win) {
        super();
        this.factory = GUIFactorySingleton.getFactory();
        this.setLayout(new GridBagLayout());
        final GridBagConstraints cnst = new GridBagConstraints();
        cnst.gridy = 0;
        cnst.gridx = 0;
        cnst.anchor = GridBagConstraints.WEST;
        cnst.insets = new Insets(0, 0, 0, 0);
        final JPanel pnl = this.factory.createPanel(new FlowLayout());
        pnl.add(factory.createLabel(user.getName() + play + (win ? "and win" : "and lose")));
        this.add(pnl, cnst);
        cnst.gridy++;
        game.getUsers().forEach(u -> {
            this.add(getHand(u, game), cnst);
            cnst.gridy++;
        });
    }

    private JPanel getHand(final User user, final Game game) {
        final JPanel pnl = this.factory.createPanel(new FlowLayout());
        pnl.add(this.factory.createLabel(user.getName()));
        game.getUserStatus(user).getDiceCount().entrySet().forEach(e -> {
            for (int i = 0; i < e.getValue(); i++) {
                final DiceLabel lbl = new DiceLabel();
                lbl.setValue(e.getKey(), DICE_SIZE);
                if (game.isDiceInBid(e.getKey(), user)) {
                    lbl.setBorder(this.factory.createBorder(Color.YELLOW, 0));
                }
                pnl.add(lbl);
            }
        });
        return pnl;
    }
}

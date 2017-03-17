package perudo.view.swing.panels.game;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import perudo.model.Game;
import perudo.model.User;
import perudo.view.swing.components.DiceLabel;
import perudo.view.swing.utility.GUIFactory;
import perudo.view.swing.utility.GUIFactorySingleton;

/**
 * Panel representing the history of the game.
 */
public class HistoryPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final int DICE_SIZE = 16;

    private final GridBagConstraints cnst;
    private final GUIFactory factory;
    private final JPanel pnlHistory;

    /**
     * Initialize the history panel.
     */
    public HistoryPanel() {
        super();
        this.setLayout(new FlowLayout());
        this.factory = GUIFactorySingleton.getFactory();
        this.pnlHistory = this.factory.createPanel(new GridBagLayout());
        this.setBorder(new TitledBorder("History"));
        this.cnst = new GridBagConstraints();
        this.cnst.gridy = 0;
        this.cnst.gridx = 0;
        this.cnst.anchor = GridBagConstraints.WEST;
        this.cnst.insets = new Insets(0, 0, 0, 0);
        final JPanel pnl = this.factory.createPanel(new FlowLayout());
        pnl.add(factory.createLabel("Game started!"));
        this.pnlHistory.add(pnl, this.cnst);
        this.cnst.gridy++;
        this.add(this.pnlHistory);
    }

    /**
     * Notify a play.
     * 
     * @param game
     *            the updated game
     * @param user
     *            the user who has played
     */
    public void playNotify(final Game game, final User user) {
        final DiceLabel lbldice = new DiceLabel();
        lbldice.setValue(game.getCurrentBid().get().getDiceValue(), DICE_SIZE);
        final JPanel pnl = this.factory.createPanel(new FlowLayout());
        pnl.add(this.factory.createLabel(user.getName() + " plays " + game.getCurrentBid().get().getQuantity()));
        pnl.add(lbldice);
        this.pnlHistory.add(pnl, this.cnst);
        this.cnst.gridy++;
    }

    /**
     * Notify a doubt.
     * 
     * @param game
     *            the updated game
     * @param user
     *            the user who has doubted
     * @param win
     *            the doubt result
     */
    public void doubtNotify(final Game game, final User user, final boolean win) {
        this.pnlHistory.add(new TurnBidSummary(game.getLastRound().get(), user, " doubts ", win), cnst);
        this.cnst.gridy++;
    }

    /**
     * Notify a urge.
     * 
     * @param game
     *            the updated game
     * @param user
     *            the user who has urged
     * @param win
     *            the urge result
     */
    public void urgeNotify(final Game game, final User user, final boolean win) {
        this.pnlHistory.add(new TurnBidSummary(game.getLastRound().get(), user, " urged ", win), cnst);
        this.cnst.gridy++;
    }

    /**
     * Notify a palifico call.
     * 
     * @param game
     *            the updated game
     * @param user
     *            the user who has called palifico
     */
    public void callPalificoNotify(final Game game, final User user) {
        final JPanel pnl = this.factory.createPanel(new FlowLayout());
        pnl.add(this.factory.createLabel(user.getName() + " called palifico"));
        this.pnlHistory.add(pnl, this.cnst);
        this.cnst.gridy++;
    }

    /**
     * Notify a exit.
     * 
     * @param game
     *            the updated game
     * @param user
     *            the user who exited
     */
    public void exitGameNotify(final Game game, final User user) {
        final JPanel pnl = this.factory.createPanel(new FlowLayout());
        pnl.add(this.factory.createLabel(user.getName() + " exited"));
        this.pnlHistory.add(pnl, this.cnst);
        this.cnst.gridy++;
    }

    /**
     * Notify the game end.
     * 
     * @param game
     *            the updated game
     */
    public void gameEndedNotify(final Game game) {
        final JPanel pnl = this.factory.createPanel(new FlowLayout());
        pnl.add(this.factory.createLabel("Game finished"));
        this.pnlHistory.add(pnl, this.cnst);
        this.cnst.gridy++;
    }

    /**
     * Get the number of elements inside history panel.
     * 
     * @return the number of elements
     */
    public int getElements() {
        return this.pnlHistory.getComponentCount();
    }
}

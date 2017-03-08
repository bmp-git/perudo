package perudo.view.impl.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JPanel;
import perudo.model.Game;
import perudo.model.User;

/**
 * Panel rappresenting all players hands in game.
 */
public class PlayersHandsListPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final int TOP_BOT_INSETS = 5;

    private Game game;
    private User user;
    private final GridBagConstraints cnst;

    /**
     * Initialize an empty list of hands.
     */
    public PlayersHandsListPanel() {
        super();
        this.setLayout(new GridBagLayout());
        this.cnst = new GridBagConstraints();
        this.cnst.gridy = 0;
        this.cnst.insets = new Insets(TOP_BOT_INSETS, 0, TOP_BOT_INSETS, 0);
    }

    /**
     * Add the hand of specified user.
     * 
     * @param user
     *            the user
     */
    public void addHand(final User user) {
        if (this.game.getUsers().contains(user)) {
            this.add(new PlayerHandPanel(this.game, user, this.user.equals(user)), cnst);
            this.cnst.gridy++;
        }
    }

    /**
     * Set the current game and the user using the panel.
     * 
     * @param game
     *            the current game
     * @param user
     *            the user using panel
     */
    public void setGame(final Game game, final User user) {
        this.user = user;
        this.game = game;
        this.removeAll();
        this.addHand(this.game.getUsers().stream().filter(u -> this.user.equals(u)).findAny().get());
        this.game.getUsers().stream().filter(u -> !this.user.equals(u) && game.getUserStatus(u).getRemainingDice() > 0)
                .forEach(u -> this.addHand(u));
    }
}

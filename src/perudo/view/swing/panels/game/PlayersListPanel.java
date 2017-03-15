package perudo.view.swing.panels.game;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Optional;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import perudo.model.Game;
import perudo.model.User;
import perudo.view.swing.utility.GUIFactory;
import perudo.view.swing.utility.GUIFactorySingleton;

/**
 * Panel representing a list of users.
 */
public class PlayersListPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String PANEL_TITLE = "Player turn";

    private final GridBagConstraints cnst;
    private final JPanel pnlUserList;
    private Optional<Game> game;

    /**
     * Initialize an empty list panel.
     */
    public PlayersListPanel() {
        this.setLayout(new FlowLayout());
        this.game = Optional.empty();
        final GUIFactory factory = GUIFactorySingleton.getFactory();
        this.pnlUserList = factory.createPanel(new GridBagLayout());
        this.setBorder(new TitledBorder(PANEL_TITLE));
        this.cnst = new GridBagConstraints();
        this.cnst.gridy = 0;
        this.cnst.insets = new Insets(0, 0, 0, 0);
        this.add(factory.createScrollPaneWithoutBorder(this.pnlUserList));
    }

    /**
     * Set the game in the panel.
     * 
     * @param game
     *            the game
     */
    public void setGame(final Game game) {
        this.game = Optional.ofNullable(game);
        this.pnlUserList.removeAll();
        this.game.get().getUsers().forEach(u -> addUser(u));

    }

    /**
     * Add a user to the list panel.
     * 
     * @param user
     *            the user to add
     */
    public void addUser(final User user) {
        if (this.game.isPresent() && !this.game.get().hasLost(user)) {
            final PlayerPanel p = new PlayerPanel(user, this.game.get().getTurn().equals(user));
            this.pnlUserList.add(p, this.cnst);
            this.cnst.gridy++;
        }
    }
}

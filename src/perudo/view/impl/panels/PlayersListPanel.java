package perudo.view.impl.panels;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Optional;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import perudo.model.Game;
import perudo.model.User;
import perudo.view.GUIFactory;
import perudo.view.impl.GUIFactorySingleton;

/**
 * Panel rappresenting a list of users.
 */
public class PlayersListPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

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
        this.setBorder(new TitledBorder("Player turn"));
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
        this.game.get().getUsers().forEach(u -> updateUser(u));

    }

    /**
     * Add a user to the list panel.
     * 
     * @param user
     *            the user to add
     */
    public void addUser(final User user) {
        final PlayerPanel p = new PlayerPanel(user, this.game.get().getTurn().equals(user));
        this.pnlUserList.add(p, this.cnst);
        this.cnst.gridy++;
    }

    /**
     * Update a user in the list panel.
     * 
     * @param user
     *            the user to update
     */
    public void updateUser(final User user) {
        boolean inserted = false;
        for (int i = 0; i < this.pnlUserList.getComponentCount() && !inserted; i++) {
            if (((PlayerPanel) this.pnlUserList.getComponent(i)).getUser().equals(user)) {
                inserted = true;
                ((PlayerPanel) this.pnlUserList.getComponent(i)).setUser(user, this.game.get().getTurn().equals(user));
            }
        }
        if (!inserted) {
            this.addUser(user);
        }
    }

    /**
     * Remove a user to the list panel.
     * 
     * @param user
     *            the user to remove
     */
    public void removeUser(final User user) {
        for (int i = 0; i < this.pnlUserList.getComponentCount(); i++) {
            if (((PlayerPanel) this.pnlUserList.getComponent(i)).getUser().equals(user)) {
                this.pnlUserList.remove(this.pnlUserList.getComponent(i));
                break;
            }
        }
    }
}

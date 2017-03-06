package perudo.view.impl.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JPanel;
import perudo.model.Game;
import perudo.model.User;
import perudo.view.GUIFactory;
import perudo.view.impl.StandardGUIFactory;

public class PlayersHandsListPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final GUIFactory factory;
    private Game game;
    private User user;
    private final GridBagConstraints cnst;

    public PlayersHandsListPanel(final Game game, final User user) {
        super();
        this.factory = new StandardGUIFactory();
        this.game = game;
        this.user = user;
        this.setLayout(new GridBagLayout());
        this.cnst = new GridBagConstraints();
        this.cnst.gridy = 0;
        this.cnst.insets = new Insets(5, 0, 5, 0);
        this.addHand(this.game.getUsers().stream().findAny().get());
        this.game.getUsers().stream().filter(u -> !this.user.equals(u)).forEach(u -> this.addHand(u));
    }

    public void addHand(final User user) {
        if (this.game.getUsers().contains(user)) {
            this.add(new PlayerHandPanel(this.game, user, this.user.equals(user)), cnst);
            this.cnst.gridy++;
        }
    }

    public void setGame(final Game game) {
        this.game = game;
        this.removeAll();
        this.addHand(this.game.getUsers().stream().findAny().get());
        this.game.getUsers().stream().filter(u -> !this.user.equals(u) && game.getUserStatus(u).getRemainingDice() > 0).forEach(u -> this.addHand(u));
    }
}

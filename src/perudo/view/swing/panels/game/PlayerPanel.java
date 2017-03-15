package perudo.view.swing.panels.game;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import perudo.model.User;
import perudo.view.swing.utility.GUIFactory;
import perudo.view.swing.utility.GUIFactorySingleton;

/**
 * Panel representing a a player in game.
 */
public class PlayerPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final int LEFT_RIGHT_PADDING = 5;

    private User user;
    private final JLabel lblUser;

    /**
     * Initialize the panel with the user.
     * 
     * @param user
     *            the user to fit in panel
     * @param userInTurn
     *            true if it is the turn of the user to fit, false otherwise
     */
    public PlayerPanel(final User user, final boolean userInTurn) {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        final GUIFactory factory = GUIFactorySingleton.getFactory();
        this.user = user;
        this.lblUser = userInTurn ? (JLabel) factory.createLabel(this.user.getName(), Color.GREEN)
                : (JLabel) factory.createLabel(this.user.getName());
        this.add(Box.createRigidArea(new Dimension(0, 10)));
        this.add(this.lblUser);
        this.add(Box.createRigidArea(new Dimension(0, 10)));
        this.setBorder(new EmptyBorder(0, LEFT_RIGHT_PADDING, 0, LEFT_RIGHT_PADDING));

    }

    /**
     * Set the user to set in panel.
     * 
     * @param user
     *            the user to set in panel
     * @param userInTurn
     *            true if it is the turn of the user to fit, false otherwise
     */
    public void setUser(final User user, final boolean userInTurn) {
        this.user = user;
        this.lblUser.setText(this.user.getName());
        this.lblUser.setForeground(userInTurn ? Color.GREEN : Color.BLACK);
        this.lblUser.repaint();
    }

    /**
     * Get the user from panel.
     * 
     * @return the user of panel
     */
    public User getUser() {
        return this.user;
    }

}

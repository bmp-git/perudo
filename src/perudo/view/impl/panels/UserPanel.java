package perudo.view.impl.panels;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import perudo.model.User;
import perudo.view.GUIFactory;
import perudo.view.impl.StandardGUIFactory;

/**
 * Panel rappresenting a user.
 */
public class UserPanel extends JPanel {

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
     * @param myUser
     *            true if the user to fit is the user using panel, false
     *            otherwise
     */
    public UserPanel(final User user, final boolean myUser) {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        final GUIFactory factory = new StandardGUIFactory();
        this.user = user;
        this.lblUser = myUser ? (JLabel) factory.createLabel(this.user.getName(), Color.GREEN)
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
     */
    public void setUser(final User user) {
        this.user = user;
        this.lblUser.setText(this.user.getName());
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

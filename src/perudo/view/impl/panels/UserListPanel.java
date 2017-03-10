package perudo.view.impl.panels;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import perudo.model.User;
import perudo.view.GUIFactory;
import perudo.view.impl.GUIFactorySingleton;

/**
 * Panel rappresenting a user list.
 */
public class UserListPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final GridBagConstraints cnst;
    private final JPanel pnlUserList;
    private User user;

    /**
     * Initialize an empty list panel.
     */
    public UserListPanel() {
        this.setLayout(new FlowLayout());
        final GUIFactory factory = GUIFactorySingleton.getFactory();
        this.pnlUserList = factory.createPanel(new GridBagLayout());
        this.setBorder(new TitledBorder("Users list"));
        this.cnst = new GridBagConstraints();
        this.cnst.gridy = 0;
        this.cnst.insets = new Insets(0, 0, 0, 0);
        this.add(factory.createScrollPaneWithoutBorder(this.pnlUserList));
    }

    /**
     * Set the user using the panel.
     * 
     * @param user
     *            the user using the panel
     */
    public void setUser(final User user) {
        this.user = user;
    }

    /**
     * Add a user to the list panel.
     * 
     * @param user
     *            the user to add
     */
    public void addUser(final User user) {
        final UserPanel p = new UserPanel(user, this.user.equals(user));
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
            if (((UserPanel) this.pnlUserList.getComponent(i)).getUser().equals(user)) {
                inserted = true;
                ((UserPanel) this.pnlUserList.getComponent(i)).setUser(user);
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
            if (((UserPanel) this.pnlUserList.getComponent(i)).getUser().equals(user)) {
                this.pnlUserList.remove(this.pnlUserList.getComponent(i));
                break;
            }
        }
    }

}

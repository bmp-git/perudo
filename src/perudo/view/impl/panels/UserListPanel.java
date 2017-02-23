package perudo.view.impl.panels;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import perudo.model.User;
import perudo.view.GUIFactory;
import perudo.view.impl.StandardGUIFactory;

public class UserListPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private final GUIFactory factory;
    private final GridBagConstraints cnst;
    private final JPanel pnlUserList;
    
    public UserListPanel() {
        this.setLayout(new FlowLayout());
        this.factory = new StandardGUIFactory();
        this.pnlUserList = this.factory.createPanel();
        this.pnlUserList.setLayout(new GridBagLayout());
        this.setBorder(new TitledBorder("Users list"));
        this.cnst = new GridBagConstraints();
        this.cnst.gridy = 0;
        this.cnst.insets = new Insets(0,0,0,0);
        this.add(this.pnlUserList);
    }

    public void addUser(final UserPanel userpnl) {    
        this.pnlUserList.add(userpnl,this.cnst);
        this.cnst.gridy++;
    }
    
    public void updateUser(final User user) {
        boolean inserted = false;
        for (int i = 0; i < this.pnlUserList.getComponentCount() && !inserted; i++) {
            if (((UserPanel) this.pnlUserList.getComponent(i)).getUser().equals(user)) {
                inserted = true;
                ((UserPanel) this.pnlUserList.getComponent(i)).setUser(user);
            }
        }
        if(!inserted) {
            this.addUser((UserPanel)this.factory.createUserPanel(user));
        }
    }
    
    public void removeUser(final User user) {
        for (int i = 0; i < this.pnlUserList.getComponentCount(); i++) {
            if (((UserPanel) this.pnlUserList.getComponent(i)).getUser().equals(user)) {
                this.pnlUserList.remove(this.pnlUserList.getComponent(i));
                break;
            }
        }
    }

}

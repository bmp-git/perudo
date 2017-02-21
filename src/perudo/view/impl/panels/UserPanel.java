package perudo.view.impl.panels;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import perudo.model.User;
import perudo.view.GUIFactory;
import perudo.view.impl.StandardGUIFactory;

public class UserPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final GUIFactory factory;
    private User user;
    private JLabel lblUser;
    
    public UserPanel(User user) {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.factory = new StandardGUIFactory();
        this.user = user;
        this.lblUser = (JLabel) this.factory.createLabel(this.user.getName());
        this.add(Box.createRigidArea(new Dimension(0,10)));
        this.add(this.lblUser);
        this.add(Box.createRigidArea(new Dimension(0,10)));
    }
    
    public void setUser(User user) {
        this.user = user;
        this.lblUser.setText(this.user.getName());
    }
    
    public User getUser() {
        return this.user;
    }

}

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

public class UserPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final GUIFactory factory;
    private User user;
    private JLabel lblUser;
    
    public UserPanel(User user, boolean myUser) {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.factory = new StandardGUIFactory();
        this.user = user;
        this.lblUser = myUser ? (JLabel) this.factory.createLabel(this.user.getName(),Color.GREEN) : (JLabel) this.factory.createLabel(this.user.getName());
        this.add(Box.createRigidArea(new Dimension(0,10)));
        this.add(this.lblUser);
        this.add(Box.createRigidArea(new Dimension(0,10)));
        this.setBorder(new EmptyBorder(0, 5, 0, 5));

    }
    
    public void setUser(final User user) {
        this.user = user;
        this.lblUser.setText(this.user.getName());
    }
    
    public User getUser() {
        return this.user;
    }

}

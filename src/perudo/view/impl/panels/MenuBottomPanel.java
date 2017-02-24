package perudo.view.impl.panels;

import java.awt.BorderLayout;
import java.util.Optional;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import perudo.model.User;
import perudo.view.GUIFactory;
import perudo.view.impl.ControllerSingleton;
import perudo.view.impl.StandardGUIFactory;

public class MenuBottomPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final GUIFactory factory;
    private Optional<User> user;
    private JLabel lblname;
    private JLabel lblonline;

    public MenuBottomPanel() {
        super();
        this.factory = new StandardGUIFactory();
        this.user = Optional.empty();
        this.setBorder(new EmptyBorder(5, 10, 5, 10));

        if (this.user.isPresent()) {
            if (ControllerSingleton.getController().getClass().getName().equals("perudo.controller.net.client.NetworkControllerImpl")) {
                this.lblname = (JLabel) this.factory.createLabel("Logged as " + this.user.get().getName());
                this.lblonline = (JLabel) this.factory.createLabel("Online.");
            } else {
                this.lblname = (JLabel) this.factory.createLabel("Logged as " + this.user.get().getName());
                this.lblonline = (JLabel) this.factory.createLabel("Offline singleplayer.");
            }
        } else {
            this.lblname = (JLabel) this.factory.createLabel("Not logged yet..");
            this.lblonline = (JLabel) this.factory.createLabel("Offline.");
        }
        this.setLayout(new BorderLayout());
        this.add(this.lblname, BorderLayout.LINE_START);
        this.add(this.lblonline, BorderLayout.LINE_END);
    }

    public void setUser(User user) {
        this.user = Optional.ofNullable(user);
        if (this.user.isPresent()) {
            if (ControllerSingleton.getController().getClass().getName().equals("perudo.controller.net.client.NetworkControllerImpl")) {
                System.out.println(ControllerSingleton.getController().getClass().getName());
                this.lblname.setText("Logged as " + this.user.get().getName());
                this.lblonline.setText("Online.");
            } else {
                this.lblname.setText("Logged as " + this.user.get().getName());
                this.lblonline.setText(("Offline singleplayer."));
            }
        } else {
            this.lblname.setText("Not logged yet..");
            this.lblonline.setText("Offline.");
        }
    }

}

package perudo.view.impl.panels;

import java.awt.BorderLayout;
import java.util.Optional;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import perudo.model.User;
import perudo.view.GUIFactory;
import perudo.view.impl.ControllerSingleton;
import perudo.view.impl.ControllerSingleton.ControllerType;
import perudo.view.impl.StandardGUIFactory;

/**
 * Panel rappresenting the user and the game modality.
 */
public class MenuBottomPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final int TOP_BOT_BORDER = 5;
    private static final int LEFT_RIGHT_BORDER = 10;

    private Optional<User> user;
    private JLabel lblname;
    private JLabel lblonline;

    /**
     * Initialize the panel without informations.
     */
    public MenuBottomPanel() {
        super();
        this.setLayout(new BorderLayout());
        this.setBorder(new EmptyBorder(TOP_BOT_BORDER, LEFT_RIGHT_BORDER, TOP_BOT_BORDER, LEFT_RIGHT_BORDER));
        this.user = Optional.empty();

        final GUIFactory factory = new StandardGUIFactory();
        if (this.user.isPresent()) {
            this.lblname = (JLabel) factory.createLabel("Logged as " + this.user.get().getName());
            if (ControllerSingleton.getControllerType() == ControllerType.MULTIPLAYER) {
                this.lblonline = (JLabel) factory.createLabel("Online.");
            } else {
                this.lblonline = (JLabel) factory.createLabel("Offline singleplayer.");
            }
        } else {
            this.lblname = (JLabel) factory.createLabel("Not logged yet..");
            this.lblonline = (JLabel) factory.createLabel("Offline.");
        }

        this.add(this.lblname, BorderLayout.LINE_START);
        this.add(this.lblonline, BorderLayout.LINE_END);
    }

    /**
     * Set the user and update panel info.
     * 
     * @param user
     *            the user using panel
     */
    public void setUser(final User user) {
        this.user = Optional.ofNullable(user);
        if (this.user.isPresent()) {
            this.lblname.setText("Logged as " + this.user.get().getName());
            if (ControllerSingleton.getControllerType() == ControllerType.MULTIPLAYER) {
                System.out.println(ControllerSingleton.getController().getClass().getName());
                this.lblonline.setText("Online.");
            } else {
                this.lblonline.setText(("Offline singleplayer."));
            }
        } else {
            this.lblname.setText("Not logged yet..");
            this.lblonline.setText("Offline.");
        }
    }

}

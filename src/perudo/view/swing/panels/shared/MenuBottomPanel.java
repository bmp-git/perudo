package perudo.view.swing.panels.shared;

import java.awt.BorderLayout;
import java.util.Optional;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import perudo.model.User;
import perudo.view.swing.utility.ControllerSingleton;
import perudo.view.swing.utility.GUIFactory;
import perudo.view.swing.utility.GUIFactorySingleton;
import perudo.view.swing.utility.ControllerSingleton.ControllerType;

/**
 * Panel representing the user and the game modality.
 */
public class MenuBottomPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final int TOP_BOT_BORDER = 5;
    private static final int LEFT_RIGHT_BORDER = 10;
    private static final String ONLINE_TEXT = "Online.";
    private static final String OFFLINE_TEXT = "Offline";
    private static final String SINGLEPLAYER_TEXT = "Offline singleplayer.";
    private static final String NOT_LOGGED_TEXT = "Not logged yet..";


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

        final GUIFactory factory = GUIFactorySingleton.getFactory();
        if (this.user.isPresent()) {
            this.lblname = (JLabel) factory.createLabel("Logged as " + this.user.get().getName());
            if (ControllerSingleton.getControllerType() == ControllerType.MULTIPLAYER) {
                this.lblonline = (JLabel) factory.createLabel(ONLINE_TEXT);
            } else {
                this.lblonline = (JLabel) factory.createLabel(SINGLEPLAYER_TEXT);
            }
        } else {
            this.lblname = (JLabel) factory.createLabel(NOT_LOGGED_TEXT);
            this.lblonline = (JLabel) factory.createLabel(OFFLINE_TEXT);
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
                this.lblonline.setText(ONLINE_TEXT);
            } else {
                this.lblonline.setText((SINGLEPLAYER_TEXT));
            }
        } else {
            this.lblname.setText(NOT_LOGGED_TEXT);
            this.lblonline.setText(OFFLINE_TEXT);
        }
    }

}

package perudo.view.impl.panels;

import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import perudo.view.GUIFactory;
import perudo.view.impl.GUIFactorySingleton;

/**
 * Panel representing a form to change name.
 */
public class ChangeNamePanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Panel title.
     */
    public static final String TITLE = "Change username...";
    private static final String LABEL_NAME = "New name";
    private final JTextField txfName;

    /**
     * Initialize panel.
     */
    public ChangeNamePanel() {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        final GUIFactory factory = GUIFactorySingleton.getFactory();
        this.txfName = (JTextField) factory.createTextField();

        this.add(Box.createRigidArea(new Dimension(0, 10)));
        this.add(factory.createLabel(LABEL_NAME));
        this.add(this.txfName);
        this.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    /**
     * Get new name from panel.
     * 
     * @return the new name
     */
    public String getName() {
        return this.txfName.getText();
    }
}

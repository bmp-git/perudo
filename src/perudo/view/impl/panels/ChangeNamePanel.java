package perudo.view.impl.panels;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;

import perudo.view.GUIFactory;
import perudo.view.impl.StandardGUIFactory;

public class ChangeNamePanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String TITLE = "Change username...";
    private static final String LABEL_NAME = "New name";
    private final GUIFactory factory;
    private JTextField txfName;
    
    public ChangeNamePanel() {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.factory = new StandardGUIFactory();
        this.txfName = (JTextField) this.factory.createTextField();
        
        this.add(Box.createRigidArea(new Dimension(0, 10)));
        this.add(this.factory.createLabel(LABEL_NAME));
        this.add(this.txfName);
        this.add(Box.createRigidArea(new Dimension(0, 10)));
    }
    
    public String getName() {
        return this.txfName.getText();
    }
}

package perudo.view.impl.components;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class DiceLabel extends JLabel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private int value;
    
    public DiceLabel(int value) {
        super();
        this.value = value;
        this.setIcon(new ImageIcon(DiceLabel.class.getResource("/images/d"+this.value+".jpg")));
    }
    
    public int getValue() {
        return this.value;
    }
    
    public void setValue(int value) {
        this.value = value;
        this.setIcon(new ImageIcon(DiceLabel.class.getResource("/images/d"+this.value+".jpg")));
    }
}

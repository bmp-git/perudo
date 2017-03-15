package perudo.view.swing.components;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import perudo.view.swing.utility.GUIUtility;
import perudo.view.swing.utility.Icon;

/**
 * Label representing a dice.
 */
public class DiceLabel extends JLabel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int value;

    /**
     * Get the dice value.
     * 
     * @return the dice value
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Set the dice value and the dice image size.
     * 
     * @param value
     *            the dice value
     * @param size
     *            the size of height and width of dice image
     */
    public void setValue(final int value, final int size) {
        this.value = value;
        this.setIcon(new ImageIcon(GUIUtility.getScaledImage(
                new ImageIcon(Icon.class.getResource("/images/dices/d" + this.value + ".jpg")).getImage(), size, size)));
    }

}

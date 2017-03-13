package perudo.view.impl.components;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

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
        try {
            this.setIcon(new ImageIcon(
                    ImageIO.read(new File(DiceLabel.class.getResource("/images/dices/d" + this.value + ".jpg").getPath()))
                            .getScaledInstance(size, size, Image.SCALE_DEFAULT)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

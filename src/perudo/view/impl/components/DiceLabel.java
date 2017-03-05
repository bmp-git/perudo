package perudo.view.impl.components;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class DiceLabel extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int value;

	public DiceLabel(int value, int size) {
		super();
		this.value = value;
		try {
			this.setIcon(new ImageIcon(
					ImageIO.read(new File(DiceLabel.class.getResource("/images/d" + this.value + ".jpg").getPath()))
							.getScaledInstance(size, size, Image.SCALE_DEFAULT)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getValue() {
		return this.value;
	}

	public void setValue(int value, int size) {
		this.value = value;
		try {
			this.setIcon(new ImageIcon(
					ImageIO.read(new File(DiceLabel.class.getResource("/images/d" + this.value + ".jpg").getPath()))
							.getScaledInstance(size, size, Image.SCALE_DEFAULT)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

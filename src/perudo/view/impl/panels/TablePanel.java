package perudo.view.impl.panels;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class TablePanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final Image img;

    public TablePanel(final Image img) {
            super();
            this.img = img;
            repaint();
        }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.img, 0, 0, null);
    }
}

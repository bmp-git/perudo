package perudo.view.impl;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * Utility class for making a GUI.
 */
public final class GUIUtility {

    private GUIUtility() {
    }

    /**
     * Fit and show the frame proportioned with monitor height and width.
     * 
     * @param frame
     *            the frame to fit
     * @param proportion
     *            the dividing proportion
     */
    public static void fitFrame(final JFrame frame, final int proportion) {
        final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        final int screenWidth = (int) screen.getWidth();
        final int screenHeight = (int) screen.getHeight();
        frame.setSize(screenWidth / proportion, screenHeight / proportion);
        frame.setVisible(true);
    }

    /**
     * Fit and show the frame packed.
     * 
     * @param frame
     *            the frame to fit
     */
    public static void fitFramePacked(final JFrame frame) {
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Get icon from the resource package.
     * 
     * @param respath
     *            the icon res path
     * @return the icon
     */
    public static ImageIcon getIcon(final String respath) {
        return new ImageIcon(StandardGUIFactory.class.getResource(respath));
    }

}

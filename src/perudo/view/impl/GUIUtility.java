package perudo.view.impl;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

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
     * Return an image scaled in height and width.
     * 
     * @param srcImg
     *            source image
     * @param w
     *            width scaled
     * @param h
     *            height scaled
     * @return image scaled
     */
    public static Image getScaledImage(final Image srcImg, final int w, final int h) {
        final BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }

}

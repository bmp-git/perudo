package perudo.view.impl.components;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Icon;

public class RotatedIcon implements Icon {

    private final Icon icon;
    private double degrees;

    /**
     * Create a RotatedIcon. The icon will rotate about its center.
     * 
     * @param icon
     *            the Icon to rotate
     * @param degrees
     *            the degrees of rotation
     */
    public RotatedIcon(final Icon icon, final double degrees) {
        this.icon = icon;
        this.degrees = degrees;
    }

    /**
     * Gets the Icon to be rotated
     *
     * @return the Icon to be rotated
     */
    public Icon getIcon() {
        return icon;
    }

    /**
     * Gets the degrees of rotation.
     *
     * @return the degrees of rotation
     */
    public double getDegrees() {
        return degrees;
    }

    /**
     * Set the degrees of rotation. The icon must be repainted.
     *
     * @param degrees
     *            the degrees of rotation
     */
    public void setDegrees(final double degrees) {
        this.degrees = degrees;
    }

    /**
     * Gets the width of this icon.
     *
     * @return the width of the icon in pixels.
     */
    @Override
    public int getIconWidth() {
        final double radians = Math.toRadians(degrees);
        final double sin = Math.abs(Math.sin(radians));
        final double cos = Math.abs(Math.cos(radians));
        return (int) Math.floor(icon.getIconWidth() * cos + icon.getIconHeight() * sin);
    }

    /**
     * Gets the height of this icon.
     *
     * @return the height of the icon in pixels.
     */
    @Override
    public int getIconHeight() {
        final double radians = Math.toRadians(degrees);
        final double sin = Math.abs(Math.sin(radians));
        final double cos = Math.abs(Math.cos(radians));
        return (int) Math.floor(icon.getIconHeight() * cos + icon.getIconWidth() * sin);
    }

    /**
     * Paint the icons of this compound icon at the specified location
     *
     * @param c
     *            The component on which the icon is painted
     * @param g
     *            the graphics context
     * @param x
     *            the X coordinate of the icon's top-left corner
     * @param y
     *            the Y coordinate of the icon's top-left corner
     */
    @Override
    public void paintIcon(final Component c, final Graphics g, final int x, final int y) {
        final Graphics2D g2 = (Graphics2D) g.create();

        final int cWidth = icon.getIconWidth() / 2;
        final int cHeight = icon.getIconHeight() / 2;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setClip(x, y, getIconWidth(), getIconHeight());
        g2.translate((getIconWidth() - icon.getIconWidth()) / 2, (getIconHeight() - icon.getIconHeight()) / 2);
        g2.rotate(Math.toRadians(degrees), x + cWidth, y + cHeight);
        icon.paintIcon(c, g2, x, y);

        g2.dispose();
    }

}

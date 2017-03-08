package perudo.view.impl;

import java.awt.Color;
import java.awt.LayoutManager;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import perudo.view.GUIFactory;

/**
 * Standard implementation of a GUI elements factory.
 *
 */
public class StandardGUIFactory implements GUIFactory {

    @Override
    public JFrame createFrame(final String title) {
        return new JFrame(title);
    }

    @Override
    public JPanel createPanel() {
        return new JPanel();
    }

    @Override
    public JPanel createPanel(final LayoutManager layout) {
        return new JPanel(layout);
    }

    @Override
    public Border createBorder(final Color color, final int padding) {
        return BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(color),
                new EmptyBorder(padding, padding, padding, padding));
    }

    @Override
    public JComponent createMenu(final String name) {
        return new JMenu(name);
    }

    @Override
    public JComponent createMenuItem(final String name, final int mnemonic, final String tooltip) {
        final JMenuItem menu = new JMenuItem(name, mnemonic);
        menu.setToolTipText(tooltip);
        return menu;
    }

    @Override
    public JComponent createButton(final String text) {
        return new JButton(text);
    }

    @Override
    public JComponent createButton(final String text, final Icon icon) {
        return new JButton(text, icon);
    }

    @Override
    public JComponent createLabel(final String text) {
        return new JLabel(text);
    }

    @Override
    public JComponent createLabel(final String text, final Color color) {
        final JLabel label = new JLabel(text);
        label.setForeground(color);
        label.repaint();
        return label;
    }

    @Override
    public JComponent createLabel(final String text, final Icon icon, final int alignment) {
        return new JLabel(text, icon, alignment);
    }

    @Override
    public JComponent createPicLabel(final String respath) throws IOException {
        return new JLabel(new ImageIcon(StandardGUIFactory.class.getResource(respath)));
    }

    @Override
    public JComponent createTextField() {
        return new JTextField();
    }

    @Override
    public JComboBox<Object> createComboBox(final Object[] values) {
        return new JComboBox<>(values);
    }

    @Override
    public JComponent createSliderHorizontal(final int minvalue, final int maxvalue, final int defaultvalue) {
        return new JSlider(JSlider.HORIZONTAL, minvalue, maxvalue, defaultvalue);
    }
}

package perudo.view.swing.utility;

import java.awt.Color;
import java.awt.LayoutManager;
import java.io.IOException;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

/**
 * Define a factory for GUI elements.
 */
public interface GUIFactory {

    /**
     * Create a frame with title.
     * 
     * @param title
     *            the frame title
     * @return the frame
     */
    JFrame createFrame(String title);

    /**
     * Create an empty panel.
     * 
     * @return the panel
     */
    JPanel createPanel();

    /**
     * Create a panel with a specified layout.
     * 
     * @param layout
     *            the layout for panel
     * @return the panel
     */
    JPanel createPanel(LayoutManager layout);

    /**
     * Create a border with a color and an internal padding.
     * 
     * @param color
     *            the border color
     * @param padding
     *            the border padding
     * @return the border
     */
    Border createBorder(Color color, int padding);

    /**
     * Create a button with text.
     * 
     * @param text
     *            the button text
     * @return the button
     */
    JComponent createButton(String text);

    /**
     * Create a button with text and icon.
     * 
     * @param text
     *            the button text
     * @param icon
     *            the button icon
     * @return the button
     */
    JComponent createButton(String text, Icon icon);

    /**
     * Create a label with text.
     * 
     * @param text
     *            the label text
     * @return the label
     */
    JComponent createLabel(String text);

    /**
     * Create a label with text and color.
     * 
     * @param text
     *            the label text
     * @param color
     *            the label text color
     * @return the label
     */
    JComponent createLabel(String text, Color color);

    /**
     * Create a label with text and icon.
     * 
     * @param text
     *            the label text
     * @param icon
     *            the label icon
     * @param alignment
     *            the label alignment
     * @return the label
     */
    JComponent createLabel(String text, Icon icon, int alignment);

    /**
     * Create a label with image inside.
     * 
     * @param respath
     *            the image res path
     * @return the label
     * @throws IOException
     *             if the image is not found
     */
    JComponent createPicLabel(String respath) throws IOException;

    /**
     * Create a textfield.
     * 
     * @return the text field
     */
    JComponent createTextField();

    /**
     * Create a textarea.
     * 
     * @return the text area
     */
    JComponent createTextArea();

    /**
     * Create an empty menu.
     * 
     * @param name
     *            the menu name
     * @return the menu
     */
    JComponent createMenu(String name);

    /**
     * Create a menu item specifing name, mnemonic associated key and tooltip.
     * 
     * @param name
     *            the name
     * @param mnemonic
     *            the mnemonic associated key
     * @param tooltip
     *            the tooltip
     * @return the menu item
     */
    JComponent createMenuItem(String name, int mnemonic, String tooltip);

    /**
     * Create a combo box from values.
     * 
     * @param values
     *            the values to insert in combobox
     * @return the combobox
     */
    JComboBox<Object> createComboBox(Object[] values);

    /**
     * Create a slider beetween two values.
     * 
     * @param minvalue
     *            the minimun value
     * @param maxvalue
     *            the maximun value
     * @param defaultvalue
     *            the default value
     * @return the slider
     */
    JComponent createSliderHorizontal(int minvalue, int maxvalue, int defaultvalue);

    /**
     * Create a JScrollPane without black border.
     * 
     * @param panel
     *            panel to add into scroll panel
     * @return the scroll panel without border
     */
    JScrollPane createScrollPaneWithoutBorder(JPanel panel);

}

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
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import perudo.model.Lobby;
import perudo.model.User;
import perudo.view.GUIFactory;
import perudo.view.impl.components.TopMenu;
import perudo.view.impl.panels.ChangeNamePanel;
import perudo.view.impl.panels.CreateLobbyPanel;
import perudo.view.impl.panels.LobbyPanel;
import perudo.view.impl.panels.MenuBottomPanel;
import perudo.view.impl.panels.UserPanel;

public class StandardGUIFactory implements GUIFactory {

    @Override
    public JPanel createPanel() {
        return new JPanel();
    }
    
    @Override
    public JPanel createPanel(LayoutManager layout) {
        return new JPanel(layout);
    }

    @Override
    public JPanel createMenuPanel() {
        return new MenuPanel();
    }

    @Override
    public JPanel createGamePanel() {
        return new GamePanel();
    }

    @Override
    public JPanel createLobbyPanel(Lobby lobby) {
        return new LobbyPanel(lobby);
    }

    @Override
    public JPanel createUserPanel(User user, boolean myUser) {
        return new UserPanel(user, myUser);
    }

    @Override
    public JPanel createCreateLobbyPanel() {
        return new CreateLobbyPanel();
    }

    @Override
    public JPanel createMenuBottomPanel() {
        return new MenuBottomPanel();
    }

    @Override
    public JPanel createChangeNamePanel() {
        return new ChangeNamePanel();
    }

    @Override
    public Border createBorder(Color color, int padding) {
        return BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(color),
                new EmptyBorder(padding, padding, padding, padding));
    }

    @Override
    public JComponent createTopMenu() {
        return new TopMenu();
    }

    @Override
    public JComponent createMenu() {
        return new JMenu();
    }

    @Override
    public JComponent createMenuItem() {
        return new JMenuItem();
    }

    @Override
    public JComponent createButton(String text) {
        return new JButton(text);
    }
    
    @Override
    public JComponent createButton(String text, Icon icon) {
        return new JButton(text,icon);
    }

    @Override
    public JComponent createLabel(String text) {
        return new JLabel(text);
    }

    @Override
    public JComponent createLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setForeground(color);
        label.repaint();
        return label;
    }

    @Override
    public JComponent createLabel(String text, Icon icon, int alignment) {
        return new JLabel(text, icon, alignment);
    }

    @Override
    public JComponent createPicLabel(String respath) throws IOException {
        return new JLabel(new ImageIcon(StandardGUIFactory.class.getResource(respath)));
    }

    @Override
    public JComponent createTextField() {
        return new JTextField();
    }

    @Override
    public JComboBox<Object> createComboBox(Object[] values) {
        return new JComboBox<>(values);
    }

    @Override
    public JComponent createSliderHorizontal(int minvalue, int maxvalue, int defaultvalue) {
        return new JSlider(JSlider.HORIZONTAL, minvalue, maxvalue, defaultvalue);
    }

    @Override
    public JComponent createVerticalSplitPane() {
        return new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    }

    @Override
    public JComponent createVerticalScrollPanel() {
        JScrollPane panel = new JScrollPane();
        panel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        return panel;
    }

    @Override
    public JFrame createFrame(String title) {
        return new JFrame(title);
    }

}

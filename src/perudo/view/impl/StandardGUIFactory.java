package perudo.view.impl;


import java.awt.Color;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import perudo.view.GUIFactory;
import perudo.view.impl.panels.CreateLobbyPanel;

public class StandardGUIFactory implements GUIFactory {
    
    @Override
    public JPanel createPanel() {
        return new JPanel();
    }
    
    @Override
    public JPanel createMenuPanel() {
        return new MenuPanel();
    }
    
    @Override
    public JPanel createCreateLobbyPanel() {
        return new CreateLobbyPanel();
    }

    @Override
    public JComponent createButton(String text) {
        return new JButton(text);
    }

    @Override
    public JComponent createLabel(String text) {
        return new JLabel(text);
    }
    

    @Override
    public JComponent createLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setForeground(color);
        label.setOpaque(true);
        return label;
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

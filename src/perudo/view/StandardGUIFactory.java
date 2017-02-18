package perudo.view;


import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ScrollPaneConstants;

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
    public JComponent createButton(String text) {
        return new JButton(text);

    }

    @Override
    public JComponent createLabel(String text) {
        return new JLabel(text);
    }
    
    @Override
    public JComponent createPicLabel(String respath) throws IOException {
        return new JLabel(new ImageIcon(StandardGUIFactory.class.getResource(respath)));
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

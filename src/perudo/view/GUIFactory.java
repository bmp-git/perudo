package perudo.view;

import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public interface GUIFactory {
        
    JFrame createFrame(String title);
    
    JPanel createPanel();
    
    JPanel createMenuPanel();
    
    JComponent createButton(String text);
    
    JComponent createLabel(String text);
    
    JComponent createPicLabel(String respath) throws IOException;
    
    JComponent createVerticalSplitPane();
    
    JComponent createVerticalScrollPanel();
}

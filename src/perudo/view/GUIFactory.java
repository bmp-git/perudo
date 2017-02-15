package perudo.view;

import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JPanel;

public interface GUIFactory {
        
    JPanel createPanel();
    
    JComponent createButton(String text);
    
    JComponent createLabel(String text);
    
    JComponent createPicLabel(String respath) throws IOException;
    
    JComponent createVerticalSplitPane();
    
    JComponent createVerticalScrollPanel();
}

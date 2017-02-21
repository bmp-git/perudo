package perudo.view;

import java.awt.Color;
import java.io.IOException;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public interface GUIFactory {
        
    JFrame createFrame(String title);
    
    JPanel createPanel();
    
    JPanel createMenuPanel();
    
    JPanel createCreateLobbyPanel();
    
    JPanel createMenuBottomPanel();
    
    JPanel createChangeNamePanel();
        
    JComponent createButton(String text);
    
    JComponent createLabel(String text);
    
    JComponent createLabel(String text, Color color);
    
    JComponent createPicLabel(String respath) throws IOException;
    
    JComponent createTextField();
    
    JComponent createTopMenu();
        
    JComponent createMenu();
    
    JComponent createMenuItem();
    
    JComboBox<Object> createComboBox(Object[] values);
    
    JComponent createSliderHorizontal(int minvalue, int maxvalue, int defaultvalue);
    
    JComponent createVerticalSplitPane();
    
    JComponent createVerticalScrollPanel();
}

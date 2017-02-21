package perudo.view;

import java.awt.Color;
import java.io.IOException;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import perudo.model.Lobby;
import perudo.model.User;

public interface GUIFactory {
        
    JFrame createFrame(String title);
    
    JPanel createPanel();
    
    JPanel createMenuPanel();
    
    JPanel createLobbyPanel(Lobby lobby);
    
    JPanel createUserPanel(User user);
    
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

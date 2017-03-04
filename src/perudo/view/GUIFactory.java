package perudo.view;

import java.awt.Color;
import java.awt.LayoutManager;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

import perudo.model.Lobby;
import perudo.model.User;

public interface GUIFactory {
        
    JFrame createFrame(String title);
    
    JPanel createPanel();
    
	JPanel createPanel(LayoutManager layout);
    
    JPanel createMenuPanel();
    
    JPanel createGamePanel();
    
    JPanel createLobbyPanel(Lobby lobby);
    
    JPanel createUserPanel(User user, boolean myUser);
    
    JPanel createCreateLobbyPanel();
    
    JPanel createMenuBottomPanel();
    
    JPanel createChangeNamePanel();
    
    Border createBorder(Color color, int padding);
        
    JComponent createButton(String text);
    
	JComponent createButton(String text, Icon icon);
    
    JComponent createLabel(String text);
    
    JComponent createLabel(String text, Color color);
    
    JComponent createLabel(String text, Icon icon, int alignment);
    
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

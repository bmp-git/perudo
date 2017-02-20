package perudo.view.impl;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class GUIUtility {
    
    private GUIUtility() {
        
    }
    
    public static void fitFrame(JFrame frame, int proportion) {

        final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        final int sw = (int) screen.getWidth();
        final int sh = (int) screen.getHeight();
        frame.setSize(sw / proportion, sh / proportion);

        frame.setVisible(true);
    }
    
    public static void fitFramePacked(JFrame frame) {

        frame.pack();

        frame.setVisible(true);
    }
    
    public static ImageIcon getIcon(String respath) {
        return new ImageIcon(StandardGUIFactory.class.getResource(respath));
    }

}

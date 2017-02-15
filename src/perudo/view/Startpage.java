package perudo.view;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Startpage extends JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = -3727819554802551366L;

    private static final String TITLE = "Perudo";
    private static final String EXIT_NAME = "Quitting..";
    private static final String EXIT_TEXT = "Do you really want to quit?";
    private static final String LOGO_RESPATH = "/images/perudo-logo.png";
    private static final String ICON_RESPATH = "/images/perudo-logo.png";

    private final GUIFactory factory;
    private JPanel main;
    private JPanel top;
    private JButton multiplayer;
    private JButton singleplayer;
    private JButton exit;
    private JLabel logo;

    public Startpage() {
        this.factory = new StandardGUIFactory();

        this.setTitle(TITLE);
        this.setLayout(new BorderLayout());
        this.setIconImage(GUIUtility.getIcon(ICON_RESPATH).getImage());

        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.setLocationByPlatform(true);
        this.setResizable(false);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int n = JOptionPane.showConfirmDialog(Startpage.this, EXIT_TEXT, EXIT_NAME, JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION)
                    System.exit(0);
            }
        });
        
        createMainPanel();
        createTopPanel();
        
        this.getContentPane().add(top, BorderLayout.NORTH);
        this.getContentPane().add(main, BorderLayout.CENTER);

    }

    private void createMainPanel() {
        main = this.factory.createPanel();
        main.setLayout(new GridBagLayout());

        multiplayer = (JButton) factory.createButton("Multiplayer");
        singleplayer = (JButton) factory.createButton("Singleplayer");
        exit = (JButton) factory.createButton("Exit");
        exit.addActionListener(a -> {
            int n = JOptionPane.showConfirmDialog(this, EXIT_TEXT, EXIT_NAME, JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION)
                System.exit(0);
        });

        final GridBagConstraints cnst = new GridBagConstraints();
        cnst.gridy = 0;
        cnst.insets = new Insets(25, 25, 25, 25);
        main.add(multiplayer, cnst);
        cnst.gridy++;
        main.add(singleplayer, cnst);
        cnst.gridy++;
        main.add(exit, cnst);
        cnst.gridy++;
    }

    private void createTopPanel() {
        top = this.factory.createPanel();
        try {
            this.logo = (JLabel) factory.createPicLabel(LOGO_RESPATH);
        } catch (IOException e) {
            System.out.println(e);
        }
        top.add(logo);
    }

    public void showFrame() {
        GUIUtility.fitFramePacked(this);
    }
}

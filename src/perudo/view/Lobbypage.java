package perudo.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

public class Lobbypage extends JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String TITLE = "Perudo - Lobby";
    private static final String EXIT_NAME = "Quitting..";
    private static final String EXIT_TEXT = "Do you really want to quit?";
    private static final String ICON_RESPATH = "/images/perudo-logo.png";

    private final GUIFactory factory;
    private JSplitPane splitPane;
    private JPanel lobbylist;
    private JPanel lobbyinfo;

    public Lobbypage() {
        this.factory = new StandardGUIFactory();
        
        this.setTitle(TITLE);
        this.setLayout(new BorderLayout());
        this.setIconImage(GUIUtility.getIcon(ICON_RESPATH).getImage());
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.setLocationByPlatform(true);

        this.addWindowListener(new WindowAdapter(){
            public void windowClosing (WindowEvent e) {
                int n = JOptionPane.showConfirmDialog(Lobbypage.this, EXIT_TEXT, EXIT_NAME, JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION)
                    System.exit(0);
            }
        });
        
        createLobbyListPanel();
        createLobbyInfoPanel();
        createSplitPanel();

        this.add(splitPane, BorderLayout.CENTER);
    }

    private void createLobbyListPanel() {
        lobbylist = factory.createPanel();
        lobbylist.setLayout(new GridBagLayout());
        lobbylist.setBorder(new TitledBorder("Lobbies list"));
        final GridBagConstraints cnst = new GridBagConstraints();
        cnst.anchor = GridBagConstraints.FIRST_LINE_START;
        cnst.weighty = 1.0;
        cnst.gridy = 0;
        cnst.insets = new Insets(5, 5, 5, 5);
        lobbylist.add(factory.createButton("prova"), cnst);
        cnst.gridy++;
        lobbylist.add(factory.createButton("prova"), cnst);
        cnst.gridy++;
        lobbylist.add(factory.createButton("prova"), cnst);
        cnst.gridy++;
        lobbylist.add(factory.createButton("prova"), cnst);
        cnst.gridy++;
        lobbylist.add(factory.createButton("prova"), cnst);
        cnst.gridy++;
        lobbylist.add(factory.createButton("prova"), cnst);
        cnst.gridy++;
        lobbylist.add(factory.createButton("prova"), cnst);

    }

    private void createLobbyInfoPanel() {
        lobbyinfo = new JPanel();
        lobbyinfo.setLayout(new FlowLayout());
        lobbyinfo.setBorder(new TitledBorder("Lobby Info"));
        lobbyinfo.add(new JButton("PROVA"));
        lobbyinfo.add(new JButton("PROVA"));
    }

    private void createSplitPanel() {
        splitPane = (JSplitPane) factory.createVerticalSplitPane();
        final JScrollPane scroll = (JScrollPane) factory.createVerticalScrollPanel();
        scroll.getViewport().add(lobbylist);
        scroll.setPreferredSize(new Dimension(200, 500));

        splitPane.setLeftComponent(scroll);
        splitPane.setRightComponent(lobbyinfo);
    }

    public void showFrame() {
        GUIUtility.fitFrame(this,2);
    }
}

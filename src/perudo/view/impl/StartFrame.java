package perudo.view.impl;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import perudo.view.GUIFactory;

/**
 * Frame modelling a simple menu for choosing playing type.
 */
public class StartFrame extends JFrame {

    /**
     * Enumeration listing possible choices in menu.
     */
    public enum StartingFrameResult {
        /**
         * Results.
         */
        MULTIPLAYER, SINGLEPLAYER, EXIT;
    }

    /**
     * 
     */
    private static final long serialVersionUID = -3727819554802551366L;
    private static final String TITLE = "Perudo";
    private static final String EXIT_NAME = "Quitting..";
    private static final String EXIT_TEXT = "Do you really want to quit?";
    private static final String LOGO_RESPATH = "/images/perudo-logo.png";
    private static final String ICON_RESPATH = "/images/perudo-logo.png";
    private static final int BUTTONS_PADD = 25;

    private final JButton multiplayer;
    private final JButton singleplayer;
    private final JButton exit;
    private JLabel logo;
    private Optional<StartingFrameResult> result;

    /**
     * Initialize and create the menu frame.
     */
    public StartFrame() {
        super();
        final GUIFactory factory = GUIFactorySingleton.getFactory();
        this.result = Optional.empty();

        this.setTitle(TITLE);
        this.setLayout(new BorderLayout());
        this.setIconImage(GUIUtility.getIcon(ICON_RESPATH).getImage());

        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.setLocationByPlatform(true);
        this.setResizable(false);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent event) {
                final int n = JOptionPane.showConfirmDialog(StartFrame.this, EXIT_TEXT, EXIT_NAME, JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION) {
                    StartFrame.this.result = Optional.of(StartingFrameResult.EXIT);
                    StartFrame.this.setVisible(false);
                    StartFrame.this.dispose();
                }
            }
        });

        final JPanel main = factory.createPanel(new GridBagLayout());

        this.multiplayer = (JButton) factory.createButton("Multiplayer");
        this.multiplayer.addActionListener(e -> {
            this.result = Optional.of(StartingFrameResult.MULTIPLAYER);
            this.setVisible(false);
            this.dispose();
        });
        this.singleplayer = (JButton) factory.createButton("Singleplayer");
        this.singleplayer.addActionListener(e -> {
            this.result = Optional.of(StartingFrameResult.SINGLEPLAYER);
            this.setVisible(false);
            this.dispose();
        });
        this.exit = (JButton) factory.createButton("Exit");
        this.exit.addActionListener(a -> {
            final int n = JOptionPane.showConfirmDialog(this, EXIT_TEXT, EXIT_NAME, JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION) {
                this.result = Optional.of(StartingFrameResult.EXIT);
                this.setVisible(false);
                this.dispose();
            }
        });

        final GridBagConstraints cnst = new GridBagConstraints();
        cnst.gridy = 0;
        cnst.insets = new Insets(BUTTONS_PADD, BUTTONS_PADD, BUTTONS_PADD, BUTTONS_PADD);
        main.add(this.multiplayer, cnst);
        cnst.gridy++;
        main.add(this.singleplayer, cnst);
        cnst.gridy++;
        main.add(this.exit, cnst);
        cnst.gridy++;

        final JPanel top = factory.createPanel();
        try {
            this.logo = (JLabel) factory.createPicLabel(LOGO_RESPATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        top.add(this.logo);

        this.getContentPane().add(top, BorderLayout.NORTH);
        this.getContentPane().add(main, BorderLayout.CENTER);

    }

    /**
     * Get the result of the start menu user choice.
     * 
     * @return the result
     */
    public Optional<StartingFrameResult> getResult() {
        return this.result;
    }

    /**
     * Show the frame.
     */
    public void showFrame() {
        GUIUtility.fitFramePacked(this);
    }
}

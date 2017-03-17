package perudo.view.swing;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import perudo.utility.LogSeverity;
import perudo.utility.impl.LoggerSingleton;
import perudo.view.swing.utility.ControllerSingleton;
import perudo.view.swing.utility.GUIFactory;
import perudo.view.swing.utility.GUIFactorySingleton;
import perudo.view.swing.utility.GUIUtility;
import perudo.view.swing.utility.Icon;

/**
 * Frame modelling a simple menu for choosing playing type.
 */
public class StartFrame extends JFrame {
    /**
     * 
     */
    private static final long serialVersionUID = -3727819554802551366L;
    private static final String TITLE = "Perudo";
    private static final String EXIT_NAME = "Quitting..";
    private static final String EXIT_TEXT = "Do you really want to quit?";
    private static final String LOGO_RESPATH = "/images/perudo-logo.png";
    private static final String MULTIPLAYER_BUTTON_TEXT = "Multiplayer";
    private static final String SINGLEPLAYER_BUTTON_TEXT = "Singleplayer";
    private static final String EXIT_BUTTON_TEXT = "Exit";
    private static final int BUTTONS_PADD = 25;

    private boolean initialized;
    private final CountDownLatch latch;

    /**
     * Initialize and create the menu frame.
     * 
     * @param server
     *            the server name or ip
     * @param port
     *            the service port
     */
    public StartFrame(final String server, final int port) {
        super();
        final GUIFactory factory = GUIFactorySingleton.getFactory();
        this.latch = new CountDownLatch(1);
        this.initialized = false;
        this.setTitle(TITLE);
        this.setLayout(new BorderLayout());
        this.setIconImage(Icon.APPLICATION_ICON.getIcon().getImage());

        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.setLocationByPlatform(true);
        this.setResizable(false);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent event) {
                final int n = JOptionPane.showConfirmDialog(StartFrame.this, EXIT_TEXT, EXIT_NAME, JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION) {
                    initialized = false;
                    StartFrame.this.setVisible(false);
                    StartFrame.this.dispose();
                    latch.countDown();
                }
            }
        });

        final JPanel main = factory.createPanel(new GridBagLayout());

        final JButton multiplayer = (JButton) factory.createButton(MULTIPLAYER_BUTTON_TEXT);
        multiplayer.addActionListener(e -> {
            try {
                ControllerSingleton.setMultiplayerController(server, port);
                initialized = true;
                StartFrame.this.setVisible(false);
                StartFrame.this.dispose();
                this.latch.countDown();
            } catch (IOException e1) {
                LoggerSingleton.get().add(LogSeverity.INFO, this.getClass(), "Can't connect to the server " + server + ":" + port);
                JOptionPane.showMessageDialog(StartFrame.this, "Can't connect to the server " + server + ":" + port,
                        "Network error", JOptionPane.ERROR_MESSAGE);
            }
        });
        final JButton singleplayer = (JButton) factory.createButton(SINGLEPLAYER_BUTTON_TEXT);
        singleplayer.addActionListener(e -> {
            ControllerSingleton.setSingleplayerController();
            initialized = true;
            StartFrame.this.setVisible(false);
            StartFrame.this.dispose();
            this.latch.countDown();
        });
        final JButton exit = (JButton) factory.createButton(EXIT_BUTTON_TEXT);
        exit.addActionListener(a -> {
            final int n = JOptionPane.showConfirmDialog(this, EXIT_TEXT, EXIT_NAME, JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION) {
                initialized = false;
                StartFrame.this.setVisible(false);
                StartFrame.this.dispose();
                this.latch.countDown();
            }
        });

        final GridBagConstraints cnst = new GridBagConstraints();
        cnst.gridy = 0;
        cnst.insets = new Insets(BUTTONS_PADD, BUTTONS_PADD, BUTTONS_PADD, BUTTONS_PADD);
        main.add(singleplayer, cnst);
        cnst.gridy++;
        main.add(multiplayer, cnst);
        cnst.gridy++;
        main.add(exit, cnst);
        cnst.gridy++;

        final JPanel top = factory.createPanel();
        JLabel logo = null;
        try {
            logo = (JLabel) factory.createPicLabel(LOGO_RESPATH);
        } catch (IOException e) {
            LoggerSingleton.get().add(LogSeverity.ERROR_UNEXPECTED, this.getClass(), "Can't load image " + LOGO_RESPATH);
        }
        top.add(logo);

        this.getContentPane().add(top, BorderLayout.NORTH);
        this.getContentPane().add(main, BorderLayout.CENTER);

    }

    /**
     * Get the result of the start menu user choice.
     * 
     * @return true if the controller is initialized, false otherwise
     */
    public boolean isInitialized() {
        return this.initialized;
    }

    /**
     * Wait for the frame closing.
     */
    public void await() {
        try {
            this.latch.await();
        } catch (InterruptedException e) {
            LoggerSingleton.get().add(LogSeverity.ERROR_UNEXPECTED, this.getClass(), "Await method crashed.");
        }
    }

    /**
     * Show the frame.
     */
    public void showFrame() {
        GUIUtility.fitFramePacked(this);
    }
}

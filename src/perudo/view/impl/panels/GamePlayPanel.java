package perudo.view.impl.panels;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JPanel;
import perudo.model.Game;
import perudo.model.User;
import perudo.view.GUIFactory;
import perudo.view.impl.ControllerSingleton;
import perudo.view.impl.GUIFactorySingleton;

/**
 * Panel representing elements to play the game.
 */
public class GamePlayPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String PLAY_BUTTON = "Play";
    private static final String DUBIT_BUTTON = "Doubt";
    private static final String URGE_BUTTON = "Urge";
    private static final String PALIFICO_BUTTON = "Palifico";
    private static final int TOP_BOT_INSETS = 5;

    private Game game;
    private User user;

    private final BidPanel pnlBid;
    private final JButton btnPlay;
    private final JButton btnDubit;
    private final JButton btnUrge;
    private final JButton btnPalifico;

    /**
     * Initialize and create the gameplay panel.
     */
    public GamePlayPanel() {
        super();
        this.setLayout(new GridBagLayout());
        final GridBagConstraints cnst = new GridBagConstraints();
        cnst.gridy = 0;
        cnst.insets = new Insets(TOP_BOT_INSETS, 0, TOP_BOT_INSETS, 0);
        final GUIFactory factory = GUIFactorySingleton.getFactory();
        final JPanel pnlInner = factory.createPanel(new FlowLayout());
        this.pnlBid = new BidPanel();
        this.btnPlay = (JButton) factory.createButton(PLAY_BUTTON);
        this.btnPlay.addActionListener(l -> {
            ControllerSingleton.getController().play(this.user, this.pnlBid.getBid());
        });
        this.btnDubit = (JButton) factory.createButton(DUBIT_BUTTON);
        this.btnDubit.addActionListener(l -> {
            ControllerSingleton.getController().doubt(this.user);
        });
        this.btnUrge = (JButton) factory.createButton(URGE_BUTTON);
        this.btnUrge.addActionListener(l -> {
            ControllerSingleton.getController().urge(this.user);
        });
        this.btnPalifico = (JButton) factory.createButton(PALIFICO_BUTTON);
        this.btnPalifico.addActionListener(l -> {
            ControllerSingleton.getController().callPalifico(this.user);
        });

        pnlInner.add(this.pnlBid);
        pnlInner.add(this.btnPlay);
        pnlInner.add(this.btnDubit);
        pnlInner.add(this.btnUrge);
        pnlInner.add(this.btnPalifico);
        this.add(pnlInner);
    }

    /**
     * Set game and user who is using the panel.
     * 
     * @param game
     *            the game updated
     * @param user
     *            the user using panel
     */
    public void setGame(final Game game, final User user) {
        this.game = game;
        this.user = user;
        this.pnlBid.setGame(this.game);
        updateButtons();
    }

    private void updateButtons() {
        this.btnPlay.setEnabled(this.game.canPlay(this.user) ? true : false);
        this.btnDubit.setEnabled(this.game.canDoubt(this.user) ? true : false);
        this.btnUrge.setEnabled(this.game.canUrge(this.user) ? true : false);
        this.btnPalifico.setEnabled(this.game.canPalifico(this.user) ? true : false);
    }

    /**
     * Set the panel and subpanels components enabled or disabled.
     * 
     * @param enabled
     *            true enable, false disable
     */
    public void setPanelEnabled(final boolean enabled) {
        this.btnPlay.setEnabled(enabled);
        this.btnDubit.setEnabled(enabled);
        this.btnUrge.setEnabled(enabled);
        this.btnPalifico.setEnabled(enabled);
    }
}

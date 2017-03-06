package perudo.view.impl.panels;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import perudo.model.Game;
import perudo.model.User;
import perudo.view.GUIFactory;
import perudo.view.impl.ControllerSingleton;
import perudo.view.impl.StandardGUIFactory;

public class GamePlayPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String PLAY_BUTTON = "Play";
    private static final String DUBIT_BUTTON = "Dubit";
    private static final String URGE_BUTTON = "Urge";
    private static final String PALIFICO_BUTTON = "Palific";

    private final GUIFactory factory;
    private final BidPanel pnlBid;
    private final JButton btnPlay;
    private final JButton btnDubit;
    private final JButton btnUrge;
    private final JButton btnPalifico;

    public GamePlayPanel(final Game game, final User user) {
        super();
        this.setLayout(new FlowLayout());
        this.pnlBid = new BidPanel(game);
        this.factory = new StandardGUIFactory();
        this.btnPlay = (JButton) this.factory.createButton(PLAY_BUTTON);
        this.btnPlay.addActionListener(l -> {
            ControllerSingleton.getController().play(user, this.pnlBid.getBid());
        });
        this.btnDubit = (JButton) this.factory.createButton(DUBIT_BUTTON);
        this.btnDubit.addActionListener(l -> {
            ControllerSingleton.getController().doubt(user);
        });
        this.btnUrge = (JButton) this.factory.createButton(URGE_BUTTON);
        this.btnUrge.addActionListener(l -> {
            ControllerSingleton.getController().urge(user);
        });
        this.btnPalifico = (JButton) this.factory.createButton(PALIFICO_BUTTON);
        this.btnPalifico.addActionListener(l -> {
            ControllerSingleton.getController().callPalifico(user);
        });
        this.add(this.pnlBid);
        this.add(this.btnPlay);
        this.add(this.btnDubit);
        this.add(this.btnUrge);
        this.add(this.btnPalifico);
    }

}

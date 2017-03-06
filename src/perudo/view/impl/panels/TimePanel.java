package perudo.view.impl.panels;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import perudo.model.Game;

public class TimePanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Game game;
    private final ScheduledExecutorService executor;
    private CircolarTimeBarPanel pnlTime;

    public TimePanel() {
        super();
        this.setLayout(new FlowLayout());
        this.executor = Executors.newScheduledThreadPool(1);
    }

    public void setGame(final Game game) {
        this.game = game;
        this.pnlTime = new CircolarTimeBarPanel(this.game.getSettings().getMaxTurnTime());
        this.add(this.pnlTime);

        this.executor.scheduleAtFixedRate(() -> {
            SwingUtilities.invokeLater(() -> {
                this.pnlTime.setRemainingTime(this.game.getTurnRemainingTime());
                this.removeAll();
                this.add(this.pnlTime);
            });
        }, 0, this.game.getTurnRemainingTime().getSeconds(), TimeUnit.MILLISECONDS);

    }
}

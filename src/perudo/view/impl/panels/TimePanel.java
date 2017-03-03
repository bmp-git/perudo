package perudo.view.impl.panels;

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
    private CircolarTimeBar pnlTime;

    public TimePanel() {
        super();
        this.executor = Executors.newScheduledThreadPool(1);
    }

    public void setGame(final Game game) {
        this.game = game;
        this.pnlTime = new CircolarTimeBar(this.game.getSettings().getMaxTurnTime());
        this.add(this.pnlTime);
        this.executor.scheduleAtFixedRate(() -> {
            SwingUtilities.invokeLater(() -> {
                this.pnlTime.setRemainingTime(this.game.getTurnRemainingTime());
            });
        }, 0, this.game.getTurnRemainingTime().getSeconds(), TimeUnit.MILLISECONDS);

    }
}

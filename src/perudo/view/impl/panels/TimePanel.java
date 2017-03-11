package perudo.view.impl.panels;

import java.awt.FlowLayout;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import perudo.model.Game;

/**
 * Panel managing a timer.
 */
public class TimePanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final ScheduledExecutorService executor;

    /**
     * Initialize an empty panel and the timer executor.
     */
    public TimePanel() {
        super();
        this.setLayout(new FlowLayout());
        this.executor = Executors.newScheduledThreadPool(1);
    }

    /**
     * Set the current game and start the timer.
     * 
     * @param game
     *            the current game
     */
    public void setGame(final Game game) {
        final CircolarTimeBarPanel pnlTime = new CircolarTimeBarPanel(game.getSettings().getMaxTurnTime());
        this.removeAll();
        this.add(pnlTime);

        this.executor.scheduleAtFixedRate(() -> {
            SwingUtilities.invokeLater(() -> {
                pnlTime.setRemainingTime(game.getTurnRemainingTime());
            });
        }, 0, game.getTurnRemainingTime().getSeconds(), TimeUnit.MILLISECONDS);
    }

    /**
     * Stop the timer.
     */
    public void stop() {
        this.executor.shutdown();
    }
}

package perudo.view.impl.panels;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import perudo.model.Game;

public class TimePanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final JLabel lblTime;
    private Game game;
    private final ScheduledExecutorService executor;

    public TimePanel() {
        super();
        this.lblTime = new JLabel("Time : ");
        this.executor = Executors.newScheduledThreadPool(1);
        this.add(this.lblTime);
    }

    public void setGame(final Game game) {
        this.game = game;
        this.executor.scheduleAtFixedRate(() -> {
            SwingUtilities.invokeLater(() -> {
                if (this.game != null) {
                    this.lblTime.setText("Time: " + this.game.getTurnRemainingTime().getSeconds() + "s");
                } else {
                    this.lblTime.setText("lblTime");
                }
            });
        }, 0, 1, TimeUnit.SECONDS);

    }
}

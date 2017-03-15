package perudo.view.swing.panels.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.time.Duration;
import javax.swing.JLabel;
import javax.swing.JPanel;

import perudo.view.swing.utility.GUIFactory;
import perudo.view.swing.utility.GUIFactorySingleton;

/**
 * Panel rappresenting a circolar timer.
 */
public class CircolarTimeBarPanel extends JPanel {

    private static final long serialVersionUID = 4826280349188429462L;
    private static final double BORDER_SIZE = 0.1;
    private static final double STARTING_POSITION_ANGLE = 90;
    private static final double DANGER_TIME_ANGLE = 90;
    private static final Color DANGER_COLOR = Color.RED;
    private static final Color OK_COLOR = Color.GREEN;
    private static final Color LABEL_COLOR = Color.BLACK;
    private static final String LABEL_END_TEXT = "!";
    private static final int FONT_SIZE = 40;

    private final Duration totalTime;
    private Duration remainingTime;
    private final JLabel label;

    /**
     * Initialize the timer with the total time.
     * 
     * @param totalTime
     *            the total time of timer
     */
    public CircolarTimeBarPanel(final Duration totalTime) {
        final GUIFactory factory = GUIFactorySingleton.getFactory();
        this.totalTime = totalTime;
        this.remainingTime = totalTime;
        this.label = (JLabel) factory.createLabel(Integer.toString((int) totalTime.getSeconds()), LABEL_COLOR);
        this.label.setFont(new Font("Consolas", Font.PLAIN, FONT_SIZE));
        this.setLayout(new BorderLayout());
        this.add(this.label);

    }

    /**
     * Set the remaining time of timer and update it.
     * 
     * @param remainingTime
     *            the remaining time
     */
    public void setRemainingTime(final Duration remainingTime) {
        this.remainingTime = remainingTime;
        this.repaint();
        this.revalidate();
    }

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);

        final Graphics2D g2 = (Graphics2D) g;

        final double angle = 360.0 * ((double) this.remainingTime.toNanos() / (double) this.totalTime.toNanos());

        Arc2D arc = new Arc2D.Double(0, 0, this.getWidth(), this.getHeight(), STARTING_POSITION_ANGLE, angle < 0 ? 360 : angle,
                2);
        g2.setColor(angle > DANGER_TIME_ANGLE ? OK_COLOR : DANGER_COLOR);
        g2.fill(arc);

        g2.setColor(this.getBackground());
        arc = new Arc2D.Double(this.getWidth() * BORDER_SIZE, this.getHeight() * BORDER_SIZE + 0.5,
                this.getWidth() - this.getWidth() * BORDER_SIZE * 2, this.getHeight() - this.getHeight() * BORDER_SIZE * 2, 0,
                360, 1);
        g2.fill(arc);

        this.label.setText(remainingTime.isNegative() ? LABEL_END_TEXT : Integer.toString((int) remainingTime.getSeconds()));

    }
}
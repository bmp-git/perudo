package perudo.view.impl.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Arc2D;
import java.time.Duration;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class CircolarTimeBar extends JPanel {

    private static final long serialVersionUID = 4826280349188429462L;
    private Duration totalTime, remainingTime;
    private final JLabel label;
    private static final double BORDER_SIZE = 0.1;
    private static final double STARTING_POSITION_ANGLE = 90;
    private static final double DANGER_TIME_ANGLE = 90;
    private static final Color DANGER_COLOR = Color.RED;
    private static final Color OK_COLOR = Color.GREEN;
    private static final Color LABEL_COLOR = Color.BLACK;
    private static final String LABEL_END_TEXT = "!";

    public CircolarTimeBar(final Duration totalTime) {
        this.totalTime = totalTime;
        this.remainingTime = totalTime;
        this.label = new JLabel(Integer.toString((int) totalTime.getSeconds()), SwingConstants.CENTER);
        this.label.setFont(new Font("Consolas", Font.PLAIN, 50));
        this.label.setForeground(LABEL_COLOR);
        setLayout(new BorderLayout());

        this.add(this.label);

        class ResizeListener extends ComponentAdapter {
            public void componentResized(ComponentEvent e) {
                label.setFont(new Font("Consolas", Font.PLAIN, (int)(Math.sqrt(e.getComponent().getWidth() * e.getComponent().getWidth()
                        + e.getComponent().getHeight() * e.getComponent().getHeight()) / 4)));
                repaint();
            }
        }
        this.addComponentListener(new ResizeListener());

    }

    public void setRemainingTime(final Duration remainingTime) {
        this.remainingTime = remainingTime;
        this.revalidate();
        this.repaint();
    }

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        double angle = 360.0 * ((double) this.remainingTime.toNanos() / (double) this.totalTime.toNanos());

        Arc2D arc = new Arc2D.Double(0, 0, this.getWidth(), this.getHeight(), STARTING_POSITION_ANGLE,
                angle < 0 ? 360 : angle, 2);
        g2.setColor(angle > DANGER_TIME_ANGLE ? OK_COLOR : DANGER_COLOR);
        g2.fill(arc);

        g2.setColor(this.getBackground());
        arc = new Arc2D.Double(this.getWidth() * BORDER_SIZE, this.getHeight() * BORDER_SIZE + 0.5,
                this.getWidth() - this.getWidth() * BORDER_SIZE * 2,
                this.getHeight() - this.getHeight() * BORDER_SIZE * 2, 0, 360, 1);
        g2.fill(arc);

        this.label.setText(
                remainingTime.isNegative() ? LABEL_END_TEXT : Integer.toString((int) remainingTime.getSeconds()));

    }
}
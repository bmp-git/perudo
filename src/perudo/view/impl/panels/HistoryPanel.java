package perudo.view.impl.panels;

import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * Panel rappresenting the play's history in a game.
 */
public class HistoryPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final JTextArea textArea;

    /**
     * Initialize the history panel.
     */
    public HistoryPanel() {
        super();
        this.textArea = new JTextArea();
        this.textArea.setLineWrap(false);
        this.textArea.setEditable(false);
        this.textArea.setText("Match begin");
        this.add(textArea);
    }

    /**
     * Append info to the hystory panel.
     * 
     * @param info
     *            the info to add
     */
    public void addInfo(final String info) {
        this.textArea.append("\n" + info);
    }

}

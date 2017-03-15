package perudo.view.swing.panels.game;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import perudo.view.swing.utility.GUIFactory;
import perudo.view.swing.utility.GUIFactorySingleton;

/**
 * Panel representing the play's history in a game.
 */
public class HistoryPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String START_ROW = "Match begin";

    private final JTextArea textArea;

    /**
     * Initialize the history panel.
     */
    public HistoryPanel() {
        super();
        final GUIFactory factory = GUIFactorySingleton.getFactory();
        this.textArea = (JTextArea) factory.createTextArea();
        this.textArea.setLineWrap(false);
        this.textArea.setEditable(false);
        this.textArea.setText(START_ROW);
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

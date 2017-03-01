package perudo.view.impl.panels;

import javax.swing.JPanel;
import javax.swing.JTextArea;

public class HistoryPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final JTextArea textArea;
    public HistoryPanel() {
        super();
        this.textArea = new JTextArea();
        this.textArea.setLineWrap(false);
        this.textArea.setEditable(false);
        this.textArea.setText("Match begin");
        this.add(textArea);
    }
    
    public void addInfo(String info) {
        this.textArea.append("\n"+info);
    }

}

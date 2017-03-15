package perudo.view.console;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;

/**
 * 
 */
public class BaseForm {

    private final MultiWindowTextGUI textGUI;
    private final BasicWindow window;

    /**
     * @param textGUI
     *            to do
     */
    protected BaseForm(final MultiWindowTextGUI textGUI) {
        this.textGUI = textGUI;
        this.window = new BasicWindow();
    }

    /**
     * 
     */
    public void showDialog() {
        this.textGUI.addWindowAndWait(this.window);
    }

    /**
     * 
     */
    public void close() {
        this.window.close();
    }

    /**
     * @return to do
     */
    protected MultiWindowTextGUI getTextGUI() {
        return this.textGUI;
    }

    /**
     * @return to do
     */
    protected BasicWindow getWindow() {
        return this.window;
    }
}

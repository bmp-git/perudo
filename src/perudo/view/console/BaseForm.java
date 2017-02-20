package perudo.view.console;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;

public abstract class BaseForm implements ConsoleView {
    protected final MultiWindowTextGUI textGUI;
    protected final BasicWindow window;

    public BaseForm(MultiWindowTextGUI textGUI) {
        this.textGUI = textGUI;
        this.window = new BasicWindow();
    }

    
    
    @Override
    public void showDialog() {
        this.textGUI.addWindowAndWait(this.window);
    }

    @Override
    public void close() {
        this.window.close();
    }
}

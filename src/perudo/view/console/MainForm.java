package perudo.view.console;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.DefaultWindowManager;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.screen.Screen;

import perudo.controller.Controller;
import perudo.controller.impl.StandardControllerImpl;
import perudo.controller.net.client.ControllerClientImpl;

/**
 * 
 */
public class MainForm extends BaseForm {

    private Controller controller;

    /**
     * 
     * @param screen
     *            to do
     * @param serverName
     *            to do
     * @param port
     *            to do
     */
    public MainForm(final Screen screen, final String serverName, final int port) {
        super(new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE)));
        super.getWindow().setTitle("PERUDO - MENU");
        super.getWindow().setHints(Arrays.asList(Window.Hint.CENTERED));
        final Panel panel = new Panel();
        panel.setLayoutManager(new GridLayout(1));
        panel.addComponent(new Button("Single player", new Runnable() {
            @Override
            public void run() {
                controller = StandardControllerImpl.newStandardControllerImpl();
                close();
            }
        }));
        panel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        panel.addComponent(new Button("Online", new Runnable() {
            @Override
            public void run() {
                try {
                    controller = ControllerClientImpl.createFromServerName(serverName, port);
                    close();
                } catch (IOException e) {
                    Utils.showMessageBox("Network error", "Can't connect to " + serverName + ":" + port, getTextGUI());
                }

            }
        }));
        panel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        panel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        panel.addComponent(new Button("Exit", new Runnable() {
            @Override
            public void run() {
                controller = null;
                close();
            }
        }));
        super.getWindow().setComponent(panel);
    }

    /**
     * 
     * @return to do
     */
    public Optional<Controller> getController() {
        return Optional.ofNullable(controller);
    }
}

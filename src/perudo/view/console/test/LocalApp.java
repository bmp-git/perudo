package perudo.view.console.test;

import java.io.IOException;

import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import perudo.controller.Controller;
import perudo.controller.impl.StandardControllerImpl;
import perudo.view.console.ViewImpl;

public class LocalApp {

    public static void main(String[] args) throws IOException, InterruptedException {
        Controller controller = StandardControllerImpl.newStandardControllerImpl();
        Terminal terminal1 = new DefaultTerminalFactory().createTerminal();
        Screen screen1 = new TerminalScreen(terminal1);
        screen1.startScreen();
        ViewImpl w2 = new ViewImpl(screen1, controller);

        Thread th = new Thread(() -> {
            Screen screen = null;
            try {
                Terminal terminal = new DefaultTerminalFactory().createTerminal();
                screen = new TerminalScreen(terminal);
                screen.startScreen();
            } catch (IOException e) {
            }
            ViewImpl w1 = new ViewImpl(screen, controller);
            w1.waitEnd();
            try {
                screen.stopScreen();
            } catch (IOException e) {
            }
        });
        th.start();

        w2.waitEnd();
        screen1.stopScreen();
        th.join();
        controller.close();
        System.out.println("bye");
    }

}

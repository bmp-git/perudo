package perudo.application;

import java.io.IOException;

import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import perudo.view.console.MainForm;

/**
 * Start point of console client view.
 */
public final class LanternaClientApplication {

    private LanternaClientApplication() {

    }

    /**
     * Runs the lanterna console client.
     * 
     * @param serverName
     *            the server name if the user decides to play online.
     * @param port
     *            the server port
     * @throws IOException
     *             if something goes wrong
     */
    public static void run(final String serverName, final int port) throws IOException {
        final Terminal terminal = new DefaultTerminalFactory().createTerminal();
        final Screen screen = new TerminalScreen(terminal);
        screen.startScreen();
        while (true) {
            final MainForm main = new MainForm(screen, serverName, port);
            main.showDialog();
            if (!main.getController().isPresent()) {
                break;
            }
            final perudo.view.console.ViewImpl view = new perudo.view.console.ViewImpl(screen,
                    main.getController().get());
            view.waitEnd();

            main.getController().get().close();
            view.close();
        }
        screen.stopScreen();
    }
}

package perudo.application;

import java.io.IOException;

import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

import perudo.utility.LogSeverity;
import perudo.utility.impl.LoggerSingleton;
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
        Screen screen = null;
        try {
            screen = new TerminalScreen(new DefaultTerminalFactory().createTerminal());
        } catch (final Exception ex) {
            try {
                screen = new TerminalScreen(new DefaultTerminalFactory().createTerminalEmulator());
            } catch (final Exception ex1) {
                LoggerSingleton.get().add(LogSeverity.ERROR_UNEXPECTED, LanternaClientApplication.class,
                        "Can't create appropriate terminal");
                System.exit(1);
            }
        }
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

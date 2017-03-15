package perudo.application;

import java.io.IOException;

import perudo.view.swing.StartFrame;
import perudo.view.swing.ViewImpl;
import perudo.view.swing.utility.ControllerSingleton;

/**
 * Start point of swing client view.
 */
public final class SwingClientApplication {

    private SwingClientApplication() {

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
        while (true) {
            final StartFrame start = new StartFrame("casa-home.no-ip.biz", port);
            start.showFrame();
            start.await();
            if (!start.isInitialized()) {
                break;
            }

            final ViewImpl view = new ViewImpl();
            view.await();
            view.close();
            try {
                ControllerSingleton.getController().close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (!view.isReturnMenu()) {
                break;
            }
        }
    }
}

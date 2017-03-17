package perudo.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.Arrays;

import perudo.controller.Controller;
import perudo.controller.impl.StandardControllerImpl;
import perudo.controller.net.server.NetworkControllerImpl;
import perudo.controller.net.server.NetworkServerListener;
import perudo.controller.net.server.TcpIPv4ServerListener;
import perudo.utility.LogSeverity;
import perudo.utility.impl.LoggerSingleton;

/**
 * Start point of the server.
 */
public final class ServerApplication {

    private ServerApplication() {

    }

    /**
     * Runs the server service.
     * 
     * @param ip
     *            the interface where the server will listen
     * @param port
     *            the server port
     * @throws IOException
     *             if something goes wrong
     */
    public static void run(final String ip, final int port) throws IOException {
        LoggerSingleton.get().enable();
        NetworkServerListener listener = null;
        Controller controller = null;

        LoggerSingleton.get().add(LogSeverity.INFO, Main.class, "Starting server...");
        listener = TcpIPv4ServerListener.startNewServerListener(port, TcpIPv4ServerListener.DEFAULT_BACKLOG,
                InetAddress.getByName(ip), Arrays.asList());
        controller = NetworkControllerImpl.newNetworkController(StandardControllerImpl.newStandardControllerImpl(),
                listener);
        LoggerSingleton.get().add(LogSeverity.INFO, Main.class, "Started.");

        String cmd;
        do {
            cmd = readLine();
            if ("info".equals(cmd)) {
                LoggerSingleton.get().add(LogSeverity.INFO, Main.class, controller.toString());
            }
        } while (!("stop".equals(cmd) || "exit".equals(cmd) || "q".equals(cmd)));

        LoggerSingleton.get().add(LogSeverity.INFO, Main.class, "Closing server...");
        listener.close();
        controller.close();
        LoggerSingleton.get().add(LogSeverity.INFO, Main.class, "Closed.");
    }

    private static String readLine() {
        final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s;
        try {
            s = br.readLine();
        } catch (IOException e) {
            return null;
        }
        return s;
    }
}

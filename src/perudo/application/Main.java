package perudo.application;

import java.io.IOException;

import perudo.utility.LogSeverity;
import perudo.utility.impl.LoggerSingleton;

/**
 * Start point of perudo application.
 */
public final class Main {

    private static final String DEFAULT_HOST_NAME = "perudo.mnd.cloud";
    private static final String DEFAULT_PORT = "45555";

    private Main() {

    }

    /**
     * Start point application.
     * 
     * @param args
     *            1 argument: "client" or "server"
     * 
     *            2 argument: ip or name of the server (for the client). ip of
     *            the listening interface (for the server).
     * 
     *            3 argument: tcp port
     * 
     *            4 argument: if first is "client" you can specify "console" to
     *            start console view istead of the swing one
     */
    public static void main(final String[] args) {
        try {
            if (args.length >= 3) {
                if (args[0].equals("client")) {
                    if (args.length >= 4 && args[3].equals("console")) {
                        LanternaClientApplication.run(args[1], Integer.parseInt(args[2]));
                    } else {
                        SwingClientApplication.run(args[1], Integer.parseInt(args[2]));
                    }
                } else if (args[0].equals("server")) {
                    ServerApplication.run(args[1], Integer.parseInt(args[2]));
                }
            } else {
                SwingClientApplication.run(DEFAULT_HOST_NAME, Integer.parseInt(DEFAULT_PORT));
            }
        } catch (final IOException e) {
            LoggerSingleton.get().add(LogSeverity.ERROR_UNEXPECTED, Main.class, "Main IOException:\n" + e.getMessage());
            e.printStackTrace();
        }
        System.exit(0);
    }
}

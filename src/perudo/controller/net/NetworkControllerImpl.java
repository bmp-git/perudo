package perudo.controller.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import perudo.controller.Controller;
import perudo.controller.impl.ControllerDecorator;
import perudo.utility.ErrorTypeException;
import perudo.utility.LogSeverity;
import perudo.utility.impl.LoggerSingleton;

/**
 * A ControllerDecorator that adds a NetworkServerListener to a given
 * controller.
 */
public final class NetworkControllerImpl extends ControllerDecorator {
    private final ExecutorService executor;

    /**
     * Creates a new NetworkController.
     * 
     * @param controller
     *            the controller on which the NetworkController will invoke the
     *            calls received from the network.
     * @param serverListener
     *            the listener of new connections from the network.
     * @return the created NetworkController.
     */
    public static Controller newNetworkController(final Controller controller,
            final NetworkServerListener serverListener) {
        return new NetworkControllerImpl(controller, serverListener);
    }

    private NetworkControllerImpl(final Controller controller, final NetworkServerListener serverListener) {
        super(controller);
        this.executor = Executors.newSingleThreadExecutor();

        final BiConsumer<IOException, DatagramStream> ioExcHandler = (ex, dgs) -> {
            LoggerSingleton.get().add(LogSeverity.INFO, this.getClass(), "handling exception (calling CloseNow())");
            this.executor.execute(() -> {
                if (dgs.getUser().isPresent()) {
                    this.closeNow(dgs.getUser().get());
                } else {
                    try {
                        dgs.close();
                    } catch (IOException e) {
                        LoggerSingleton.get().add(LogSeverity.ERROR_REGULAR, this.getClass(),
                                "IOException closing DatagramStream for user " + dgs.getUser().orElse(null));
                    }
                }
            });

        };

        final MethodInvoker methodInvoker = new MethodInvoker(Controller.class);

        final BiConsumer<Datagram, DatagramStream> invoker = (dg, dgs) -> {
            this.executor.execute(() -> {
                try {
                    if (dg.getMethodName().contains("initializeNewUser")) {
                        controller.initializeNewUser(NetworkViewImpl.newNetworkView(dgs));
                    } else {
                        methodInvoker.execute(this, dg);
                    }
                } catch (ErrorTypeException e) {
                    LoggerSingleton.get().add(LogSeverity.ERROR_UNEXPECTED, this.getClass(),
                            "ErrorTypeException in methodInvoker " + e.getMessage());
                }
            });

        };

        final BiConsumer<InputStream, OutputStream> datagramStreamCreator = (is, os) -> {
            this.executor.execute(() -> {
                try {
                    DatagramStreamImpl.initializeNewDatagramStream(is, os, Arrays.asList(invoker),
                            Arrays.asList(ioExcHandler));
                } catch (IOException e) {
                    // in case of exception the client who has requested a
                    // connection is ignored
                    LoggerSingleton.get().add(LogSeverity.ERROR_UNEXPECTED, this.getClass(),
                            "IOException in initializeNewDatagramStream.");
                }
            });
        };

        serverListener.addNewConnectionObserver(datagramStreamCreator);
        serverListener.start();

    }

    @Override
    public void close() throws IOException {
        super.close();
        this.executor.shutdown();
    }
}

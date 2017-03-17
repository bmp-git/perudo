package perudo.controller.net.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

import perudo.utility.LogSeverity;
import perudo.utility.impl.LoggerSingleton;

/**
 * Listens to new connections over a TCP/IP network.
 *
 */
public final class TcpIPv4ServerListener implements NetworkServerListener {
    /**
     * The default maximum length of the queue of incoming connections.
     */
    public static final int DEFAULT_BACKLOG = 50;
    private final ExecutorService networkListener;
    private final ExecutorService notifier;
    private final ServerSocket serverSocket;
    private final List<BiConsumer<InputStream, OutputStream>> observers;
    private volatile boolean run;
    private volatile boolean started;

    /**
     * Creates a new NetworkServerListener over TCP/IP.
     * 
     * @param port
     *            the port where to listen.
     * @param observers
     *            the consumers to be executed when a new connection occurs.
     * @return the created NetworkServerListner.
     * @throws IOException
     *             can't create the NetworkServerListner.
     */
    public static NetworkServerListener startNewServerListener(final int port,
            final List<BiConsumer<InputStream, OutputStream>> observers) throws IOException {
        return new TcpIPv4ServerListener(port, observers);
    }

    /**
     * Creates a new NetworkServerListener over TCP/IP.
     * 
     * @param port
     *            the port where to listen.
     * @param backlog
     *            maximum length of the queue of incoming connections.
     * @param bindAddr
     *            the local InetAddress the server will bind to
     * @param observers
     *            the consumers to be executed when a new connection occurs.
     * @return the created NetworkServerListner.
     * @throws IOException
     *             can't create the NetworkServerListner.
     */
    public static NetworkServerListener startNewServerListener(final int port, final int backlog,
            final InetAddress bindAddr, final List<BiConsumer<InputStream, OutputStream>> observers)
            throws IOException {
        return new TcpIPv4ServerListener(port, observers);
    }

    private TcpIPv4ServerListener(final int port, final int backlog, final InetAddress bindAddr,
            final List<BiConsumer<InputStream, OutputStream>> observers) throws IOException {
        this.observers = new CopyOnWriteArrayList<>(observers);
        this.serverSocket = new ServerSocket(port, backlog, bindAddr);
        this.run = true;
        this.started = false;
        this.notifier = Executors.newSingleThreadExecutor();
        this.networkListener = Executors.newSingleThreadExecutor();
    }

    private TcpIPv4ServerListener(final int port, final List<BiConsumer<InputStream, OutputStream>> observers)
            throws IOException {
        this(port, DEFAULT_BACKLOG, null, observers);
    }

    @Override
    public void addNewConnectionObserver(final BiConsumer<InputStream, OutputStream> inOutStreamConsumer) {
        this.observers.add(inOutStreamConsumer);
    }

    @Override
    public void start() {
        if (started) {
            return;
        }
        this.started = true;
        this.networkListener.execute(() -> {
            try {
                while (run) {
                    final Socket clientSocket = serverSocket.accept();
                    LoggerSingleton.get().add(LogSeverity.INFO, this.getClass(), "new connection from "
                            + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());
                    final InputStream inStream = clientSocket.getInputStream();
                    final OutputStream outStream = clientSocket.getOutputStream();
                    this.notifier.execute(() -> {
                        this.observers.forEach(bc -> bc.accept(inStream, outStream));
                    });
                }
            } catch (IOException e) {
                LoggerSingleton.get().add(LogSeverity.ERROR_REGULAR, this.getClass(),
                        "exception while listening, probably the ServerSocket has been closed.");
            }
        });
    }

    @Override
    public void close() throws IOException {
        this.run = false;
        this.networkListener.shutdownNow();
        this.notifier.shutdownNow();
        this.serverSocket.close();
    }

}

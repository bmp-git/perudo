package perudo.controller.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

/**
 * Listens to new connections over a TCP/IP network.
 *
 */
public final class TcpIPv4ServerListener implements NetworkServerListener {
    private final ExecutorService networkListener;
    private final ExecutorService notifier;
    private final ServerSocket serverSocket;
    private final List<BiConsumer<InputStream, OutputStream>> observers;
    private volatile boolean run;

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

    private TcpIPv4ServerListener(final int port, final List<BiConsumer<InputStream, OutputStream>> observers)
            throws IOException {
        this.observers = new CopyOnWriteArrayList<>(observers);
        this.serverSocket = new ServerSocket(port);
        this.run = true;
        this.notifier = Executors.newSingleThreadExecutor();
        this.networkListener = Executors.newSingleThreadExecutor();
    }

    @Override
    public void addNewConnectionObserver(final BiConsumer<InputStream, OutputStream> inOutStreamConsumer) {
        this.observers.add(inOutStreamConsumer);
    }

    @Override
    public void start() {
        this.networkListener.execute(() -> {
            try {
                while (run) {
                    final Socket clientSocket = serverSocket.accept();
                    final InputStream inStream = clientSocket.getInputStream();
                    final OutputStream outStream = clientSocket.getOutputStream();
                    this.notifier.execute(() -> {
                        this.observers.forEach(bc -> bc.accept(inStream, outStream));
                    });
                }
            } catch (IOException e) {
                System.out.println("Server listener closed...");
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

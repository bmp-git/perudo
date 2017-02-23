package perudo.controller.net;

import java.io.Closeable;
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

public class TcpIPv4ServerListener implements NetworkServerListener, Closeable {
    private final ExecutorService networkListener;
    private final ExecutorService notifier;
    private final ServerSocket serverSocket;
    private final List<BiConsumer<InputStream, OutputStream>> observers;
    private volatile boolean run;

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
        this.networkListener.execute(() -> {
            try {
                while (run) {
                    final Socket clientSocket = serverSocket.accept();
                    final InputStream inStream = clientSocket.getInputStream();
                    final OutputStream outStream = clientSocket.getOutputStream();
                    notifier.execute(() -> {
                        observers.forEach(bc -> bc.accept(inStream, outStream));
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void addNewConnectionObserver(BiConsumer<InputStream, OutputStream> inOutStreamConsumer) {
        observers.add(inOutStreamConsumer);
    }

    @Override
    public void close() {
        this.run = false;
        this.networkListener.shutdownNow();
        this.notifier.shutdownNow();
    }

}

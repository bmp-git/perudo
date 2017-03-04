package perudo.controller.net;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

import perudo.model.User;

/**
 * A DatagramStream that can be created from a socket.
 *
 */
public class DatagramStreamSocketImpl implements DatagramStream {
    private final DatagramStream stream;
    private final Socket socket;

    /**
     * Creates a new DatagramStream.
     * 
     * @param socket
     *            the socket from which to create the stream.
     * @param observers
     *            the consumers that will be executed on a new connection.
     * @param exceptionObservers
     *            the consumers that will be executed if an exception occurs.
     * @throws IOException
     *             can't create the DatagramStream.
     */
    public DatagramStreamSocketImpl(final Socket socket, final List<BiConsumer<Datagram, DatagramStream>> observers,
            final List<BiConsumer<IOException, DatagramStream>> exceptionObservers) throws IOException {
        this.socket = socket;
        this.stream = DatagramStreamImpl.initializeNewDatagramStream(socket.getInputStream(), socket.getOutputStream(),
                observers, exceptionObservers);
    }

    @Override
    public void send(final Datagram datagram) {
        stream.send(datagram);
    }

    @Override
    public void onDatagramReceived(final BiConsumer<Datagram, DatagramStream> consumer) {
        stream.onDatagramReceived(consumer);
    }

    @Override
    public void onIOException(final BiConsumer<IOException, DatagramStream> consumer) {
        stream.onIOException(consumer);

    }

    @Override
    public Optional<User> getUser() {
        return stream.getUser();
    }

    @Override
    public void setUser(final User user) {
        stream.setUser(user);
    }

    @Override
    public void close() throws IOException {
        this.socket.close();
        this.stream.close();
    }
}

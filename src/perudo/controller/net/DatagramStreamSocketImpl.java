package perudo.controller.net;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

import perudo.model.User;

public class DatagramStreamSocketImpl implements DatagramStream {
    private final DatagramStream stream;
    private final Socket socket;

    public DatagramStreamSocketImpl(Socket socket, final List<BiConsumer<Datagram, DatagramStream>> observers,
            final List<BiConsumer<IOException, DatagramStream>> exceptionObservers) throws IOException {
        this.socket = socket;
        this.stream = DatagramStreamImpl.initializeNewDatagramStream(socket.getInputStream(), socket.getOutputStream(), observers,
                exceptionObservers);
    }

    @Override
    public void send(Datagram datagram) {
        stream.send(datagram);
    }

    @Override
    public void onDatagramReceived(BiConsumer<Datagram, DatagramStream> consumer) {
        stream.onDatagramReceived(consumer);
    }

    @Override
    public void onIOException(BiConsumer<IOException, DatagramStream> consumer) {
        stream.onIOException(consumer);

    }

    @Override
    public Optional<User> getUser() {
        return stream.getUser();
    }

    @Override
    public void setUser(User user) {
        stream.setUser(user);
    }

    @Override
    public void close() throws IOException {
        this.socket.close();
        this.stream.close();
    }
}

package perudo.controller.net;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.function.BiConsumer;

public class DatagramStreamSocketImpl implements DatagramStream {
    private final DatagramStream stream;
    private final Socket socket;

    public DatagramStreamSocketImpl(Socket socket) throws IOException {
        this.socket = socket;
        this.stream = new DatagramStreamImpl(socket.getInputStream(), socket.getOutputStream(), Arrays.asList());
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
    public void close() throws IOException{
        this.socket.close();
        this.stream.close();
    }
}

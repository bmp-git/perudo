package perudo.controller.net;

import java.io.Closeable;
import java.util.function.BiConsumer;

public interface DatagramStream extends Closeable {
	void send(Datagram datagram);

	void onDatagramReceived(BiConsumer<Datagram, DatagramStream> consumer);
}

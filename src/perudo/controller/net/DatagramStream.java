package perudo.controller.net;

import java.util.function.BiConsumer;

public interface DatagramStream {
	void send(Datagram datagram);

	void onDatagramReceived(BiConsumer<Datagram, DatagramStream> consumer);
}

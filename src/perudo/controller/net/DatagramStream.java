package perudo.controller.net;

import java.util.function.Consumer;

public interface DatagramStream {
	void send(Datagram datagram);

	void onDatagramReceived(Consumer<Datagram> consumer);
}

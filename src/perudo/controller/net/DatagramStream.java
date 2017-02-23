package perudo.controller.net;

import java.io.Closeable;
import java.io.IOException;
import java.util.Optional;
import java.util.function.BiConsumer;

import perudo.model.User;

public interface DatagramStream extends Closeable {
	void send(Datagram datagram);

	void onDatagramReceived(BiConsumer<Datagram, DatagramStream> consumer);
	
	void onIOException(BiConsumer<IOException, DatagramStream> consumer);
	
	Optional<User> getUser();
	
	void setUser(User user);
}

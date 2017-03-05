package perudo.controller.net;

import java.io.Closeable;
import java.io.IOException;
import java.util.Optional;
import java.util.function.BiConsumer;

import perudo.model.User;

/**
 * Defines a stram of Datagrams.
 *
 */
public interface DatagramStream extends Closeable {
    /**
     * Send a datagram into the stream.
     * 
     * @param datagram
     *            the datagram to be send.
     */
    void send(Datagram datagram);

    /**
     * Adds a consumer that will be executed when a datagram is received from
     * stream.
     * 
     * @param consumer
     *            the consumer to be executed.
     */
    void onDatagramReceived(BiConsumer<Datagram, DatagramStream> consumer);

    /**
     * Adds a consumer that will be executed when an IOException occurs.
     * 
     * @param consumer
     *            the consumer to be executed.
     */
    void onIOException(BiConsumer<IOException, DatagramStream> consumer);

    /**
     * Gets the user associated with this stream.
     * 
     * @return the optional of the user associated with this stream.
     */
    Optional<User> getUser();

    /**
     * Sets the user associated with this stream.
     * 
     * @param user
     *            the user to be set.
     */
    void setUser(User user);
}

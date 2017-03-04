package perudo.controller.net;

import java.io.Closeable;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.BiConsumer;

/**
 * Defines a listener of new connections.
 */
public interface NetworkServerListener extends Closeable {
    /**
     * Adds a consumer that will be executed when a new connection occurs. The
     * new connection consists of an InputStream and an OutputStream.
     * 
     * @param inOutStreamConsumer
     *            The consumer to be added.
     */
    void addNewConnectionObserver(BiConsumer<InputStream, OutputStream> inOutStreamConsumer);

    /**
     * Starts listening to new connections.
     */
    void start();
}

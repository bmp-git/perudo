package perudo.controller.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

import perudo.model.User;
import perudo.utility.LogSeverity;
import perudo.utility.impl.LoggerSingleton;

/**
 * Basic DatagramStream Implementation.
 */
public final class DatagramStreamImpl implements DatagramStream {
    private final ObjectOutputStream objOutStream;
    private final ObjectInputStream objInStream;
    private final List<BiConsumer<Datagram, DatagramStream>> observers;
    private final List<BiConsumer<IOException, DatagramStream>> exceptionObservers;
    private final ExecutorService receiver;
    private volatile Optional<User> user;
    private volatile boolean closed;

    /**
     * Creates a DatagramStream from InputStream and an OutputStream.
     * 
     * @param inStream
     *            Stream in which to write.
     * @param outStream
     *            Stream from which to read.
     * @param observers
     *            Consumers to be executed when a new datagram has been read
     *            from inStream
     * @param exceptionObservers
     *            Consumers to be executed when an exception occurs.
     * @return DatagramStream
     * @throws IOException
     *             Can't create the DatagramStream from streams given.
     */
    public static DatagramStream initializeNewDatagramStream(final InputStream inStream, final OutputStream outStream,
            final List<BiConsumer<Datagram, DatagramStream>> observers,
            final List<BiConsumer<IOException, DatagramStream>> exceptionObservers) throws IOException {
        return new DatagramStreamImpl(inStream, outStream, observers, exceptionObservers);
    }

    private DatagramStreamImpl(final InputStream inStream, final OutputStream outStream,
            final List<BiConsumer<Datagram, DatagramStream>> observers,
            final List<BiConsumer<IOException, DatagramStream>> exceptionObservers) throws IOException {

        this.user = Optional.empty();

        this.objOutStream = new ObjectOutputStream(outStream);
        this.objInStream = new ObjectInputStream(inStream);
        this.observers = new CopyOnWriteArrayList<>(observers);
        this.exceptionObservers = new CopyOnWriteArrayList<>(exceptionObservers);
        this.receiver = Executors.newSingleThreadExecutor();
        this.closed = false;

        this.receiver.execute(() -> {
            try {
                while (!this.closed) {
                    final Object readObj = this.objInStream.readObject();
                    if (!this.closed) {
                        final Datagram readDatagram = (Datagram) readObj;
                        LoggerSingleton.get().add(LogSeverity.INFO, this.getClass(),
                                "Received -> " + readDatagram.getMethodName());
                        this.observers.forEach(c -> c.accept(readDatagram, this));
                    }
                }
            } catch (IOException e) {
                LoggerSingleton.get().add(LogSeverity.ERROR_REGULAR, this.getClass(), "Exception in receive.");
                this.notifyIOException(e);
            } catch (Exception e) {
                LoggerSingleton.get().add(LogSeverity.ERROR_UNEXPECTED, this.getClass(), "Exception in receive.");
            }
        });
    }

    @Override
    public void send(final Datagram datagram) {
        if (!this.closed) {
            LoggerSingleton.get().add(LogSeverity.INFO, this.getClass(), "Send -> " + datagram.getMethodName());
            try {
                this.objOutStream.reset();
                this.objOutStream.writeObject(datagram);
                this.objOutStream.flush();
            } catch (IOException e) {
                LoggerSingleton.get().add(LogSeverity.ERROR_REGULAR, this.getClass(), "Exception in send.");
                this.notifyIOException(e);
            }
        }
    }

    @Override
    public void onDatagramReceived(final BiConsumer<Datagram, DatagramStream> consumer) {
        this.observers.add(consumer);
    }

    @Override
    public void onIOException(final BiConsumer<IOException, DatagramStream> consumer) {
        this.exceptionObservers.add(consumer);

    }

    @Override
    public Optional<User> getUser() {
        return this.user;
    }

    @Override
    public void setUser(final User user) {
        this.user = Optional.ofNullable(user);
    }

    @Override
    public void close() throws IOException {
        if (!this.closed) {
            this.closed = true;
            this.receiver.shutdown();
            this.objInStream.close();
            this.objOutStream.close();
            LoggerSingleton.get().add(LogSeverity.INFO, this.getClass(),
                    "datagramStream of user " + user.get().getName() + " shutted down!");
        }
    }

    private void notifyIOException(final IOException e) {
        if (!this.closed) {
            this.exceptionObservers.forEach(c -> c.accept(e, this));
        }
    }

}

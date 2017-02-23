package perudo.controller.net;

import java.io.Closeable;
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

public class DatagramStreamImpl implements DatagramStream, Closeable {
    private final ObjectInputStream objInStream;
    private final ObjectOutputStream objOutStream;
    private final List<BiConsumer<Datagram, DatagramStream>> observers;
    private final List<BiConsumer<IOException, DatagramStream>> exceptionObservers;
    private final ExecutorService sender;
    private final ExecutorService receiver;
    private final ExecutorService notifier;
    private Optional<User> user;
    private volatile boolean read;

    public static DatagramStream initializeNewDatagramStream(final InputStream inStream, final OutputStream outStream,
            final List<BiConsumer<Datagram, DatagramStream>> observers,
            final List<BiConsumer<IOException, DatagramStream>> exceptionObservers) throws IOException {
        return new DatagramStreamImpl(inStream, outStream, observers, exceptionObservers);
    }

    public DatagramStreamImpl(final InputStream inStream, final OutputStream outStream,
            final List<BiConsumer<Datagram, DatagramStream>> observers,
            final List<BiConsumer<IOException, DatagramStream>> exceptionObservers) throws IOException {
        this.user = Optional.empty();
        this.objInStream = new ObjectInputStream(inStream);
        this.objOutStream = new ObjectOutputStream(outStream);
        this.observers = new CopyOnWriteArrayList<>(observers);
        this.exceptionObservers = new CopyOnWriteArrayList<>(exceptionObservers);
        this.sender = Executors.newSingleThreadExecutor();
        this.receiver = Executors.newSingleThreadExecutor();
        this.notifier = Executors.newSingleThreadExecutor();
        this.read = true;

        receiver.execute(() -> {
            while (read) {
                try {
                    final Object readObj = this.objInStream.readObject();
                    final Datagram readDatagram = (Datagram) readObj;
                    notifier.execute(() -> {
                        observers.forEach(c -> c.accept(readDatagram, this));
                    });
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    notifier.execute( () -> {
                        exceptionObservers.forEach( c -> c.accept(e, this));
                    });
                }
            }

        });
    }

    @Override
    public void send(final Datagram datagram) {
        sender.execute(() -> {
            try {
                this.objOutStream.writeObject(datagram);
                this.objOutStream.flush();
            } catch (IOException e) {
                notifier.execute( () -> {
                    exceptionObservers.forEach( c -> c.accept(e, this));
                });
            }
        });

    }

    @Override
    public void onDatagramReceived(final BiConsumer<Datagram, DatagramStream> consumer) {
        observers.add(consumer);
    }

    @Override
    public void onIOException(BiConsumer<IOException, DatagramStream> consumer) {
        exceptionObservers.add(consumer);

    }

    @Override
    public void close() throws IOException {
        this.read = false;
        this.sender.shutdownNow();
        this.receiver.shutdownNow();
        this.notifier.shutdownNow();
        this.objInStream.close();
        this.objOutStream.close();
    }

    @Override
    public Optional<User> getUser() {
        return this.user;
    }

    @Override
    public void setUser(User user) {
        this.user = Optional.ofNullable(user);
    }

}

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

public class DatagramStreamImpl implements DatagramStream {
    private final ObjectOutputStream objOutStream;
    private final InputStream inStream;
    private final List<BiConsumer<Datagram, DatagramStream>> observers;
    private final List<BiConsumer<IOException, DatagramStream>> exceptionObservers;
    //private final ExecutorService sender;
    private final ExecutorService receiver;
    private final ExecutorService notifier;
    private volatile Optional<User> user;
    private volatile boolean read;

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
        this.inStream = inStream;
        this.observers = new CopyOnWriteArrayList<>(observers);
        this.exceptionObservers = new CopyOnWriteArrayList<>(exceptionObservers);
        //this.sender = Executors.newSingleThreadExecutor();
        this.receiver = Executors.newSingleThreadExecutor();
        this.notifier = Executors.newSingleThreadExecutor();
        this.read = true;

        this.receiver.execute(() -> {
            try (ObjectInputStream objInStream = new ObjectInputStream(this.inStream)) {
                while (read) {
                    final Object readObj = objInStream.readObject();
                    if (read) {
                        final Datagram readDatagram = (Datagram) readObj;
                        // TODO for debug
                        System.out.println("Received -> " + readDatagram.getMethodName());
                        this.notifier.execute(() -> {
                            this.observers.forEach(c -> c.accept(readDatagram, this));
                        });
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                this.notifier.execute(() -> {
                    this.exceptionObservers.forEach(c -> c.accept(e, this));
                });
            }
        });
    }

    @Override
    public void send(final Datagram datagram) {
        //this.sender.execute(() -> {
            // TODO for debug
            System.out.println("Send -> " + datagram.getMethodName());
            try {
                objOutStream.reset();
                objOutStream.writeObject(datagram);
                objOutStream.flush();
            } catch (IOException e) {
                this.notifier.execute(() -> {
                    this.exceptionObservers.forEach(c -> c.accept(e, this));
                });
            }
        //});

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
        this.read = false;
        //this.sender.shutdownNow();
        this.receiver.shutdownNow();
        this.notifier.shutdownNow();

        // TODO for debug
        System.out.println("DatagramStream: datagramStream of user " + user.get().getName() + " shutted down!");
    }

}

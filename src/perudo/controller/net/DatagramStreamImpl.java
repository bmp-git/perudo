package perudo.controller.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class DatagramStreamImpl implements DatagramStream {
    private final ObjectInputStream objInStream;
    private final ObjectOutputStream objOutStream;
    private final List<Consumer<Datagram>> observers;
    private final ExecutorService sender;
    private final ExecutorService receiver;
    private final ExecutorService notifier;
    private volatile boolean read;

    public static DatagramStream initializeNewDatagramStream(final InputStream inStream, final OutputStream outStream,
            final List<Consumer<Datagram>> observers) throws IOException {
        return new DatagramStreamImpl(inStream, outStream, observers);
    }

    public DatagramStreamImpl(final InputStream inStream, final OutputStream outStream,
            final List<Consumer<Datagram>> observers) throws IOException {
        this.objInStream = new ObjectInputStream(inStream);
        this.objOutStream = new ObjectOutputStream(outStream);
        this.observers = new CopyOnWriteArrayList<>(observers);
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
                        observers.forEach(c -> c.accept(readDatagram));
                    });
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
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
                e.printStackTrace();
            }
        });

    }

    @Override
    public void onDatagramReceived(final Consumer<Datagram> consumer) {
        observers.add(consumer);
    }

}

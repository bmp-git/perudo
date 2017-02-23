package perudo.controller.net;

import java.io.Closeable;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.BiConsumer;

public interface NetworkServerListener extends Closeable{
    void addNewConnectionObserver(BiConsumer<InputStream, OutputStream> inOutStreamConsumer);

    void start();
}

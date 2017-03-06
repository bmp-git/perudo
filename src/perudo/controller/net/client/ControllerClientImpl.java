package perudo.controller.net.client;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

import perudo.controller.Controller;
import perudo.controller.net.Datagram;
import perudo.controller.net.DatagramStream;
import perudo.controller.net.DatagramStreamImpl;
import perudo.controller.net.MethodInvoker;
import perudo.model.Bid;
import perudo.model.GameSettings;
import perudo.model.Lobby;
import perudo.model.User;
import perudo.model.UserType;
import perudo.utility.DiffTime;
import perudo.utility.ErrorTypeException;
import perudo.view.View;

/**
 * A Network implementation of Controller.
 */
public class ControllerClientImpl implements Controller {

    /**
     * Create a network controller from host and port.
     * 
     * @param host
     *            the server name
     * 
     * @param port
     *            the server port
     * 
     * @throws IOException
     *             if the connection is impossible
     * 
     * @return the instance of initialized Controller
     */
    public static Controller createFromServerName(final String host, final int port) throws IOException {
        Socket socket = new Socket(host, port);
        return new ControllerClientImpl(socket);
    }

    private final DatagramStream stream;
    private View view;
    private final ExecutorService executor;
    private final MethodInvoker invoker;

    /**
     * Create a network controller from socket.
     * 
     * @param socket
     *            the connection to the server
     * 
     * @throws IOException
     *             if the connection is impossible
     */
    public ControllerClientImpl(final Socket socket) throws IOException {

        this.view = null;
        this.executor = Executors.newSingleThreadExecutor();
        this.invoker = new MethodInvoker(View.class);

        BiConsumer<Datagram, DatagramStream> receiver = (datagram, dgStream) -> {
            this.executor.execute(() -> {
                try {
                    DiffTime.setServerDiffTime(Duration.between(Instant.now(), datagram.getCreationTime()));
                    this.invoker.execute(view, datagram);
                } catch (final ErrorTypeException e) {
                    this.view.showError(e.getErrorType());
                }
            });
        };
        BiConsumer<IOException, DatagramStream> ioExcHandler = (exception, dgStream) -> {
            try {
                if (this.view != null) {
                    this.view.close();
                }
                this.close();
            } catch (IOException ex) {

            }
        };
        this.stream = DatagramStreamImpl.initializeNewDatagramStream(socket.getInputStream(), socket.getOutputStream(),
                Arrays.asList(receiver), Arrays.asList(ioExcHandler));
    }

    @Override
    public void initializeNewUser(final View view) {
        if (this.view != null) {
            throw new IllegalStateException("Only one view is allowed.");
        }
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(View.class),
                Arrays.asList((Serializable) null));
        this.view = new ViewClientImpl(view, this.stream);
        this.executor.execute(() -> {
            stream.send(dg);
        });
    }

    @Override
    public void changeUserName(final User user, final String name) {
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(User.class, String.class),
                Arrays.asList(user, name));
        this.executor.execute(() -> {
            stream.send(dg);
        });
    }

    @Override
    public void getUsers(final User user) {
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(User.class), Arrays.asList(user));
        this.executor.execute(() -> {
            stream.send(dg);
        });
    }

    @Override
    public void createLobby(final User user, final GameSettings info) {
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(User.class, GameSettings.class),
                Arrays.asList(user, info));
        this.executor.execute(() -> {
            stream.send(dg);
        });
    }

    @Override
    public void getLobbies(final User user) {
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(User.class), Arrays.asList(user));
        this.executor.execute(() -> {
            stream.send(dg);
        });
    }

    @Override
    public void joinLobby(final User user, final Lobby lobby) {
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(User.class, Lobby.class),
                Arrays.asList(user, lobby));
        this.executor.execute(() -> {
            stream.send(dg);
        });
    }

    @Override
    public void addBotToLobby(final User user, final Lobby lobby, final UserType type) {
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(User.class, Lobby.class, UserType.class),
                Arrays.asList(user, lobby, type));
        this.executor.execute(() -> {
            stream.send(dg);
        });

    }

    @Override
    public void exitLobby(final User user) {
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(User.class), Arrays.asList(user));
        this.executor.execute(() -> {
            stream.send(dg);
        });
    }

    @Override
    public void startLobby(final User user) {
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(User.class), Arrays.asList(user));
        this.executor.execute(() -> {
            stream.send(dg);
        });
    }

    @Override
    public void getGames(final User user) {
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(User.class), Arrays.asList(user));
        this.executor.execute(() -> {
            stream.send(dg);
        });
    }

    @Override
    public void play(final User user, final Bid bid) {
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(User.class, Bid.class),
                Arrays.asList(user, bid));
        this.executor.execute(() -> {
            stream.send(dg);
        });
    }

    @Override
    public void doubt(final User user) {
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(User.class), Arrays.asList(user));
        this.executor.execute(() -> {
            stream.send(dg);
        });
    }

    @Override
    public void urge(final User user) {
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(User.class), Arrays.asList(user));
        this.executor.execute(() -> {
            stream.send(dg);
        });
    }

    @Override
    public void callPalifico(final User user) {
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(User.class), Arrays.asList(user));
        this.executor.execute(() -> {
            stream.send(dg);
        });
    }

    @Override
    public void exitGame(final User user) {
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(User.class), Arrays.asList(user));
        this.executor.execute(() -> {
            stream.send(dg);
        });
    }

    @Override
    public void close(final User user) {
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(User.class), Arrays.asList(user));
        this.executor.execute(() -> {
            stream.send(dg);
        });
    }

    @Override
    public void closeNow(final User user) {
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(User.class), Arrays.asList(user));
        this.executor.execute(() -> {
            stream.send(dg);
        });
    }

    @Override
    public void close() throws IOException {
        this.executor.shutdownNow();
        this.stream.close();
    }

}

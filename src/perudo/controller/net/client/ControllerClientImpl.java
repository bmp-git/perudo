package perudo.controller.net.client;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
import perudo.utility.ErrorType;
import perudo.utility.ErrorTypeException;
import perudo.utility.LogSeverity;
import perudo.utility.impl.LoggerSingleton;
import perudo.view.View;

/**
 * A Network implementation of Controller.
 */
public final class ControllerClientImpl implements Controller {

    private final Map<DatagramStream, View> streams;
    private final Map<User, DatagramStream> users;
    private final String host;
    private final int port;
    private final ExecutorService executor;
    private final MethodInvoker invoker;

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
    public static Controller createFromServerName(final String host, final int port) {
        return new ControllerClientImpl(host, port);
    }

    private ControllerClientImpl(final String host, final int port) {
        this.host = host;
        this.port = port;
        this.streams = new HashMap<>();
        this.users = new HashMap<>();
        this.executor = Executors.newSingleThreadExecutor();
        this.invoker = new MethodInvoker(View.class);
    }

    // when receive a datagram
    private void receiver(final Datagram datagram, final DatagramStream dgStream) {
        this.executor.execute(() -> {
            try {
                DiffTime.setServerDiffTime(Duration.between(Instant.now(), datagram.getCreationTime()));

                invoker.execute(this.streams.get(dgStream), datagram);

                if (dgStream.getUser().isPresent() && !this.users.containsKey(dgStream.getUser().get())) {
                    this.users.put(dgStream.getUser().get(), dgStream);
                    LoggerSingleton.get().add(LogSeverity.INFO, this.getClass(),
                            dgStream.getUser().get().getName() + " has been initialized.");
                }
            } catch (final ErrorTypeException e) {
                this.streams.get(dgStream).showError(e.getErrorType());
            }
        });
    }

    // when an error occured while connected
    private void ioExcHandler(final IOException e, final DatagramStream dgStream) {
        try {
            this.streams.get(dgStream).close();
            dgStream.close();
            this.streams.remove(dgStream);
            if (dgStream.getUser().isPresent() && this.users.containsKey(dgStream.getUser().get())) {
                this.users.remove(dgStream.getUser().get());
            }
            LoggerSingleton.get().add(LogSeverity.INFO, this.getClass(),
                    "The user " + (dgStream.getUser().isPresent() ? dgStream.getUser().get().getName() : "[NULL]")
                            + " has been removed. Exception " + e.getMessage());
        } catch (final IOException ex) {
            LoggerSingleton.get().add(LogSeverity.ERROR_UNEXPECTED, this.getClass(),
                    "exception during closing a stream/view.\n" + ex.getMessage());
        }
    }

    private DatagramStream createDatagramStreamFromSocket(final Socket socket) throws IOException {
        return DatagramStreamImpl.initializeNewDatagramStream(socket.getInputStream(), socket.getOutputStream(),
                Arrays.asList((d, s) -> receiver(d, s)), Arrays.asList((e, s) -> ioExcHandler(e, s)));
    }

    @Override
    public void initializeNewUser(final View view) {
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(View.class),
                Arrays.asList((Serializable) null));

        this.executor.execute(() -> {
            try {
                final DatagramStream stream = createDatagramStreamFromSocket(new Socket(host, port));
                this.streams.put(stream, new ViewClientImpl(view, stream));
                stream.send(dg);
            } catch (final IOException e) {
                try {
                    LoggerSingleton.get().add(LogSeverity.ERROR_UNEXPECTED, this.getClass(),
                            "Problem while connecting.\n" + e.getMessage());
                    view.showError(ErrorType.CONNECTION_REJECTED_FROM_SERVER);
                    view.close();
                } catch (final IOException e1) {
                    LoggerSingleton.get().add(LogSeverity.ERROR_UNEXPECTED, this.getClass(),
                            "Problem while closing view.\n" + e.getMessage());
                }
            }
        });
    }

    private void send(final User user, final Datagram dg) {
        if (this.users.containsKey(user)) {
            this.users.get(user).send(dg);
        } else {
            // TODO for closing bot (temporary solution)
            this.streams.keySet().stream().limit(1).forEach(s -> s.send(dg));
            LoggerSingleton.get().add(LogSeverity.ERROR_REGULAR, this.getClass(),
                    "The user " + (user == null ? "NULL" : user.getName())
                            + " is not been initializated yet. Datagram: " + dg.getMethodName());
        }
    }

    @Override
    public void changeUserName(final User user, final String name) {
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(User.class, String.class),
                Arrays.asList(user, name));
        this.executor.execute(() -> {
            this.send(user, dg);
        });
    }

    @Override
    public void getUsers(final User user) {
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(User.class), Arrays.asList(user));
        this.executor.execute(() -> {
            this.send(user, dg);
        });
    }

    @Override
    public void createLobby(final User user, final GameSettings info) {
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(User.class, GameSettings.class),
                Arrays.asList(user, info));
        this.executor.execute(() -> {
            this.send(user, dg);
        });
    }

    @Override
    public void getLobbies(final User user) {
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(User.class), Arrays.asList(user));
        this.executor.execute(() -> {
            this.send(user, dg);
        });
    }

    @Override
    public void joinLobby(final User user, final Lobby lobby) {
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(User.class, Lobby.class),
                Arrays.asList(user, lobby));
        this.executor.execute(() -> {
            this.send(user, dg);
        });
    }

    @Override
    public void addBotToLobby(final User user, final Lobby lobby, final UserType type) {
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(User.class, Lobby.class, UserType.class),
                Arrays.asList(user, lobby, type));
        this.executor.execute(() -> {
            this.send(user, dg);
        });

    }

    @Override
    public void exitLobby(final User user) {
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(User.class), Arrays.asList(user));
        this.executor.execute(() -> {
            this.send(user, dg);
        });
    }

    @Override
    public void startLobby(final User user) {
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(User.class), Arrays.asList(user));
        this.executor.execute(() -> {
            this.send(user, dg);
        });
    }

    @Override
    public void getGames(final User user) {
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(User.class), Arrays.asList(user));
        this.executor.execute(() -> {
            this.send(user, dg);
        });
    }

    @Override
    public void play(final User user, final Bid bid) {
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(User.class, Bid.class),
                Arrays.asList(user, bid));
        this.executor.execute(() -> {
            this.send(user, dg);
        });
    }

    @Override
    public void doubt(final User user) {
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(User.class), Arrays.asList(user));
        this.executor.execute(() -> {
            this.send(user, dg);
        });
    }

    @Override
    public void urge(final User user) {
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(User.class), Arrays.asList(user));
        this.executor.execute(() -> {
            this.send(user, dg);
        });
    }

    @Override
    public void callPalifico(final User user) {
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(User.class), Arrays.asList(user));
        this.executor.execute(() -> {
            this.send(user, dg);
        });
    }

    @Override
    public void exitGame(final User user) {
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(User.class), Arrays.asList(user));
        this.executor.execute(() -> {
            this.send(user, dg);
        });
    }

    @Override
    public void close(final User user) {
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(User.class), Arrays.asList(user));
        this.executor.execute(() -> {
            this.send(user, dg);
        });
    }

    @Override
    public void closeNow(final User user) {
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(User.class), Arrays.asList(user));
        this.executor.execute(() -> {
            this.send(user, dg);
        });
    }

    @Override
    public void close() throws IOException {
        this.executor.shutdownNow();
        this.streams.keySet().forEach(s -> {
            try {
                s.close();
            } catch (IOException e) {
                LoggerSingleton.get().add(LogSeverity.ERROR_UNEXPECTED, this.getClass(),
                        "Error while closeing the controller\n" + e.getMessage());
            }
        });
        LoggerSingleton.get().add(LogSeverity.INFO, this.getClass(), "Closed.");
    }

}

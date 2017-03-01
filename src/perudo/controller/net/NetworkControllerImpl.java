package perudo.controller.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import perudo.controller.Controller;
import perudo.controller.net.Datagram;
import perudo.controller.net.DatagramStream;
import perudo.controller.net.DatagramStreamImpl;
import perudo.controller.net.MethodInvoker;
import perudo.controller.net.NetworkServerListener;
import perudo.model.Bid;
import perudo.model.GameSettings;
import perudo.model.Lobby;
import perudo.model.User;
import perudo.model.UserType;
import perudo.utility.ErrorTypeException;
import perudo.view.View;

public class NetworkControllerImpl implements Controller {
    private final Controller controller;
    private final MethodInvoker methodInvoker;
    private final List<DatagramStream> streams;
    private final BiConsumer<InputStream, OutputStream> datagramStreamCreator;
    private final BiConsumer<Datagram, DatagramStream> invoker;
    private final BiConsumer<IOException, DatagramStream> ioExcHandler;
    private final ExecutorService executor;

    public static Controller newNetworkController(Controller controller, NetworkServerListener serverListener) {
        return new NetworkControllerImpl(controller, serverListener);
    }

    private NetworkControllerImpl(Controller controller, NetworkServerListener serverListener) {
        this.controller = controller;
        this.methodInvoker = new MethodInvoker(Controller.class);
        this.streams = new CopyOnWriteArrayList<>();
        this.executor = Executors.newSingleThreadExecutor();

        this.ioExcHandler = (ex, dgs) -> {
            this.executor.execute(() -> {
                if (dgs.getUser().isPresent()) {
                    this.closeNow(dgs.getUser().get());
                }
            });

        };

        this.invoker = (dg, dgs) -> {
            this.executor.execute(() -> {
                try {
                    if (dg.getMethodName().contains("initializeNewUser")) {
                        controller.initializeNewUser(NetworkViewImpl.newNetworkView(dgs));
                    } else {
                        methodInvoker.execute(this, dg);
                    }
                } catch (ErrorTypeException e) {
                    e.printStackTrace();
                }
            });

        };

        this.datagramStreamCreator = (is, os) -> {
            this.executor.execute(() -> {
                try {
                    final DatagramStream datagramStream = DatagramStreamImpl.initializeNewDatagramStream(is, os,
                            Arrays.asList(this.invoker), Arrays.asList(this.ioExcHandler));
                    this.streams.add(datagramStream);
                } catch (IOException e) {
                    // in case of exception the client who has requested a
                    // connection is ignored
                    e.printStackTrace();
                }
            });
        };

        serverListener.addNewConnectionObserver(this.datagramStreamCreator);
        serverListener.start();

    }

    @Override
    public void initializeNewUser(final View view) {
        this.controller.initializeNewUser(view);

    }

    @Override
    public void changeUserName(final User user, final String name) {
        this.controller.changeUserName(user, name);

    }

    @Override
    public void getUsers(final User user) {
        this.controller.getUsers(user);

    }

    @Override
    public void createLobby(final User user, final GameSettings info) {
        this.controller.createLobby(user, info);

    }

    @Override
    public void getLobbies(final User user) {
        this.controller.getLobbies(user);

    }

    @Override
    public void joinLobby(final User user, final Lobby lobby) {
        this.controller.joinLobby(user, lobby);

    }

    @Override
    public void addBotToLobby(final User user, final Lobby lobby, final UserType type) {
        this.controller.addBotToLobby(user, lobby, type);
    }

    @Override
    public void exitLobby(final User user) {
        this.controller.exitLobby(user);

    }

    @Override
    public void startLobby(final User user) {
        this.controller.startLobby(user);

    }

    @Override
    public void getGames(final User user) {
        this.controller.getGames(user);

    }

    @Override
    public void play(final User user, final Bid bid) {
        this.controller.play(user, bid);

    }

    @Override
    public void doubt(final User user) {
        this.controller.doubt(user);

    }

    @Override
    public void urge(final User user) {
        this.controller.urge(user);

    }

    @Override
    public void callPalifico(final User user) {
        this.controller.callPalifico(user);

    }

    @Override
    public void exitGame(final User user) {
        this.controller.exitGame(user);

    }

    @Override
    public void close(final User user) {
        this.controller.close(user);
    }

    @Override
    public void closeNow(final User user) {
        /*
         * try { this.streams.stream().filter(s -> Objects.equals(user,
         * s.getUser().orElse(null)))
         * .collect(Collectors.toList()).get(0).close(); } catch (IOException e)
         * { e.printStackTrace(); }
         */
        this.controller.closeNow(user);
    }

    @Override
    public void close() throws IOException {
        this.controller.close();
        this.executor.shutdown();
    }
}

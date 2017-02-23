package perudo.controller.net;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import perudo.controller.Controller;
import perudo.model.Bid;
import perudo.model.GameSettings;
import perudo.model.Lobby;
import perudo.model.User;
import perudo.utility.ErrorTypeException;
import perudo.view.View;

public class ControllerClientImpl implements Controller, Closeable {

    public static Controller createFromIPv4(String host, int port) throws IOException {
        Socket conn = new Socket(host, port);
        return new ControllerClientImpl(new DatagramStreamSocketImpl(conn));
    }

    private final DatagramStream stream;
    private View view;
    private final ExecutorService executor;
    private final MethodInvoker invoker;

    public ControllerClientImpl(final DatagramStream stream) {
        this.stream = stream;
        this.view = null;
        this.executor = Executors.newSingleThreadExecutor();
        this.invoker = new MethodInvoker(View.class);

        this.stream.onDatagramReceived((datagram, dgStream) -> {
            this.executor.execute(() -> {
                try {
                    this.invoker.execute(view, datagram);
                } catch (final ErrorTypeException e) {
                    this.view.showError(e.getErrorType());
                }
            });
        });
        /*
         * this.stream.onClose(e -> { this.executor.execute(() -> { if
         * (e.isPresent()) { this.view.showError(e.get()); }
         * 
         * try { this.close(); } catch (IOException e1) { // TODO Auto-generated
         * catch block e1.printStackTrace(); } }); });
         */
    }

    @Override
    public void initializeNewUser(final View view) {
        if (this.view != null) {
            throw new IllegalStateException("Only one view is allowed.");
        }
        this.view = view;
        this.executor.execute(() -> {
            stream.send(Datagram.createCurrentMethodDatagram(Arrays.asList(View.class),
                    Arrays.asList((Serializable) null)));
        });
    }

    @Override
    public void changeUserName(final User user, final String name) {
        this.executor.execute(() -> {
            stream.send(Datagram.createCurrentMethodDatagram(Arrays.asList(User.class, String.class),
                    Arrays.asList(user, name)));
        });
    }

    @Override
    public void getUsers(final User user) {
        this.executor.execute(() -> {
            stream.send(Datagram.createCurrentMethodDatagram(Arrays.asList(User.class), Arrays.asList(user)));
        });
    }

    @Override
    public void createLobby(final User user, final GameSettings info) {
        this.executor.execute(() -> {
            stream.send(Datagram.createCurrentMethodDatagram(Arrays.asList(User.class, GameSettings.class),
                    Arrays.asList(user, info)));
        });
    }

    @Override
    public void getLobbies(final User user) {
        this.executor.execute(() -> {
            stream.send(Datagram.createCurrentMethodDatagram(Arrays.asList(User.class), Arrays.asList(user)));
        });
    }

    @Override
    public void joinLobby(final User user, final Lobby lobby) {
        this.executor.execute(() -> {
            stream.send(Datagram.createCurrentMethodDatagram(Arrays.asList(User.class, Lobby.class),
                    Arrays.asList(user, lobby)));
        });
    }

    @Override
    public void exitLobby(final User user) {
        this.executor.execute(() -> {
            stream.send(Datagram.createCurrentMethodDatagram(Arrays.asList(User.class), Arrays.asList(user)));
        });
    }

    @Override
    public void startLobby(final User user) {
        this.executor.execute(() -> {
            stream.send(Datagram.createCurrentMethodDatagram(Arrays.asList(User.class), Arrays.asList(user)));
        });
    }

    @Override
    public void getGames(final User user) {
        this.executor.execute(() -> {
            stream.send(Datagram.createCurrentMethodDatagram(Arrays.asList(User.class), Arrays.asList(user)));
        });
    }

    @Override
    public void play(final User user, final Bid bid) {
        this.executor.execute(() -> {
            stream.send(Datagram.createCurrentMethodDatagram(Arrays.asList(User.class, Bid.class),
                    Arrays.asList(user, bid)));
        });
    }

    @Override
    public void doubt(final User user) {
        this.executor.execute(() -> {
            stream.send(Datagram.createCurrentMethodDatagram(Arrays.asList(User.class), Arrays.asList(user)));
        });
    }

    @Override
    public void urge(final User user) {
        this.executor.execute(() -> {
            stream.send(Datagram.createCurrentMethodDatagram(Arrays.asList(User.class), Arrays.asList(user)));
        });
    }

    @Override
    public void callPalifico(final User user) {
        this.executor.execute(() -> {
            stream.send(Datagram.createCurrentMethodDatagram(Arrays.asList(User.class), Arrays.asList(user)));
        });
    }

    @Override
    public void exitGame(final User user) {
        this.executor.execute(() -> {
            stream.send(Datagram.createCurrentMethodDatagram(Arrays.asList(User.class), Arrays.asList(user)));
        });
    }

    @Override
    public void close(final User user) {
        this.executor.execute(() -> {
            stream.send(Datagram.createCurrentMethodDatagram(Arrays.asList(User.class), Arrays.asList(user)));
        });
    }

    @Override
    public void closeNow(User user) {
        this.executor.execute(() -> {
            stream.send(Datagram.createCurrentMethodDatagram(Arrays.asList(User.class), Arrays.asList(user)));
        });        
    }
    
    @Override
    public void close() throws IOException {
        this.executor.shutdownNow();
        this.stream.close();
    }

}

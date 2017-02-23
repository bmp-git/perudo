package perudo.controller.net;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import perudo.controller.net.Datagram;
import perudo.controller.net.DatagramStream;
import perudo.model.Game;
import perudo.model.Lobby;
import perudo.model.User;
import perudo.utility.ErrorType;
import perudo.utility.Response;
import perudo.view.View;

public class NetworkViewImpl implements View, Closeable {
    private final DatagramStream datagramStream;
    private final ExecutorService executor;

    public static View newNetworkView(final DatagramStream datagramStream) {
        return new NetworkViewImpl(datagramStream);
    }

    private NetworkViewImpl(final DatagramStream datagramStream) {
        this.datagramStream = datagramStream;
        this.executor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void initializeNewUserRespond(final Response<User> user) {
        final Method m = new Object() {
        }.getClass().getEnclosingMethod();
        this.executor.execute(() -> {
            this.datagramStream.send(
                    Datagram.createCurrentMethodDatagram(Arrays.asList(m.getParameterTypes()), Arrays.asList(user)));
        });

    }

    @Override
    public void initializeNewUserNotify(final User user) {
        final Method m = new Object() {
        }.getClass().getEnclosingMethod();
        this.executor.execute(() -> {
            this.datagramStream.send(
                    Datagram.createCurrentMethodDatagram(Arrays.asList(m.getParameterTypes()), Arrays.asList(user)));
        });

    }

    @Override
    public void userExitNotify(final User user) {
        final Method m = new Object() {
        }.getClass().getEnclosingMethod();
        this.executor.execute(() -> {
            this.datagramStream.send(
                    Datagram.createCurrentMethodDatagram(Arrays.asList(m.getParameterTypes()), Arrays.asList(user)));
        });

    }

    @Override
    public void changeNameNotify(final User oldUser, final User newUser) {
        final Method m = new Object() {
        }.getClass().getEnclosingMethod();
        this.executor.execute(() -> {
            this.datagramStream.send(Datagram.createCurrentMethodDatagram(Arrays.asList(m.getParameterTypes()),
                    Arrays.asList(oldUser, newUser)));

        });

    }

    @Override
    public void getUsersRespond(final Response<Set<User>> users) {
        final Method m = new Object() {
        }.getClass().getEnclosingMethod();
        this.executor.execute(() -> {
            this.datagramStream.send(
                    Datagram.createCurrentMethodDatagram(Arrays.asList(m.getParameterTypes()), Arrays.asList(users)));
        });

    }

    @Override
    public void createLobbyNotify(final Lobby lobby) {
        final Method m = new Object() {
        }.getClass().getEnclosingMethod();
        this.executor.execute(() -> {
            this.datagramStream.send(
                    Datagram.createCurrentMethodDatagram(Arrays.asList(m.getParameterTypes()), Arrays.asList(lobby)));
        });

    }

    @Override
    public void removeLobbyNotify(final Lobby lobby) {
        final Method m = new Object() {
        }.getClass().getEnclosingMethod();
        this.executor.execute(() -> {
            this.datagramStream.send(
                    Datagram.createCurrentMethodDatagram(Arrays.asList(m.getParameterTypes()), Arrays.asList(lobby)));
        });

    }

    @Override
    public void getLobbiesRespond(final Response<Set<Lobby>> lobbies) {
        final Method m = new Object() {
        }.getClass().getEnclosingMethod();
        this.executor.execute(() -> {
            this.datagramStream.send(
                    Datagram.createCurrentMethodDatagram(Arrays.asList(m.getParameterTypes()), Arrays.asList(lobbies)));
        });

    }

    @Override
    public void joinLobbyNotify(final Lobby lobby, final User user) {
        final Method m = new Object() {
        }.getClass().getEnclosingMethod();
        this.executor.execute(() -> {
            this.datagramStream.send(Datagram.createCurrentMethodDatagram(Arrays.asList(m.getParameterTypes()),
                    Arrays.asList(lobby, user)));
        });

    }

    @Override
    public void exitLobbyNotify(final Lobby lobby, final User user) {
        final Method m = new Object() {
        }.getClass().getEnclosingMethod();
        this.executor.execute(() -> {
            this.datagramStream.send(Datagram.createCurrentMethodDatagram(Arrays.asList(m.getParameterTypes()),
                    Arrays.asList(lobby, user)));
        });

    }

    @Override
    public void startLobbyNotify(final Lobby lobby, final Game game) {
        final Method m = new Object() {
        }.getClass().getEnclosingMethod();
        this.executor.execute(() -> {
            this.datagramStream.send(Datagram.createCurrentMethodDatagram(Arrays.asList(m.getParameterTypes()),
                    Arrays.asList(lobby, game)));
        });

    }

    @Override
    public void removeGameNotify(final Game game) {
        final Method m = new Object() {
        }.getClass().getEnclosingMethod();
        this.executor.execute(() -> {
            this.datagramStream.send(
                    Datagram.createCurrentMethodDatagram(Arrays.asList(m.getParameterTypes()), Arrays.asList(game)));
        });

    }

    @Override
    public void getGamesRespond(final Response<Set<Game>> games) {
        final Method m = new Object() {
        }.getClass().getEnclosingMethod();
        this.executor.execute(() -> {
            this.datagramStream.send(
                    Datagram.createCurrentMethodDatagram(Arrays.asList(m.getParameterTypes()), Arrays.asList(games)));
        });

    }

    @Override
    public void playNotify(final Game game, final User user) {
        final Method m = new Object() {
        }.getClass().getEnclosingMethod();
        this.executor.execute(() -> {
            this.datagramStream.send(Datagram.createCurrentMethodDatagram(Arrays.asList(m.getParameterTypes()),
                    Arrays.asList(game, user)));
        });

    }

    @Override
    public void doubtNotify(final Game game, final User user, final boolean win) {
        final Method m = new Object() {
        }.getClass().getEnclosingMethod();
        this.executor.execute(() -> {
            this.datagramStream.send(Datagram.createCurrentMethodDatagram(Arrays.asList(m.getParameterTypes()),
                    Arrays.asList(game, user, win)));
        });

    }

    @Override
    public void urgeNotify(final Game game, final User user, final boolean win) {
        final Method m = new Object() {
        }.getClass().getEnclosingMethod();
        this.executor.execute(() -> {
            this.datagramStream.send(Datagram.createCurrentMethodDatagram(Arrays.asList(m.getParameterTypes()),
                    Arrays.asList(game, user, win)));
        });

    }

    @Override
    public void callPalificoNotify(final Game game, final User user) {
        final Method m = new Object() {
        }.getClass().getEnclosingMethod();
        this.executor.execute(() -> {
            this.datagramStream.send(Datagram.createCurrentMethodDatagram(Arrays.asList(m.getParameterTypes()),
                    Arrays.asList(game, user)));
        });

    }

    @Override
    public void exitGameNotify(final Game game, final User user) {
        final Method m = new Object() {
        }.getClass().getEnclosingMethod();
        this.executor.execute(() -> {
            this.datagramStream.send(Datagram.createCurrentMethodDatagram(Arrays.asList(m.getParameterTypes()),
                    Arrays.asList(game, user)));
        });

    }

    @Override
    public void gameEndedNotify(final Game game) {
        final Method m = new Object() {
        }.getClass().getEnclosingMethod();
        this.executor.execute(() -> {
            this.datagramStream.send(
                    Datagram.createCurrentMethodDatagram(Arrays.asList(m.getParameterTypes()), Arrays.asList(game)));

        });

    }

    @Override
    public void showError(final ErrorType errorType) {
        final Method m = new Object() {
        }.getClass().getEnclosingMethod();
        this.executor.execute(() -> {
            this.datagramStream.send(Datagram.createCurrentMethodDatagram(Arrays.asList(m.getParameterTypes()),
                    Arrays.asList(errorType)));
        });

    }

    @Override
    public void close() throws IOException {
        this.executor.shutdown();
    }

}

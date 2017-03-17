package perudo.controller.net.server;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
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

/**
 * A view that sends notifications over the network.
 *
 */
public final class NetworkViewImpl implements View, Closeable {
    private final DatagramStream datagramStream;
    private final ExecutorService executor;

    /**
     * Creates a new View that sends notifications over the network.
     * 
     * @param datagramStream
     *            The stream into wich send the notifications.
     * @return The view created.
     */
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
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(m.getParameterTypes()),
                Arrays.asList(user));
        this.executor.execute(() -> {
            if (user.isOk()) {
                this.datagramStream.setUser(user.getValue());
            }
            this.datagramStream.send(dg);
        });

    }

    @Override
    public void initializeNewUserNotify(final User user) {
        final Method m = new Object() {
        }.getClass().getEnclosingMethod();
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(m.getParameterTypes()),
                Arrays.asList(user));
        this.executor.execute(() -> {
            this.datagramStream.send(dg);
        });

    }

    @Override
    public void userExitNotify(final User user) {
        final Method m = new Object() {
        }.getClass().getEnclosingMethod();
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(m.getParameterTypes()),
                Arrays.asList(user));
        this.executor.execute(() -> {
            this.datagramStream.send(dg);
        });

    }

    @Override
    public void changeNameNotify(final User oldUser, final User newUser) {
        final Method m = new Object() {
        }.getClass().getEnclosingMethod();
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(m.getParameterTypes()),
                Arrays.asList(oldUser, newUser));
        this.executor.execute(() -> {
            if (Objects.equals(this.datagramStream.getUser().orElse(null), oldUser)) {
                this.datagramStream.setUser(newUser);
            }
            this.datagramStream.send(dg);
        });

    }

    @Override
    public void getUsersRespond(final Response<Set<User>> users) {
        final Method m = new Object() {
        }.getClass().getEnclosingMethod();
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(m.getParameterTypes()),
                Arrays.asList(users));
        this.executor.execute(() -> {
            this.datagramStream.send(dg);
        });

    }

    @Override
    public void createLobbyNotify(final Lobby lobby) {
        final Method m = new Object() {
        }.getClass().getEnclosingMethod();
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(m.getParameterTypes()),
                Arrays.asList(lobby));
        this.executor.execute(() -> {
            this.datagramStream.send(dg);
        });

    }

    @Override
    public void removeLobbyNotify(final Lobby lobby) {
        final Method m = new Object() {
        }.getClass().getEnclosingMethod();
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(m.getParameterTypes()),
                Arrays.asList(lobby));
        this.executor.execute(() -> {
            this.datagramStream.send(dg);
        });

    }

    @Override
    public void getLobbiesRespond(final Response<Set<Lobby>> lobbies) {
        final Method m = new Object() {
        }.getClass().getEnclosingMethod();
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(m.getParameterTypes()),
                Arrays.asList(lobbies));
        this.executor.execute(() -> {
            this.datagramStream.send(dg);
        });

    }

    @Override
    public void joinLobbyNotify(final Lobby lobby, final User user) {
        final Method m = new Object() {
        }.getClass().getEnclosingMethod();
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(m.getParameterTypes()),
                Arrays.asList(lobby, user));
        this.executor.execute(() -> {
            this.datagramStream.send(dg);
        });

    }

    @Override
    public void exitLobbyNotify(final Lobby lobby, final User user) {
        final Method m = new Object() {
        }.getClass().getEnclosingMethod();
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(m.getParameterTypes()),
                Arrays.asList(lobby, user));
        this.executor.execute(() -> {
            this.datagramStream.send(dg);
        });

    }

    @Override
    public void startLobbyNotify(final Lobby lobby, final Game game) {
        final Method m = new Object() {
        }.getClass().getEnclosingMethod();
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(m.getParameterTypes()),
                Arrays.asList(lobby, game));
        this.executor.execute(() -> {
            this.datagramStream.send(dg);
        });

    }

    @Override
    public void removeGameNotify(final Game game) {
        final Method m = new Object() {
        }.getClass().getEnclosingMethod();
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(m.getParameterTypes()),
                Arrays.asList(game));
        this.executor.execute(() -> {
            this.datagramStream.send(dg);
        });

    }

    @Override
    public void getGamesRespond(final Response<Set<Game>> games) {
        final Method m = new Object() {
        }.getClass().getEnclosingMethod();
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(m.getParameterTypes()),
                Arrays.asList(games));
        this.executor.execute(() -> {
            this.datagramStream.send(dg);
        });

    }

    @Override
    public void playNotify(final Game game, final User user) {
        final Method m = new Object() {
        }.getClass().getEnclosingMethod();
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(m.getParameterTypes()),
                Arrays.asList(game, user));
        this.executor.execute(() -> {
            this.datagramStream.send(dg);
        });

    }

    @Override
    public void doubtNotify(final Game game, final User user, final boolean win) {
        final Method m = new Object() {
        }.getClass().getEnclosingMethod();
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(m.getParameterTypes()),
                Arrays.asList(game, user, win));
        this.executor.execute(() -> {
            this.datagramStream.send(dg);
        });

    }

    @Override
    public void urgeNotify(final Game game, final User user, final boolean win) {
        final Method m = new Object() {
        }.getClass().getEnclosingMethod();
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(m.getParameterTypes()),
                Arrays.asList(game, user, win));
        this.executor.execute(() -> {
            this.datagramStream.send(dg);
        });

    }

    @Override
    public void callPalificoNotify(final Game game, final User user) {
        final Method m = new Object() {
        }.getClass().getEnclosingMethod();
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(m.getParameterTypes()),
                Arrays.asList(game, user));
        this.executor.execute(() -> {
            this.datagramStream.send(dg);
        });

    }

    @Override
    public void exitGameNotify(final Game game, final User user) {
        final Method m = new Object() {
        }.getClass().getEnclosingMethod();
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(m.getParameterTypes()),
                Arrays.asList(game, user));
        this.executor.execute(() -> {
            this.datagramStream.send(dg);
        });

    }

    @Override
    public void gameEndedNotify(final Game game) {
        final Method m = new Object() {
        }.getClass().getEnclosingMethod();
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(m.getParameterTypes()),
                Arrays.asList(game));
        this.executor.execute(() -> {
            this.datagramStream.send(dg);

        });

    }

    @Override
    public void showError(final ErrorType errorType) {
        final Method m = new Object() {
        }.getClass().getEnclosingMethod();
        final Datagram dg = Datagram.createCurrentMethodDatagram(Arrays.asList(m.getParameterTypes()),
                Arrays.asList(errorType));
        this.executor.execute(() -> {
            this.datagramStream.send(dg);
        });

    }

    @Override
    public void close() throws IOException {
        this.executor.shutdown();
        this.datagramStream.close();
    }

}

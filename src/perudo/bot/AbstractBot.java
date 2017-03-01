package perudo.bot;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import perudo.controller.Controller;
import perudo.model.Game;
import perudo.model.Lobby;
import perudo.model.User;
import perudo.utility.ErrorType;
import perudo.utility.Response;
import perudo.view.View;

public abstract class AbstractBot implements View {
    protected final Controller controller;
    protected final User user;
    protected final ExecutorService executor;
    protected Game game;
    protected Lobby lobby;

    public AbstractBot(final Controller controller, final User user) {
        this.controller = controller;
        this.user = user;
        this.executor = Executors.newSingleThreadExecutor();
    }

    protected abstract void play(final Game game);

    @Override
    public void initializeNewUserRespond(Response<User> user) {
    }

    @Override
    public void initializeNewUserNotify(User user) {
    }

    @Override
    public void userExitNotify(User user) {
    }

    @Override
    public void changeNameNotify(User oldUser, User newUser) {
    }

    @Override
    public void getUsersRespond(Response<Set<User>> users) {
    }

    @Override
    public void createLobbyNotify(Lobby lobby) {
    }

    @Override
    public void removeLobbyNotify(Lobby lobby) {
    }

    @Override
    public void getLobbiesRespond(Response<Set<Lobby>> lobbies) {
    }

    @Override
    public void joinLobbyNotify(Lobby lobby, User user) {
        if (this.user.equals(user)) {
            this.lobby = lobby;
        }
    }

    @Override
    public void exitLobbyNotify(Lobby lobby, User user) {
        this.executor.execute(() -> {
            if (this.user.equals(lobby.getOwner())) {
                this.controller.closeNow(this.user);
                try {
                    this.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void startLobbyNotify(Lobby lobby, Game game) {
            this.executor.execute(() -> {
                if (lobby.equals(this.lobby)) {
                    this.game = game;
                    this.play(game);
                }
            });
    }

    @Override
    public void removeGameNotify(Game game) {
    }

    @Override
    public void getGamesRespond(Response<Set<Game>> games) {
    }

    @Override
    public void playNotify(Game game, User user) {
        this.executor.execute(() -> {
            this.play(game);
        });
    }

    @Override
    public void doubtNotify(Game game, User user, boolean win) {
        this.executor.execute(() -> {
            this.play(game);
        });
    }

    @Override
    public void urgeNotify(Game game, User user, boolean win) {
        this.executor.execute(() -> {
            this.play(game);
        });
    }

    @Override
    public void callPalificoNotify(Game game, User user) {
        this.executor.execute(() -> {
            this.play(game);
        });
    }

    @Override
    public void exitGameNotify(Game game, User user) {
        this.executor.execute(() -> {
            if (this.user.equals(user)) {
                this.controller.closeNow(this.user);
            }
        });

    }

    @Override
    public void gameEndedNotify(Game game) {
        this.executor.execute(() -> {
            if (game.equals(this.game)) {
                this.controller.closeNow(this.user);
            }
        });

    }

    @Override
    public void showError(ErrorType errorType) {
        System.out.println(this.getClass().getName() + " -> showError -> " + errorType);
    }

    public void close() throws IOException {
        this.executor.shutdown();
    }
}

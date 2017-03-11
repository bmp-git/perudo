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

/**
 * An abstract bot implementation. If you extend this class you should implement
 * only play() method.
 */
public abstract class AbstractBot implements View {

    private final Controller controller;
    private final User user;
    private final ExecutorService executor;
    private Game game;
    private Lobby lobby;

    /**
     * Create the AbstractBot from controller and user.
     * 
     * @param controller
     *            The controller to use
     * 
     * @param user
     *            The user to use.
     */
    public AbstractBot(final Controller controller, final User user) {
        this.controller = controller;
        this.user = user;
        this.executor = Executors.newSingleThreadExecutor();
    }

    /**
     * This is called when the bot should evaluate what to do.
     */
    protected abstract void play();

    /**
     * The controller where call the actions.
     * 
     * @return the controller instance
     */
    protected Controller getController() {
        return controller;
    }

    /**
     * The user to utilize in the calls to controller.
     * 
     * @return the user instance
     */
    protected User getUser() {
        return user;
    }

    /**
     * The actual game to evaluate.
     * 
     * @return the game instance
     */
    protected Game getGame() {
        return game;
    }

    @Override
    public void initializeNewUserRespond(final Response<User> user) {
    }

    @Override
    public void initializeNewUserNotify(final User user) {
    }

    @Override
    public void userExitNotify(final User user) {
    }

    @Override
    public void changeNameNotify(final User oldUser, final User newUser) {
    }

    @Override
    public void getUsersRespond(final Response<Set<User>> users) {
    }

    @Override
    public void createLobbyNotify(final Lobby lobby) {
    }

    @Override
    public void removeLobbyNotify(final Lobby lobby) {
    }

    @Override
    public void getLobbiesRespond(final Response<Set<Lobby>> lobbies) {
    }

    @Override
    public void joinLobbyNotify(final Lobby lobby, final User user) {
        if (this.user.equals(user)) {
            this.lobby = lobby;
        }
    }

    @Override
    public void exitLobbyNotify(final Lobby lobby, final User user) {
        this.executor.execute(() -> {
            if (this.user.equals(lobby.getOwner())) {
                this.controller.closeNow(this.user);
            }
        });
    }

    @Override
    public void startLobbyNotify(final Lobby lobby, final Game game) {
        this.executor.execute(() -> {
            if (lobby.equals(this.lobby)) {
                this.game = game;
                this.play();
            }
        });
    }

    @Override
    public void removeGameNotify(final Game game) {
    }

    @Override
    public void getGamesRespond(final Response<Set<Game>> games) {
    }

    @Override
    public void playNotify(final Game game, final User user) {
        this.executor.execute(() -> {
            if (game.equals(this.game)) {
                this.game = game;
                this.play();
            }
        });
    }

    @Override
    public void doubtNotify(final Game game, final User user, final boolean win) {
        this.executor.execute(() -> {
            if (game.equals(this.game)) {
                this.game = game;
                this.play();
            }
        });
    }

    @Override
    public void urgeNotify(final Game game, final User user, final boolean win) {
        this.executor.execute(() -> {
            if (game.equals(this.game)) {
                this.game = game;
                this.play();
            }
        });
    }

    @Override
    public void callPalificoNotify(final Game game, final User user) {
        this.executor.execute(() -> {
            if (game.equals(this.game)) {
                this.game = game;
                this.play();
            }
        });
    }

    @Override
    public void exitGameNotify(final Game game, final User user) {
        this.executor.execute(() -> {
            if (this.user.equals(user)) {
                this.controller.closeNow(this.user);
            }

            if (game.equals(this.game)) {
                if (game.getUsers().stream().allMatch(u -> u.getType().isBot())) {
                    // all bots
                    this.controller.closeNow(this.user);
                } else {
                    this.game = game;
                    this.play();
                }
            }

        });

    }

    @Override
    public void gameEndedNotify(final Game game) {
    }

    @Override
    public void showError(final ErrorType errorType) {
        System.out.println(this.getClass().getName() + " -> showError -> " + errorType);

        this.executor.execute(() -> {
            this.play();
        });
    }

    @Override
    public void close() throws IOException {
        this.executor.shutdown();
    }
}

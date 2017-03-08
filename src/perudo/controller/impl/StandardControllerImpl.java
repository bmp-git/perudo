package perudo.controller.impl;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import perudo.bot.BotFactory;
import perudo.controller.Controller;
import perudo.model.Bid;
import perudo.model.Game;
import perudo.model.GameSettings;
import perudo.model.Lobby;
import perudo.model.Model;
import perudo.model.User;
import perudo.model.UserType;
import perudo.model.impl.BidImpl;
import perudo.model.impl.LobbyImpl;
import perudo.model.impl.ModelImpl;
import perudo.model.impl.UserImpl;
import perudo.utility.ErrorType;
import perudo.utility.ErrorTypeException;
import perudo.utility.impl.LoggerSingleton;
import perudo.utility.impl.ResponseImpl;
import perudo.view.View;

/**
 * A basic controller.
 */
public class StandardControllerImpl implements Controller {
    private final Model model;
    private final Map<User, View> views;
    private final ExecutorService executor;
    private final ExecutorService turnTimer;

    /**
     * Create a new StandardControllerImpl.
     */
    public StandardControllerImpl() {
        this.model = new ModelImpl();
        this.views = new ConcurrentHashMap<>();
        this.executor = Executors.newSingleThreadExecutor();
        this.turnTimer = Executors.newCachedThreadPool();
    }

    @Override
    public void initializeNewUser(final View view) {
        Objects.requireNonNull(view);
        this.executor.execute(() -> {
            try {
                final User newUser = UserImpl.getNewAnonymousUser("Anonymous", UserType.PLAYER);
                this.model.addUser(newUser);
                this.views.put(newUser, view);
                view.initializeNewUserRespond(ResponseImpl.of(newUser));
                views.entrySet().stream().filter(e -> !Objects.equals(e, newUser))
                        .forEach(e -> e.getValue().initializeNewUserNotify(newUser));
            } catch (ErrorTypeException e) {
                view.initializeNewUserRespond(ResponseImpl.empty(e.getErrorType()));
            }
        });
    }

    @Override
    public void changeUserName(final User user, final String name) {
        Objects.requireNonNull(user);
        this.executor.execute(() -> {
            final View view = this.views.get(user);
            try {
                if (this.userIsInLobby(user)) {
                    throw new ErrorTypeException(ErrorType.USER_IS_IN_LOBBY);
                } else if (this.userIsInGame(user)) {
                    throw new ErrorTypeException(ErrorType.USER_IS_IN_GAME);
                }
                this.model.removeUser(user);
            } catch (ErrorTypeException e) {
                view.showError(e.getErrorType());
                return;
            }

            try {
                final User newUser = user.changeName(name);
                this.model.addUser(newUser);
                this.views.remove(user);
                this.views.put(newUser, view);
                this.views.forEach((u, v) -> v.changeNameNotify(user, newUser));
            } catch (ErrorTypeException e) {
                try {
                    this.model.addUser(user);
                } catch (ErrorTypeException e1) {
                    e1.printStackTrace();
                    this.views.forEach((u, v) -> v.userExitNotify(user));
                    this.views.remove(user);
                    view.showError(e1.getErrorType());
                }
                view.showError(e.getErrorType());
            }
        });
    }

    @Override
    public void getUsers(final User user) {
        Objects.requireNonNull(user);
        this.executor.execute(() -> {
            final View view = this.views.get(user);
            view.getUsersRespond(ResponseImpl.of(this.model.getUsers()));
        });
    }

    @Override
    public void createLobby(final User user, final GameSettings info) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(info);
        this.executor.execute(() -> {
            try {
                if (this.userIsInLobby(user)) {
                    throw new ErrorTypeException(ErrorType.USER_IS_IN_LOBBY);
                } else if (this.userIsInGame(user)) {
                    throw new ErrorTypeException(ErrorType.USER_IS_IN_GAME);
                }
                final Lobby lobby = new LobbyImpl(info, user);
                this.model.addLobby(lobby);
                this.views.forEach((u, v) -> v.createLobbyNotify(lobby));
                this.views.forEach((u, v) -> v.joinLobbyNotify(lobby, user));
            } catch (ErrorTypeException e) {
                final View view = this.views.get(user);
                view.showError(e.getErrorType());
            }
        });
    }

    @Override
    public void getLobbies(final User user) {
        Objects.requireNonNull(user);
        this.executor.execute(() -> {
            final View view = this.views.get(user);
            view.getLobbiesRespond(ResponseImpl.of(this.model.getLobbies()));
        });

    }

    @Override
    public void joinLobby(final User user, final Lobby lobby) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(lobby);
        this.executor.execute(() -> {
            try {
                if (this.userIsInLobby(user)) {
                    throw new ErrorTypeException(ErrorType.USER_IS_IN_LOBBY);
                } else if (this.userIsInGame(user)) {
                    throw new ErrorTypeException(ErrorType.USER_IS_IN_GAME);
                }
                final Lobby lobbyModel = this.getLobbyFromModel(lobby);
                lobbyModel.addUser(user);
                this.views.forEach((u, v) -> v.joinLobbyNotify(lobbyModel, user));
            } catch (ErrorTypeException e) {
                final View view = this.views.get(user);
                view.showError(e.getErrorType());
            }
        });

    }

    @Override
    public void addBotToLobby(final User user, final Lobby lobby, final UserType type) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(lobby);
        Objects.requireNonNull(type);
        this.executor.execute(() -> {
            final View view = this.views.get(user);
            Lobby lobbyModelTemp = null;
            User botUTemp = null;
            View botVTemp = null;
            try {
                lobbyModelTemp = this.getLobbyFromModel(lobby);
                if (!Objects.equals(lobbyModelTemp.getOwner(), user)) {
                    throw new ErrorTypeException(ErrorType.USER_DOES_NOT_OWN_LOBBY);
                }
                botUTemp = UserImpl.getNewAnonymousUser("Bot", type);
                botVTemp = BotFactory.createBot(this, botUTemp);
                this.model.addUser(botUTemp);
            } catch (ErrorTypeException e) {
                view.showError(e.getErrorType());
                return;
            }

            final Lobby lobbyModel = lobbyModelTemp;
            final User botU = botUTemp;
            final View botV = botVTemp;

            try {
                this.views.put(botU, botV);
                views.entrySet().stream().forEach(e -> e.getValue().initializeNewUserNotify(botU));
                lobbyModel.addUser(botU);
                this.views.forEach((u, v) -> v.joinLobbyNotify(lobbyModel, botU));
            } catch (ErrorTypeException e) {
                try {
                    this.model.removeUser(botU);
                    this.views.forEach((u, v) -> v.userExitNotify(botU));
                    this.views.remove(botU);
                } catch (ErrorTypeException e1) {
                    e1.printStackTrace();
                    view.showError(e1.getErrorType());
                }
                view.showError(e.getErrorType());
            }
        });

    }

    @Override
    public void exitLobby(final User user) {
        Objects.requireNonNull(user);
        this.executor.execute(() -> {
            try {
                final Lobby lobby = this.getLobbyFromModel(user);
                lobby.removeUser(user);
                this.views.forEach((u, v) -> v.exitLobbyNotify(lobby, user));
                if (lobby.getUsers().isEmpty()) {
                    this.model.removeLobby(lobby);
                    this.views.forEach((u, v) -> v.removeLobbyNotify(lobby));
                }
            } catch (ErrorTypeException e) {
                final View view = this.views.get(user);
                view.showError(e.getErrorType());
            }

        });

    }

    @Override
    public void startLobby(final User user) {
        Objects.requireNonNull(user);
        this.executor.execute(() -> {
            final View view = this.views.get(user);
            Lobby lobbyTMP = null;
            Game gameTMP = null;
            try {
                lobbyTMP = this.getLobbyFromModel(user);
                gameTMP = lobbyTMP.startGame(user);
                this.model.removeLobby(lobbyTMP);
            } catch (ErrorTypeException e) {
                view.showError(e.getErrorType());
                return;
            }

            final Lobby lobby = lobbyTMP;
            final Game game = gameTMP;
            try {
                this.views.forEach((u, v) -> v.removeLobbyNotify(lobby));
                this.model.addGame(game);
                this.startTurnTimeChecker(game);
                this.views.forEach((u, v) -> v.startLobbyNotify(lobby, game));
            } catch (ErrorTypeException e) {
                try {
                    this.model.addLobby(lobby);
                    this.views.forEach((u, v) -> v.createLobbyNotify(lobby));
                } catch (ErrorTypeException e1) {
                    e1.printStackTrace();
                    view.showError(e1.getErrorType());
                }
                view.showError(e.getErrorType());
            }
        });

    }

    @Override
    public void getGames(final User user) {
        Objects.requireNonNull(user);
        this.executor.execute(() -> {
            final View view = this.views.get(user);
            view.getGamesRespond(ResponseImpl.of(this.model.getGames()));
        });
    }

    @Override
    public void play(final User user, final Bid bid) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(bid);
        this.executor.execute(() -> {
            try {
                final Game game = this.getGameFromModel(user);
                game.play(bid, user);
                final Map<User, View> gameViews = this.getViewsfromGame(game);
                gameViews.forEach((u, v) -> v.playNotify(game, user));
            } catch (ErrorTypeException e) {
                final View view = this.views.get(user);
                view.showError(e.getErrorType());
            }
        });

    }

    @Override
    public void doubt(final User user) {
        Objects.requireNonNull(user);
        this.executor.execute(() -> {
            try {
                final Game game = this.getGameFromModel(user);
                final boolean result = game.doubt(user);
                final Map<User, View> gameViews = this.getViewsfromGame(game);
                gameViews.forEach((u, v) -> v.doubtNotify(game, user, result));
                if (game.isOver()) {
                    gameViews.forEach((u, v) -> v.gameEndedNotify(game));
                }
            } catch (ErrorTypeException e) {
                final View view = this.views.get(user);
                view.showError(e.getErrorType());
            }

        });

    }

    @Override
    public void urge(final User user) {
        Objects.requireNonNull(user);
        this.executor.execute(() -> {
            try {
                final Game game = this.getGameFromModel(user);
                final boolean result = game.urge(user);
                final Map<User, View> gameViews = this.getViewsfromGame(game);
                gameViews.forEach((u, v) -> v.urgeNotify(game, user, result));
                if (game.isOver()) {
                    gameViews.forEach((u, v) -> v.gameEndedNotify(game));
                }
            } catch (ErrorTypeException e) {
                final View view = this.views.get(user);
                view.showError(e.getErrorType());
            }
        });

    }

    @Override
    public void callPalifico(final User user) {
        Objects.requireNonNull(user);
        this.executor.execute(() -> {
            try {
                final Game game = this.getGameFromModel(user);
                game.callPalifico(user);
                final Map<User, View> gameViews = this.getViewsfromGame(game);
                gameViews.forEach((u, v) -> v.callPalificoNotify(game, user));
            } catch (ErrorTypeException e) {
                final View view = this.views.get(user);
                view.showError(e.getErrorType());
            }
        });
    }

    @Override
    public void exitGame(final User user) {
        Objects.requireNonNull(user);
        this.executor.execute(() -> {
            try {
                final Game game = this.getGameFromModel(user);
                final boolean gameAlreadyOver = game.isOver();
                game.removeUser(user);
                final Map<User, View> gameViews = this.getViewsfromGame(game);
                gameViews.forEach((u, v) -> v.exitGameNotify(game, user));
                this.views.get(user).exitGameNotify(game, user);
                if (game.getUsers().size() == 1 && !gameAlreadyOver) {
                    gameViews.forEach((u, v) -> v.gameEndedNotify(game));
                } else if (game.getUsers().isEmpty()) {
                    this.model.removeGame(game);
                    this.views.forEach((u, v) -> v.removeGameNotify(game));
                }
            } catch (ErrorTypeException e) {
                final View view = this.views.get(user);
                view.showError(e.getErrorType());
            }
        });
    }

    @Override
    public void close(final User user) {
        Objects.requireNonNull(user);
        this.executor.execute(() -> {
            final View view = this.views.get(user);
            try {
                if (!this.model.getUsers().contains(user)) {
                    throw new ErrorTypeException(ErrorType.USER_DOES_NOT_EXISTS);
                } else if (this.userIsInLobby(user)) {
                    throw new ErrorTypeException(ErrorType.USER_IS_IN_LOBBY);
                } else if (this.userIsInGame(user)) {
                    throw new ErrorTypeException(ErrorType.USER_IS_IN_GAME);
                } else {
                    this.model.removeUser(user);
                    this.views.remove(user);
                    this.views.forEach((u, v) -> v.userExitNotify(user));
                    view.userExitNotify(user);
                    view.close();
                    LoggerSingleton.get().add(this.getClass(),
                            "StandardController: view of user" + user.getName() + " closed and removed.");
                }
            } catch (ErrorTypeException e) {
                view.showError(e.getErrorType());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void closeNow(final User user) {
        Objects.requireNonNull(user);
        this.executor.execute(() -> {
            try {
                if (this.views.containsKey(user)) {
                    this.views.get(user).close();
                    this.views.remove(user);
                    LoggerSingleton.get().add(this.getClass(),
                            "StandardController: view of user" + user.getName() + " closed and removed.");
                }
                if (this.userIsInLobby(user)) {
                    final Lobby lobby = this.getLobbyFromModel(user);
                    lobby.removeUser(user);
                    this.views.forEach((u, v) -> v.exitLobbyNotify(lobby, user));
                    if (lobby.getUsers().isEmpty()) {
                        this.model.removeLobby(lobby);
                        this.views.forEach((u, v) -> v.removeLobbyNotify(lobby));
                    }
                }
                if (this.userIsInGame(user)) {
                    final Game game = this.getGameFromModel(user);
                    final boolean gameAlreadyOver = game.isOver();
                    game.removeUser(user);
                    final Map<User, View> gameViews = this.getViewsfromGame(game);
                    gameViews.forEach((u, v) -> v.exitGameNotify(game, user));
                    if (game.getUsers().size() == 1 && !gameAlreadyOver) {
                        gameViews.forEach((u, v) -> v.gameEndedNotify(game));
                    } else if (game.getUsers().isEmpty()) {
                        this.model.removeGame(game);
                        this.views.forEach((u, v) -> v.removeGameNotify(game));
                    }
                }
                if (this.model.getUsers().contains(user)) {
                    this.model.removeUser(user);
                    this.views.forEach((u, v) -> v.userExitNotify(user));
                }

            } catch (ErrorTypeException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void close() throws IOException {
        this.executor.shutdownNow();
        this.turnTimer.shutdownNow();
    }

    private void startTurnTimeChecker(final Game game) {
        Objects.requireNonNull(game);
        turnTimer.execute(() -> {
            while (!game.isOver()) {
                if (game.getTurnRemainingTime().isNegative()) {

                    try {
                        final Bid bid = game.getCurrentBid().isPresent() ? game.getCurrentBid().get().nextBid()
                                : new BidImpl(1, 1);
                        final User user = game.getTurn();
                        game.play(bid, user);
                        final Map<User, View> gameViews = this.getViewsfromGame(game);
                        gameViews.forEach((u, v) -> v.playNotify(game, user));
                    } catch (ErrorTypeException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void checkSize(final Collection<?> collection, final ErrorType error) throws ErrorTypeException {
        if (collection.isEmpty()) {
            throw new ErrorTypeException(error);
        } else if (collection.size() > 1) {
            throw new IllegalStateException();
        }
    }

    private Lobby getLobbyFromModel(final Lobby lobby) throws ErrorTypeException {
        final List<Lobby> result = this.model.getLobbies().stream().filter(lobby::equals).collect(Collectors.toList());
        this.checkSize(result, ErrorType.LOBBY_NOT_EXISTS);
        return result.get(0);
    }

    private Lobby getLobbyFromModel(final User user) throws ErrorTypeException {
        if (!this.model.getUsers().contains(user)) {
            throw new ErrorTypeException(ErrorType.USER_DOES_NOT_EXISTS);
        }
        final List<Lobby> result = this.model.getLobbies().stream().filter(l -> l.getUsers().contains(user))
                .collect(Collectors.toList());
        this.checkSize(result, ErrorType.USER_IS_NOT_IN_A_LOBBY);
        return result.get(0);
    }

    private Game getGameFromModel(final User user) throws ErrorTypeException {
        if (!this.model.getUsers().contains(user)) {
            throw new ErrorTypeException(ErrorType.USER_DOES_NOT_EXISTS);
        }
        final List<Game> result = this.model.getGames().stream().filter(g -> g.getUsers().contains(user))
                .collect(Collectors.toList());
        this.checkSize(result, ErrorType.USER_IS_NOT_IN_A_GAME);
        return result.get(0);
    }

    private boolean userIsInGame(final User user) {
        return this.model.getGames().stream().flatMap(g -> g.getUsers().stream()).anyMatch(user::equals);
    }

    private boolean userIsInLobby(final User user) {
        return this.model.getLobbies().stream().flatMap(l -> l.getUsers().stream()).anyMatch(user::equals);
    }

    private Map<User, View> getViewsfromGame(final Game game) {
        return this.views.entrySet().stream().filter(e -> game.getUsers().contains(e.getKey()))
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
    }
}

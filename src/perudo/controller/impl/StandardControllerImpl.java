package perudo.controller.impl;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import perudo.controller.Controller;
import perudo.model.Bid;
import perudo.model.Game;
import perudo.model.GameSettings;
import perudo.model.Lobby;
import perudo.model.Model;
import perudo.model.User;
import perudo.model.impl.LobbyImpl;
import perudo.model.impl.ModelImpl;
import perudo.model.impl.UserImpl;
import perudo.utility.ErrorType;
import perudo.utility.ErrorTypeException;
import perudo.utility.impl.ResponseImpl;
import perudo.view.View;

public class StandardControllerImpl implements Controller, Closeable{
    private final Model model;
    private final Map<User, View> views;
    private final ExecutorService executor;

    public StandardControllerImpl() {
        this.model = new ModelImpl();
        this.views = new HashMap<>();
        this.executor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void initializeNewUser(final View view) {
        Objects.requireNonNull(view);
        this.executor.execute(() -> {
            final User newUser = new UserImpl("Anonymous");
            try {
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

            final User newUser = user.changeName(name);

            try {
                this.model.addUser(newUser);
            } catch (ErrorTypeException e) {
                try {
                    this.model.addUser(user);
                } catch (ErrorTypeException e1) {
                    e1.printStackTrace();
                    this.views.remove(user);
                }
                view.showError(e.getErrorType());
                return;
            }

            this.views.remove(user);
            this.views.put(newUser, view);
            this.views.forEach((u, v) -> v.changeNameNotify(newUser));
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
                this.model.addGame(game);
                this.views.forEach((u, v) -> v.startLobbyNotify(lobby, game));
            } catch (ErrorTypeException e) {
                try {
                    this.model.addLobby(lobby);
                } catch (ErrorTypeException e1) {
                    e1.printStackTrace();
                    this.views.forEach((u, v) -> v.removeLobbyNotify(lobby));
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
                game.removeUser(user);
                this.getViewsfromGame(game).forEach((u, v) -> v.exitGameNotify(game, user));
                this.views.get(user).exitGameNotify(game, user);
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
            try {
                if (!this.model.getUsers().contains(user)) {
                    throw new ErrorTypeException(ErrorType.USER_DOES_NOT_EXISTS);
                } else if (this.userIsInLobby(user)) {
                    throw new ErrorTypeException(ErrorType.USER_IS_IN_LOBBY);
                } else if (this.userIsInGame(user)) {
                    throw new ErrorTypeException(ErrorType.USER_IS_IN_GAME);
                } else {
                    this.model.removeUser(user);
                    this.views.forEach((u, v) -> v.userExitNotify(user));
                    this.views.remove(user);
                }
            } catch (ErrorTypeException e) {
                final View view = this.views.get(user);
                view.showError(e.getErrorType());
            }
        });

    }
    
    @Override
    public void close() throws IOException {
        this.executor.shutdown();
    }

    private void checkSize(final Collection<?> collection, final ErrorType error) throws ErrorTypeException {
        if (collection.size() == 0) {
            throw new ErrorTypeException(error);
        } else if (collection.size() > 1) {
            throw new IllegalStateException();
        }
    }

    private Lobby getLobbyFromModel(final Lobby lobby) throws ErrorTypeException {
        List<Lobby> result = this.model.getLobbies().stream().filter(lobby::equals).collect(Collectors.toList());
        checkSize(result, ErrorType.LOBBY_NOT_EXISTS);
        return result.get(0);
    }

    private Lobby getLobbyFromModel(final User user) throws ErrorTypeException {
        if (!this.model.getUsers().contains(user)) {
            throw new ErrorTypeException(ErrorType.USER_DOES_NOT_EXISTS);
        }
        List<Lobby> result = this.model.getLobbies().stream().filter(l -> l.getUsers().contains(user))
                .collect(Collectors.toList());
        checkSize(result, ErrorType.USER_IS_NOT_IN_A_LOBBY);
        return result.get(0);
    }

    private Game getGameFromModel(final User user) throws ErrorTypeException {
        if (!this.model.getUsers().contains(user)) {
            throw new ErrorTypeException(ErrorType.USER_DOES_NOT_EXISTS);
        }
        List<Game> result = this.model.getGames().stream().filter(g -> g.getUsers().contains(user))
                .collect(Collectors.toList());
        checkSize(result, ErrorType.USER_IS_NOT_IN_A_GAME);
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

package perudo.controller.impl;

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
import perudo.model.impl.GameImpl;
import perudo.model.impl.LobbyImpl;
import perudo.model.impl.ModelImpl;
import perudo.model.impl.UserImpl;
import perudo.utility.ErrorType;
import perudo.utility.ErrorTypeException;
import perudo.utility.impl.ResponseImpl;
import perudo.utility.impl.ResultImpl;
import perudo.view.View;

public class StandardControllerImpl implements Controller {
    private final Model model;
    private final Map<User, View> views;
    private final ExecutorService executor;

    public StandardControllerImpl() {
        this.model = new ModelImpl();
        this.views = new HashMap<>();
        this.executor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void initialize() {
        // TODO Auto-generated method stub
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
            } catch (ErrorTypeException e) {
                view.initializeNewUserRespond(ResponseImpl.empty(e.getErrorType()));
            }
        });
    }

    @Override
    public void changeUserName(final User user, final String name) {
        Objects.requireNonNull(user);
        this.executor.execute(() -> {
            final User newUser = new UserImpl(name);
            final View view = this.views.get(user);
            try {
                this.model.addUser(newUser);
            } catch (ErrorTypeException e) {
                view.changeNameRespond(ResponseImpl.empty(e.getErrorType()));
                return;
            }

            try {
                this.model.removeUser(user);
            } catch (ErrorTypeException e) {
                try {
                    this.model.removeUser(newUser);
                } catch (ErrorTypeException e1) {
                    e1.printStackTrace();
                    System.exit(1);
                }
                view.changeNameRespond(ResponseImpl.empty(e.getErrorType()));
                return;
            }

            this.views.remove(user);
            this.views.put(newUser, view);
            view.changeNameRespond(ResponseImpl.of(newUser));
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
            final View view = this.views.get(user);
            try {
                final Lobby lobby = new LobbyImpl(info, user);
                this.model.addLobby(lobby);
                view.createLobbyRespond(ResponseImpl.of(lobby));
            } catch (ErrorTypeException e) {
                view.createLobbyRespond(ResponseImpl.empty(e.getErrorType()));
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
            final View view = this.views.get(user);
            try {
                final Lobby lobbyModel = getLobbyFromModel(lobby);
                lobbyModel.addUser(user);
                view.joinLobbyRespond(ResponseImpl.of(lobbyModel));
            } catch (ErrorTypeException e) {
                view.joinLobbyRespond(ResponseImpl.empty(e.getErrorType()));
            }
        });

    }

    @Override
    public void exitLobby(User user) {
        Objects.requireNonNull(user);
        this.executor.execute(() -> {
            final View view = this.views.get(user);
            try {
                final Lobby lobby = getLobbyFromModel(user);
                lobby.removeUser(user);
                view.exitLobbyRespond(ResponseImpl.of(lobby));
            } catch (ErrorTypeException e) {
                view.exitLobbyRespond(ResponseImpl.empty(e.getErrorType()));
            }
        });

    }

    @Override
    public void startLobby(User user) {
        Objects.requireNonNull(user);
        this.executor.execute(() -> {
            final View view = this.views.get(user);
            Lobby lobby = null;
            Game game = null;
            try {
                lobby = getLobbyFromModel(user);
                if (!Objects.equals(lobby.getOwner(), user)) {
                    throw new ErrorTypeException(ErrorType.USER_DOES_NOT_OWN_LOBBY);
                }
                game = new GameImpl(lobby.getInfo(), lobby.getUsers());
                this.model.removeLobby(lobby);

            } catch (ErrorTypeException e) {
                view.startLobbyRespond(ResponseImpl.empty(e.getErrorType()));
                return;
            }

            try {
                this.model.addGame(game);
            } catch (ErrorTypeException e) {
                try {
                    this.model.addLobby(lobby);
                } catch (ErrorTypeException e1) {
                    e1.printStackTrace();
                    System.exit(2);
                }
                view.startLobbyRespond(ResponseImpl.empty(e.getErrorType()));
            }

        });

    }

    @Override
    public void getGames(User user) {
        Objects.requireNonNull(user);
        this.executor.execute(() -> {
            final View view = this.views.get(user);
            view.getGamesRespond(ResponseImpl.of(this.model.getGames()));
        });
    }

    @Override
    public void play(User user, Bid bid) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(bid);
        this.executor.execute(() -> {
            final View view = this.views.get(user);
            try {
                final Game game = getGameFromModel(user);
                game.play(bid, user);
                view.playRespond(ResultImpl.ok());
            } catch (ErrorTypeException e) {
                view.playRespond(ResultImpl.error(e.getErrorType()));
            }
        });

    }

    @Override
    public void doubt(User user) {
        // TODO Auto-generated method stub

    }

    @Override
    public void urge(User user) {
        // TODO Auto-generated method stub

    }

    @Override
    public void callPalifico(User user) {
        // TODO Auto-generated method stub

    }

    @Override
    public void exitGame(User user) {
        // TODO Auto-generated method stub

    }

    @Override
    public void close(User user) {
        // TODO Auto-generated method stub

    }

    private void checkSize(Collection<?> collection, ErrorType error) throws ErrorTypeException {
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
        List<Game> result = this.model.getGames().stream().filter(g -> g.getUsers().contains(user)).collect(Collectors.toList());
        checkSize(result, ErrorType.USER_IS_NOT_IN_A_GAME);
        return result.get(0);
    }

}

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
            final View view = this.views.get(user);
            User newUser = user.changeName(name);
            try {
                this.model.removeUser(user);
            } catch (ErrorTypeException e) {
                view.changeNameRespond(ResponseImpl.empty(e.getErrorType()));
                return;
            }

            try {
                this.model.addUser(newUser);
            } catch (ErrorTypeException e) {
                try {
                    this.model.addUser(user);
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
    public void exitLobby(final User user) {
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
    public void startLobby(final User user) {
        Objects.requireNonNull(user);
        this.executor.execute(() -> {
            final View view = this.views.get(user);
            Lobby lobby = null;
            Game game = null;
            try {
                lobby = getLobbyFromModel(user);
                game = lobby.startGame(user);
                this.model.removeLobby(lobby);

            } catch (ErrorTypeException e) {
                view.startLobbyRespond(ResponseImpl.empty(e.getErrorType()));
                return;
            }

            try {
                this.model.addGame(game);
                view.startLobbyRespond(ResultImpl.ok());
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
            final View view = this.views.get(user);
            try {
                final Game game = getGameFromModel(user);
                game.play(bid, user);
                view.playRespond(ResultImpl.ok());
                if (game.isOver()) {
                    this.views.entrySet().stream().filter(e -> game.getUsers().contains(e.getKey()))
                            .forEach(e -> e.getValue().gameEnded(game));
                }
            } catch (ErrorTypeException e) {
                view.playRespond(ResultImpl.error(e.getErrorType()));
            }
        });

    }

    @Override
    public void doubt(final User user) {
        Objects.requireNonNull(user);
        this.executor.execute(() -> {
            final View view = this.views.get(user);
            try {
                final Game game = getGameFromModel(user);
                final boolean result = game.doubt(user);
                // need to tell the view if the doubt was correct or not
                // ????

                view.doubtRespond(ResultImpl.ok());
            } catch (ErrorTypeException e) {
                view.doubtRespond(ResultImpl.error(e.getErrorType()));
            }

        });

    }

    @Override
    public void urge(final User user) {
        Objects.requireNonNull(user);
        this.executor.execute(() -> {
            final View view = this.views.get(user);
            try {
                final Game game = getGameFromModel(user);
                final boolean result = game.urge(user);
                // need to tell the view if the urge was correct or not
                // ????

                view.urgeRespond(ResultImpl.ok());
            } catch (ErrorTypeException e) {
                view.urgeRespond(ResultImpl.error(e.getErrorType()));
            }
        });

    }

    @Override
    public void callPalifico(final User user) {
        Objects.requireNonNull(user);
        this.executor.execute(() -> {
            final View view = this.views.get(user);
            try {
                final Game game = getGameFromModel(user);
                game.callPalifico(user);
                view.callPalificoRespond(ResultImpl.ok());
            } catch (ErrorTypeException e) {
                view.callPalificoRespond(ResultImpl.error(e.getErrorType()));
            }
        });
    }

    @Override
    public void exitGame(final User user) {
        Objects.requireNonNull(user);
        this.executor.execute(() -> {
            final View view = this.views.get(user);
            try {
                final Game game = getGameFromModel(user);
                //game.removeUser(user);
                view.exitGameRespond(ResultImpl.ok());
            } catch (ErrorTypeException e) {
                view.exitGameRespond(ResultImpl.error(e.getErrorType()));
            }
        });
    }

    @Override
    public void close(final User user) {
        Objects.requireNonNull(user);
        this.executor.execute(() -> {
            try {
                if(!this.model.getUsers().contains(user)) {
                    throw new ErrorTypeException(ErrorType.USER_DOES_NOT_EXISTS);
                } else if(this.userIsInLobby(user)) {
                    throw new ErrorTypeException(ErrorType.USER_IS_IN_LOBBY);
                } else if (this.userIsInGame(user)) {
                    throw new ErrorTypeException(ErrorType.USER_IS_IN_GAME);
                } else {
                    this.model.removeUser(user);
                    this.views.remove(user);
                }
            } catch (ErrorTypeException e) {
                e.printStackTrace();
            }        
        });

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

}

package perudo.model.impl;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import perudo.model.Bid;
import perudo.model.Game;
import perudo.model.GameRules;
import perudo.model.GameSettings;
import perudo.model.PlayerStatus;
import perudo.model.User;
import perudo.utility.DiffTime;
import perudo.utility.ErrorType;
import perudo.utility.ErrorTypeException;
import perudo.utility.IdDispenser;

/**
 * An implementation of Game interface.
 */
public class GameImpl implements Game {

    private static final long serialVersionUID = 462313460628002640L;

    private final int id;
    private final List<User> userList;
    private final Map<User, PlayerStatus> usersStatus;
    private final GameSettings settings;
    private final GameRules rules;

    private User currentTurn;
    private Instant turnStartTime;
    private Bid currentBid;
    private User bidUser;
    private boolean palifico;
    private Game lastRound;

    /**
     * Create a standard GameImpl.
     * 
     * @param settings
     *            the settings for the game
     * 
     * @param rules
     *            the rules for the game
     * 
     * @param users
     *            the users who plays
     */
    public GameImpl(final GameSettings settings, final GameRules rules, final Set<User> users) {
        this.rules = rules;
        if (settings == null || users == null || users.size() > settings.getMaxPlayer()) {
            throw new IllegalArgumentException();
        }

        this.id = IdDispenser.getGameIdDispenser().getNextId();
        this.settings = settings;
        this.userList = new ArrayList<>(users);
        this.usersStatus = new HashMap<>();
        this.userList.forEach(u -> this.usersStatus.put(u, PlayerStatusImpl.fromGameSettings(this.settings)));

        // random start player
        this.resetGame(this.getUsers().get(new Random().nextInt(this.getUsers().size())));
    }

    private GameImpl(final GameImpl game) {
        this.id = game.id;
        this.userList = new ArrayList<>(game.userList);
        this.usersStatus = new HashMap<>(game.usersStatus);
        this.settings = game.settings;
        this.rules = game.rules;
        this.currentTurn = game.currentTurn;
        this.turnStartTime = game.turnStartTime;
        this.currentBid = game.currentBid;
        this.bidUser = game.bidUser;
        this.palifico = game.palifico;
        // change to game.lastRound if you want to permit
        // game.getLastRound().getLastRound().getLastRound()...
        // with null you can do max game.getLastRound()
        this.lastRound = null;
    }

    private void resetGame(final User playerTurn) {
        if (this.isOver()) {
            return;
        }

        this.palifico = false;
        this.currentBid = null;
        this.bidUser = null;

        // reroll all dice
        this.userList.forEach(u -> this.usersStatus.put(u, this.usersStatus.get(u).rollDice()));

        this.goNextTurn(playerTurn);

        if ((!this.userList.contains(playerTurn)) || this.hasLost(playerTurn)) {
            this.goNextTurn(this.rules.nextTurnBid(playerTurn, this));
        }
    }

    private void goNextTurn(final User user) {
        this.currentTurn = user;
        this.turnStartTime = Instant.now();
    }

    private void changeDiceQuantity(final Map<User, Integer> diceVariation) {
        diceVariation.entrySet().stream().filter(p -> this.getUsers().contains(p.getKey()))
                .forEach(p -> this.addDice(p.getKey(), p.getValue()));
    }

    private void addDice(final User user, final int quantity) {
        if (this.usersStatus.get(user).getRemainingDice() < 0) {
            return;
        }
        this.usersStatus.put(user,
                this.usersStatus.get(user).setRemainingDice(this.usersStatus.get(user).getRemainingDice() + quantity));
    }

    private void checkUserExistence(final User user) throws ErrorTypeException {
        if (!this.userList.contains(user)) {
            throw new ErrorTypeException(ErrorType.USER_DOES_NOT_EXISTS);
        }
    }

    private void checkIsOver() throws ErrorTypeException {
        if (this.isOver()) {
            throw new ErrorTypeException(ErrorType.GAME_IS_OVER);
        }
    }

    @Override
    public synchronized boolean isOver() {
        return getUsers().stream().filter(u -> getUserStatus(u).getRemainingDice() > 0).count() <= 1;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public List<User> getUsers() {
        return Collections.unmodifiableList(this.userList);
    }

    @Override
    public synchronized User getTurn() {
        return this.currentTurn;
    }

    @Override
    public synchronized Optional<User> getBidUser() {
        return Optional.ofNullable(this.bidUser);
    }

    @Override
    public synchronized void play(final Bid bid, final User user) throws ErrorTypeException {
        this.checkIsOver();
        this.checkUserExistence(user);

        this.rules.checkCanBid(user, this);

        if (!this.rules.bidValid(bid, this)) {
            throw new ErrorTypeException(ErrorType.GAME_INVALID_BID);
        }

        this.currentBid = bid;
        this.bidUser = user;

        this.goNextTurn(this.rules.nextTurnBid(user, this));
    }

    @Override
    public synchronized Boolean doubt(final User user) throws ErrorTypeException {
        this.checkIsOver();
        this.checkUserExistence(user);

        this.rules.checkCanDoubt(user, this);

        this.lastRound = new GameImpl(this);
        final boolean win = this.rules.doubtWin(this);
        this.changeDiceQuantity(this.rules.changesDiceDoubt(user, this, win));
        this.resetGame(this.rules.nextTurnDoubt(user, this, win));
        return win;

    }

    @Override
    public synchronized Boolean urge(final User user) throws ErrorTypeException {
        this.checkIsOver();
        this.checkUserExistence(user);

        this.rules.checkCanUrge(user, this);

        this.lastRound = new GameImpl(this);
        final boolean win = this.rules.urgeWin(this);
        this.changeDiceQuantity(this.rules.changesDiceUrge(user, this, win));
        this.resetGame(this.rules.nextTurnUrge(user, this, win));
        return win;
    }

    @Override
    public synchronized void callPalifico(final User user) throws ErrorTypeException {
        this.checkIsOver();
        this.checkUserExistence(user);

        this.rules.checkCanPalifico(user, this);

        this.usersStatus.put(user, this.getUserStatus(user).callPalifico());
        this.goNextTurn(this.rules.nextTurnPalifico(user, this));
        this.palifico = true;
    }

    @Override
    public GameSettings getSettings() {
        return this.settings;
    }

    @Override
    public GameRules getRules() {
        return this.rules;
    }

    @Override
    public synchronized Optional<Bid> getCurrentBid() {
        return Optional.ofNullable(this.currentBid);
    }

    @Override
    public synchronized Duration getTurnRemainingTime() {
        return this.getSettings().getMaxTurnTime()
                .minus(Duration.between(this.turnStartTime, Instant.now().plus(DiffTime.getServerDiffTime())));
    }

    @Override
    public synchronized PlayerStatus getUserStatus(final User user) {
        return this.usersStatus.get(user);
    }

    @Override
    public synchronized boolean turnIsPalifico() {
        return this.palifico;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((settings == null) ? 0 : settings.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GameImpl other = (GameImpl) obj;
        if (id != other.id) {
            return false;
        }
        if (settings == null) {
            if (other.settings != null) {
                return false;
            }
        } else if (!settings.equals(other.settings)) {
            return false;
        }
        return true;
    }

    @Override
    public synchronized void removeUser(final User user) throws ErrorTypeException {
        this.checkUserExistence(user);
        final PlayerStatus status = this.getUserStatus(user);
        this.userList.remove(user);
        this.usersStatus.remove(user);

        if (status.getRemainingDice() != 0) {
            this.resetGame(this.getTurn());
        }
    }

    @Override
    public synchronized boolean hasLost(final User user) {
        return !this.getUsers().contains(user) || this.getUserStatus(user).getRemainingDice() == 0;
    }

    @Override
    public int getTotalRemainingDice() {
        return this.getUsers().stream().mapToInt(u -> this.getUserStatus(u).getRemainingDice()).sum();
    }

    @Override
    public Optional<Game> getLastRound() {
        return Optional.ofNullable(lastRound);
    }
}

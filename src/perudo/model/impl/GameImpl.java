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
import perudo.model.GameSettings;
import perudo.model.PlayerStatus;
import perudo.model.User;
import perudo.utility.DiffTime;
import perudo.utility.ErrorType;
import perudo.utility.ErrorTypeException;
import perudo.utility.IdDispenser;

/**
 * An implementation of Game interface following the rule of the classic game.
 */
public class GameImpl implements Game {

    private static final long serialVersionUID = 462313460628002640L;

    private final int id;
    private final List<User> userList;
    private final Map<User, PlayerStatus> usersStatus;
    private final GameSettings settings;

    private User currentTurn;
    private Instant turnStartTime;
    private Bid currentBid;
    private User bidUser;
    private boolean palifico;

    /**
     * Create a standard GameImpl.
     * 
     * @param settings
     *            the settings for the game
     * 
     * @param users
     *            the users who plays
     */
    public GameImpl(final GameSettings settings, final Set<User> users) {
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

    private void resetGame(final User playerTurn) {
        if (this.isOver()) {
            return;
        }

        this.resetTurnStartTime();
        this.palifico = false;
        this.currentBid = null;
        this.bidUser = null;

        // reroll all dice
        this.userList.forEach(u -> this.usersStatus.put(u, this.usersStatus.get(u).rollDice()));

        this.currentTurn = playerTurn;
        if ((!this.userList.contains(playerTurn)) || this.getUserStatus(playerTurn).getRemainingDice() == 0) {
            this.goNextTurn();
        }

    }

    private void goNextTurn() {
        if (this.isOver()) {
            return;
        }

        // go next, if the next player have loss, go next, ...
        do {
            this.currentTurn = this.userList.get((this.userList.indexOf(this.getTurn()) + 1) % this.userList.size());
        } while (this.getUserStatus(this.getTurn()).getRemainingDice() <= 0);

        // reset turn data
        this.resetTurnStartTime();
    }

    private void resetTurnStartTime() {
        this.turnStartTime = Instant.now();
    }

    private void removeDice(final User user, final int quantity) {
        this.addDice(user, -quantity);
    }

    private void addDice(final User user, final int quantity) {
        if (this.usersStatus.get(user).getRemainingDice() + quantity > this.getSettings().getInitialDiceNumber()) {
            return;
        }
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

    private void checkUserNotLose(final User user) throws ErrorTypeException {
        if (this.getUserStatus(user).getRemainingDice() <= 0) {
            throw new ErrorTypeException(ErrorType.GAME_YOU_ALREADY_LOSE);
        }
    }

    private void checkUserTurn(final User user) throws ErrorTypeException {
        if (!getTurn().equals(user)) {
            throw new ErrorTypeException(ErrorType.GAME_NOT_YOUR_TURN);
        }
    }

    private void checkIsOver() throws ErrorTypeException {
        if (this.isOver()) {
            throw new ErrorTypeException(ErrorType.GAME_IS_OVER);
        }
    }

    private void checkUserCanUrge(final User user) throws ErrorTypeException {
        // no one had bid yet, the one who bid can't urge, the one who is turn
        // can't urge
        if ((!this.getCurrentBid().isPresent()) || this.getBidUser().get().equals(user)
                || this.getTurn().equals(user)) {
            throw new ErrorTypeException(ErrorType.GAME_CANT_CALL_URGE_NOW);
        }
    }

    @Override
    public synchronized Optional<Integer> getRealBidDiceCount() {
        if (!this.getCurrentBid().isPresent()) {
            return Optional.empty();
        }
        final int bidUserJollyCount = this.userList.stream().filter(u -> this.getBidUser().get().equals(u))
                .mapToInt(u -> this.usersStatus.get(u).getDiceCount().get(1)).sum();
        final int usersDiceCount = this.userList.stream()
                .mapToInt(u -> this.usersStatus.get(u).getDiceCount().get(this.getCurrentBid().get().getDiceValue()))
                .sum();

        return Optional.of((this.getCurrentBid().get().getDiceValue() == 1 ? usersDiceCount
                : (usersDiceCount + bidUserJollyCount)));
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
        this.checkUserNotLose(user);
        this.checkUserTurn(user);

        // check if bid is valid
        if (this.getCurrentBid().isPresent()
                && !this.getCurrentBid().get().isNextBidValid(bid, this.turnIsPalifico(), this.getSettings())) {
            throw new ErrorTypeException(ErrorType.GAME_INVALID_BID);
        }

        this.currentBid = bid;
        this.bidUser = user;
        this.goNextTurn();
    }

    @Override
    public synchronized Boolean doubt(final User user) throws ErrorTypeException {
        this.checkIsOver();
        this.checkUserExistence(user);
        this.checkUserNotLose(user);
        this.checkUserTurn(user);
        if (!this.getCurrentBid().isPresent()) {
            throw new ErrorTypeException(ErrorType.GAME_CANT_DOUBT_NOW);
        }

        if (this.getRealBidDiceCount().get() < this.getCurrentBid().get().getQuantity()) {
            // doubt is correct, bid user loss 1 dice
            this.removeDice(this.getBidUser().get(), 1);
            this.resetGame(this.getBidUser().get());
            return true;
        } else {
            // doubt is wrong, user loss 1 dice
            this.removeDice(user, 1);
            this.resetGame(user);
            return false;
        }
    }

    @Override
    public synchronized Boolean urge(final User user) throws ErrorTypeException {
        this.checkIsOver();
        this.checkUserExistence(user);
        this.checkUserNotLose(user);
        this.checkUserCanUrge(user);

        if (this.getRealBidDiceCount().get() == this.getCurrentBid().get().getQuantity()) {
            // urge is correct, all users (user excluded) loss 1 dice
            this.userList.stream().filter(u -> this.usersStatus.get(u).getRemainingDice() > 0)
                    .filter(u -> !u.equals(user)).forEach(u -> this.removeDice(u, 1));
            // user get 1 dice
            this.addDice(user, 1);
            this.resetGame(this.getTurn());
            return true;
        } else {
            // urge is wrong, user loss 1 dice
            this.removeDice(user, 1);
            this.resetGame(user);
            return false;
        }

    }

    @Override
    public synchronized void callPalifico(final User user) throws ErrorTypeException {
        this.checkIsOver();
        this.checkUserExistence(user);

        // bid already started
        if (this.getCurrentBid().isPresent() || this.turnIsPalifico()) {
            throw new ErrorTypeException(ErrorType.GAME_CANT_CALL_PALIFICO_NOW);
        }

        this.usersStatus.put(user, this.getUserStatus(user).callPalifico());
        this.resetTurnStartTime();
        this.currentTurn = user;
        this.palifico = true;
    }

    @Override
    public GameSettings getSettings() {
        return this.settings;
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
}

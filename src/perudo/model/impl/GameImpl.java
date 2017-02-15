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
import perudo.utility.ErrorType;
import perudo.utility.ErrorTypeException;
import perudo.utility.IdDispenser;

public class GameImpl implements Game {

    private final int id;
    private final List<User> userList;
    private final Map<User, PlayerStatus> usersStatus;
    private final GameSettings settings;

    private User currentTurn;
    private Instant turnStartTime;
    private Optional<Bid> currentBid;
    private boolean palifico;

    public GameImpl(final GameSettings settings, final Set<User> users) {
        if (settings == null || users == null || users.size() > settings.getMaxPlayer()) {
            throw new IllegalArgumentException();
        }

        this.id = IdDispenser.getGameIdDispenser().getNextId();
        this.settings = settings;
        this.userList = new ArrayList<>(users);
        this.usersStatus = new HashMap<>();
        this.userList.forEach(u -> this.usersStatus.put(u, PlayerStatusImpl.fromGameSettings(this.getSettings())));

        this.currentTurn = this.getUsers().get(new Random().nextInt(this.getUsers().size()));
        this.resetGame();
    }

    private void resetGame() {
        if (this.isOver()) {
            return;
        }

        this.palifico = false;
        this.currentBid = Optional.empty();
        // reroll all dice
        this.userList.forEach(u -> this.usersStatus.put(u, this.usersStatus.get(u).rollDice()));
        this.goNextTurn();
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
        if (this.usersStatus.get(user).getRemainingDice() + quantity < 0) {
            return;
        }
        this.usersStatus.put(user,
                this.usersStatus.get(user).setRemainingDice(this.usersStatus.get(user).getRemainingDice() + quantity));
    }

    private void checkUserExistence(User user) throws ErrorTypeException {
        if (!this.userList.contains(user)) {
            throw new ErrorTypeException(ErrorType.USER_DOES_NOT_EXISTS);
        }
    }

    private void checkUserNotLose(User user) throws ErrorTypeException {
        if (this.getUserStatus(user).getRemainingDice() <= 0) {
            throw new ErrorTypeException(ErrorType.GAME_YOU_ALREADY_LOSE);
        }
    }

    private void checkUserTurn(User user) throws ErrorTypeException {
        if (!getTurn().equals(user)) {
            throw new ErrorTypeException(ErrorType.GAME_NOT_YOUR_TURN);
        }
    }

    private void checkUserCanUrge(User user) throws ErrorTypeException {
        // no one had bid yet, the one who bid can't urge, the one who is turn
        // can't urge
        if ((!this.getCurrentBid().isPresent()) || this.getBidUser().equals(user) || this.getTurn().equals(user)) {
            throw new ErrorTypeException(ErrorType.GAME_CANT_CALL_URGE_NOW);
        }
    }

    private int getBidDiceCount() {
        if (!this.getCurrentBid().isPresent()) {
            throw new IllegalStateException();
        }
        int bidUserJollyCount = this.userList.stream().filter(u -> this.getBidUser().equals(u))
                .mapToInt(u -> this.usersStatus.get(u).getDiceCount().get(1)).sum();
        int usersDiceCount = this.userList.stream()
                .mapToInt(u -> this.usersStatus.get(u).getDiceCount().get(this.getCurrentBid().get().getDiceValue()))
                .sum();

        return this.getCurrentBid().get().getDiceValue() == 1 ? usersDiceCount : usersDiceCount + bidUserJollyCount;
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
    public User getTurn() {
        return this.currentTurn;
    }

    private User getBidUser() {
        int idx = this.userList.indexOf(this.getTurn()) - 1;
        idx = idx < 0 ? this.userList.size() - 1 : idx;

        while (this.getUserStatus(this.userList.get(idx)).getRemainingDice() == 0) {
            idx = idx - 1 < 0 ? this.userList.size() - 1 : idx - 1;
        }

        return this.userList.get(idx);
    }

    @Override
    public void play(Bid bid, User user) throws ErrorTypeException {
        this.checkUserExistence(user);
        this.checkUserNotLose(user);
        this.checkUserTurn(user);

        // check if bid is valid
        if (this.getCurrentBid().isPresent()
                && !this.getCurrentBid().get().isNextBidValid(bid, this.turnIsPalifico())) {
            throw new ErrorTypeException(ErrorType.GAME_INVALID_BID);
        }

        this.currentBid = Optional.of(bid);
        this.goNextTurn();
    }

    @Override
    public Boolean doubt(User user) throws ErrorTypeException {
        this.checkUserExistence(user);
        this.checkUserNotLose(user);
        this.checkUserTurn(user);
        if (!this.getCurrentBid().isPresent()) {
            throw new ErrorTypeException(ErrorType.GAME_CANT_DOUBT_NOW);
        }

        if (this.getBidDiceCount() < this.getCurrentBid().get().getQuantity()) {
            // doubt is correct, bid user loss 1 dice
            this.removeDice(this.getBidUser(), 1);
            this.resetGame();
            return true;
        } else {
            // doubt is wrong, user loss 1 dice
            this.removeDice(user, 1);
            this.resetGame();
            return false;
        }
    }

    @Override
    public Boolean urge(User user) throws ErrorTypeException {
        this.checkUserExistence(user);
        this.checkUserNotLose(user);
        this.checkUserCanUrge(user);

        if (this.getBidDiceCount() == this.getCurrentBid().get().getQuantity()) {
            // urge is correct, all users (user excluded) loss 1 dice
            this.userList.stream().filter(u -> this.usersStatus.get(u).getRemainingDice() > 0)
                    .filter(u -> !u.equals(user)).forEach(u -> this.removeDice(u, 1));
            // user get 1 dice
            this.addDice(user, 1);
            this.resetGame();
            return true;
        } else {
            // urge is wrong, user loss 1 dice
            this.removeDice(user, 1);
            this.resetGame();
            return false;
        }

    }

    @Override
    public void callPalifico(User user) throws ErrorTypeException {
        this.checkUserExistence(user);

        // bid already started
        if (this.getCurrentBid().isPresent()) {
            throw new ErrorTypeException(ErrorType.GAME_CANT_CALL_PALIFICO_NOW);
        }

        this.usersStatus.put(user, this.getUserStatus(user).callPalifico());
        this.resetTurnStartTime();
        this.currentTurn = user;
    }

    @Override
    public GameSettings getSettings() {
        return this.settings;
    }

    @Override
    public Optional<Bid> getCurrentBid() {
        return this.currentBid;
    }

    @Override
    public Duration getTurnRemainingTime() {
        return Duration.between(this.turnStartTime, Instant.now());
    }

    @Override
    public PlayerStatus getUserStatus(User user) {
        return this.usersStatus.get(user);
    }

    @Override
    public boolean turnIsPalifico() {
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
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GameImpl other = (GameImpl) obj;
        if (id != other.id)
            return false;
        if (settings == null) {
            if (other.settings != null)
                return false;
        } else if (!settings.equals(other.settings))
            return false;
        return true;
    }

}

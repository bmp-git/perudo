package perudo.model.impl;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import perudo.model.Bid;
import perudo.model.Game;
import perudo.model.GameSettings;
import perudo.model.PlayerStatus;
import perudo.model.User;
import perudo.utility.ErrorType;
import perudo.utility.IdDispenser;
import perudo.utility.Response;
import perudo.utility.Result;
import perudo.utility.impl.ResponseImpl;
import perudo.utility.impl.ResultImpl;

public class GameImpl implements Game {

    private final int id;
    private final List<User> userList;
    private final Map<User, PlayerStatus> usersStatus;
    private final GameSettings settings;

    private User currentTurn;
    private Instant turnStartTime;
    private Bid currentBid;
    private User bidUser;
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
        this.palifico = false;
        this.bidUser = null;
        this.currentBid = new BidImpl(0, 0);
        // reroll all dice
        this.userList.forEach(u -> this.usersStatus.put(u, this.usersStatus.get(u).rollDice()));
        this.goNextTurn();
    }

    private void goNextTurn() {
        // check if at least 2 players are in game (at least 1 dice)
        if (this.usersStatus.entrySet().stream().filter(p -> p.getValue().getRemainingDice() > 0).count() <= 1) {
            throw new IllegalStateException("There is no next turn, only 1 or nobody have at least 1 dice");
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

    private boolean canUrge(User user) {
        // no one had yet, the one who bid can't urge, the one who is turn can't
        // urge
        return !(this.bidUser == null || this.bidUser.equals(user) || this.getTurn().equals(user));
    }

    private void removeDice(final User user, final int quantity) {
        this.usersStatus.put(user,
                this.usersStatus.get(user).setRemainingDice(this.usersStatus.get(user).getRemainingDice() - quantity));
    }

    private int getBidDiceCount() {
        int count = 0;
        for (User u : this.userList) {
            if (u.equals(this.bidUser) && this.getCurrentBid().getDiceValue() != 1) {
                // count jolly
                count += this.usersStatus.get(u).getDiceCount().get(1);
            }
            // count dice value
            count += this.usersStatus.get(u).getDiceCount().get(this.getCurrentBid().getDiceValue());
        }
        return count;

        // return
        // (this.getCurrentBid().getDiceValue()!=1?this.userList.stream().mapToInt(u
        // ->
        // this.usersStatus.get(u).getDiceCount().get(this.getCurrentBid().getDiceValue())).sum():0)
        // + this.userList.stream().filter(u ->
        // u.equals(this.bidUser)).mapToInt(u ->
        // this.usersStatus.get(u).getDiceCount().get(1)).sum();
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

    @Override
    public Result play(Bid bid, User user) {
        if (!this.usersStatus.containsKey(user)) {
            return ResultImpl.error(ErrorType.USER_DOES_NOT_EXISTS);
        }
        if (this.getUserStatus(user).getRemainingDice() <= 0) {
            return ResultImpl.error(ErrorType.GAME_YOU_ALREADY_LOSE);
        }
        if (!getTurn().equals(user)) {
            return ResultImpl.error(ErrorType.GAME_NOT_YOUR_TURN);
        }
        if (!this.getCurrentBid().isNextBidValid(bid, this.turnIsPalifico())) {
            return ResultImpl.error(ErrorType.GAME_INVALID_BID);
        }

        this.currentBid = bid;
        this.bidUser = user;
        this.goNextTurn();
        return ResultImpl.ok();
    }

    @Override
    public Response<Boolean> doubt(User user) {
        if (!this.usersStatus.containsKey(user)) {
            return ResponseImpl.empty(ErrorType.USER_DOES_NOT_EXISTS);
        }
        if (this.getUserStatus(user).getRemainingDice() <= 0) {
            return ResponseImpl.empty(ErrorType.GAME_YOU_ALREADY_LOSE);
        }
        if (!getTurn().equals(user)) {
            return ResponseImpl.empty(ErrorType.GAME_NOT_YOUR_TURN);
        }

        if (this.getBidDiceCount() < this.getCurrentBid().getQuantity()) {
            // doubt is correct, bid user loss 1 dice
            this.removeDice(this.bidUser, 1);
            if (!this.isOver()) {
                this.goNextTurn();
            }
            if (!this.isOver()) {
                this.goNextTurn();
            }
            this.resetGame();
            return ResponseImpl.of(true);
        } else {
            // doubt is wrong, user loss 1 dice
            this.removeDice(user, 1);
            if (!this.isOver()) {
                this.goNextTurn();
            }
            this.resetGame();
            return ResponseImpl.of(false);
        }
    }

    @Override
    public Response<Boolean> urge(User user) {
        if (!canUrge(user)) {
            return ResponseImpl.empty(ErrorType.GAME_CANT_CALL_URGE_NOW);
        }
        if (!this.usersStatus.containsKey(user)) {
            return ResponseImpl.empty(ErrorType.USER_DOES_NOT_EXISTS);
        }
        if (this.getUserStatus(user).getRemainingDice() <= 0) {
            return ResponseImpl.empty(ErrorType.GAME_YOU_ALREADY_LOSE);
        }

        if (this.getBidDiceCount() == this.getCurrentBid().getQuantity()) {
            // urge is correct, all users - user loss 1 dice
            this.userList.stream().filter(u -> this.usersStatus.get(u).getRemainingDice() > 0)
                    .filter(u -> !u.equals(user)).forEach(u -> this.removeDice(u, 1));
            if (!this.isOver()) {
                this.goNextTurn();
            }
            this.resetGame();
            return ResponseImpl.of(true);
        } else {
            // urge is wrong, user loss 1 dice
            this.removeDice(user, 1);
            if (!this.isOver()) {
                this.goNextTurn();
            }
            this.resetGame();
            return ResponseImpl.of(false);
        }

    }

    @Override
    public Result callPalifico(User user) {
        // bid already started
        if (this.bidUser != null) {
            return ResultImpl.error(ErrorType.GAME_CANT_CALL_PALIFICO_NOW);
        }
        if (!this.usersStatus.containsKey(user)) {
            return ResultImpl.error(ErrorType.USER_DOES_NOT_EXISTS);
        }
        // already called
        if (this.getUserStatus(user).hasCalledPalifico()) {
            return ResultImpl.error(ErrorType.GAME_PALIFICO_ALREADY_USED);
        }
        // must have 1 dice
        if (this.getUserStatus(user).getRemainingDice() != 1) {
            return ResultImpl.error(ErrorType.GAME_CANT_CALL_PALIFICO_NOW);
        }

        if (this.getUserStatus(user).getRemainingDice() == 0) {
            return ResultImpl.error(ErrorType.GAME_YOU_ALREADY_LOSE);
        }

        this.usersStatus.put(user, this.getUserStatus(user).setHasCalledPalifico(true));
        this.resetTurnStartTime();
        this.currentTurn = user;

        return ResultImpl.ok();
    }

    @Override
    public GameSettings getSettings() {
        return this.settings;
    }

    @Override
    public Bid getCurrentBid() {
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

}

package perudo.model.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import perudo.model.Bid;
import perudo.model.Game;
import perudo.model.GameRules;
import perudo.model.User;
import perudo.utility.ErrorType;
import perudo.utility.ErrorTypeException;

/**
 * The implementation of classic rules set.
 */
public class StandardGameRules implements GameRules {

    private static final long serialVersionUID = -1963421140009185891L;
    private static final int MIN_DICE_QUANTITY = 1;

    private void checkUserNotLose(final User user, final Game game) throws ErrorTypeException {
        if (game.getUserStatus(user).getRemainingDice() <= 0) {
            throw new ErrorTypeException(ErrorType.GAME_YOU_ALREADY_LOSE);
        }
    }

    private void checkUserTurn(final User user, final Game game) throws ErrorTypeException {
        if (!game.getTurn().equals(user)) {
            throw new ErrorTypeException(ErrorType.GAME_NOT_YOUR_TURN);
        }
    }

    @Override
    public void checkCanBid(final User user, final Game game) throws ErrorTypeException {
        this.checkUserNotLose(user, game);
        this.checkUserTurn(user, game);
    }

    @Override
    public void checkCanDoubt(final User user, final Game game) throws ErrorTypeException {
        this.checkUserNotLose(user, game);
        this.checkUserTurn(user, game);
        if (!game.getCurrentBid().isPresent()) {
            throw new ErrorTypeException(ErrorType.GAME_CANT_DOUBT_NOW);
        }
    }

    @Override
    public void checkCanUrge(final User user, final Game game) throws ErrorTypeException {
        this.checkUserNotLose(user, game);
        if ((!game.getCurrentBid().isPresent()) || game.getBidUser().get().equals(user)
                || game.getTurn().equals(user)) {
            throw new ErrorTypeException(ErrorType.GAME_CANT_CALL_URGE_NOW);
        }
    }

    @Override
    public void checkCanPalifico(final User user, final Game game) throws ErrorTypeException {
        this.checkUserNotLose(user, game);
        if (game.getCurrentBid().isPresent() || game.turnIsPalifico()
                || game.getUserStatus(user).getRemainingDice() != 1 || game.getUserStatus(user).hasCalledPalifico()) {
            throw new ErrorTypeException(ErrorType.GAME_CANT_CALL_PALIFICO_NOW);
        }
    }

    @Override
    public boolean bidValid(final Bid bid, final Game game) {
        // illegal bid
        if (bid.getDiceValue() > game.getSettings().getMaxDiceValue()) {
            return false;
        }

        // first bid
        if (!game.getCurrentBid().isPresent()) {
            return bid.getQuantity() >= MIN_DICE_QUANTITY;
        }

        if (game.turnIsPalifico()) {
            if (bid.getDiceValue() != game.getCurrentBid().get().getDiceValue()) {
                return false;
            }

            // only more dice
            return bid.getQuantity() > game.getCurrentBid().get().getQuantity();
        }

        // the minimum quantity playable
        return bid.getQuantity() >= this.nextBid(bid.getDiceValue(), game).getQuantity();
    }

    @Override
    public User nextTurnBid(final User user, final Game game) {
        if (game.isOver()) {
            throw new IllegalStateException("Game is over. Can't find next turn.");
        }
        User idx = user;
        do {
            idx = game.getUsers().get((game.getUsers().indexOf(idx) + 1) % game.getUsers().size());
        } while (game.getUserStatus(idx).getRemainingDice() <= 0);
        return idx;
    }

    @Override
    public User nextTurnPalifico(final User user, final Game game) {
        return user;
    }

    @Override
    public User nextTurnDoubt(final User user, final Game game, final boolean win) {
        return win ? game.getBidUser().get() : user;
    }

    @Override
    public User nextTurnUrge(final User user, final Game game, final boolean win) {
        return user;
    }

    @Override
    public boolean doubtWin(final Game game) {
        if (game.getCurrentBid().isPresent()) {
            return this.getRealBidDiceCount(game).get() < game.getCurrentBid().get().getQuantity();
        }
        return false;
    }

    @Override
    public boolean urgeWin(final Game game) {
        if (game.getCurrentBid().isPresent()) {
            return this.getRealBidDiceCount(game).get() == game.getCurrentBid().get().getQuantity();
        }
        return false;
    }

    @Override
    public Optional<Integer> getRealBidDiceCount(final Game game) {
        if (!game.getCurrentBid().isPresent()) {
            return Optional.empty();
        }
        final int bidUserJollyCount = game.getUsers().stream().filter(u -> game.getBidUser().get().equals(u))
                .mapToInt(u -> game.getUserStatus(u).getDiceCount().get(1)).sum();
        final int usersDiceCount = game.getUsers().stream()
                .mapToInt(u -> game.getUserStatus(u).getDiceCount().get(game.getCurrentBid().get().getDiceValue()))
                .sum();

        return Optional.of((game.getCurrentBid().get().getDiceValue() == 1 ? usersDiceCount
                : (usersDiceCount + bidUserJollyCount)));
    }

    @Override
    public Map<User, Integer> changesDiceUrge(final User user, final Game game, final boolean win) {
        final Map<User, Integer> map = new HashMap<>();
        if (win) {
            if (game.getUserStatus(user).getRemainingDice() < game.getSettings().getInitialDiceNumber()) {
                map.put(user, 1);
            }
            game.getUsers().stream().filter(u -> !u.equals(user) && !game.hasLost(u)).forEach(u -> map.put(u, -1));
        } else {
            map.put(user, -1);
        }

        return map;
    }

    @Override
    public Map<User, Integer> changesDiceDoubt(final User user, final Game game, final boolean win) {
        final Map<User, Integer> map = new HashMap<>();
        map.put(win ? game.getBidUser().get() : user, -1);
        return map;
    }

    @Override
    public int getMinDiceQuantity(final int diceValue, final Game game) {
        if (game.turnIsPalifico() && game.getCurrentBid().isPresent() && diceValue != game.getCurrentBid().get().getDiceValue()) {
            throw new IllegalStateException("You can't play different dice value if the turn is palifico!");
        }
        return this.nextBid(diceValue, game).getQuantity();
    }

    @Override
    public int getMaxDiceQuantity(final int diceValue, final Game game) {
        if (!game.getCurrentBid().isPresent()) {
            return game.getTotalRemainingDice();
        }
        final int minDiceQuantity = this.getMinDiceQuantity(diceValue, game);
        if (minDiceQuantity >= game.getTotalRemainingDice()) {
            return minDiceQuantity + 1;
        }
        return game.getTotalRemainingDice();
    }

    @Override
    public Bid nextBid(final int diceValue, final Game game) {
        int quantity = MIN_DICE_QUANTITY;
        if (!game.getCurrentBid().isPresent()) {
            return new BidImpl(quantity, diceValue);
        }

        if (diceValue == 1 && game.getCurrentBid().get().getDiceValue() == 1) {
            quantity = game.getCurrentBid().get().getQuantity() + 1;
        } else if (diceValue != 1 && game.getCurrentBid().get().getDiceValue() == 1) {
            quantity = game.getCurrentBid().get().getQuantity() * 2 + 1;
        } else if (diceValue == 1 && game.getCurrentBid().get().getDiceValue() != 1) {
            quantity = (game.getCurrentBid().get().getQuantity() + 1) / 2;
        } else if (diceValue > game.getCurrentBid().get().getDiceValue()) {
            quantity = game.getCurrentBid().get().getQuantity();
        } else if (diceValue <= game.getCurrentBid().get().getDiceValue()) {
            quantity = game.getCurrentBid().get().getQuantity() + 1;
        }

        return new BidImpl(quantity, diceValue);
    }
}

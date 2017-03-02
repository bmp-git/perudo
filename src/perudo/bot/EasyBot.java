package perudo.bot;

import java.util.Random;

import perudo.controller.Controller;
import perudo.model.Game;
import perudo.model.User;
import perudo.model.impl.BidImpl;
import perudo.utility.ErrorType;
import perudo.utility.ErrorTypeException;

public class EasyBot extends AbstractBot {
    private final Random random;

    public EasyBot(final Controller controller, final User user) {
        super(controller, user);
        this.random = new Random();
    }

    protected void play(final Game game) {
        this.game = game;
        if (this.game.isOver()) {
            return;
        }

        if (!this.game.getTurn().equals(this.user) && !this.game.canCallPalifico(this.user)) {
            return;
        }

        this.sleep(3000, 3000);

        if (this.game.canCallPalifico(this.user)) {
            this.controller.callPalifico(this.user);
            return;
        }
        
        this.sleep(2000, 2000);

        // my turn
        if (this.game.getTurn().equals(this.user)) {

            // choose if doubt or play
            if (this.game.getCurrentBid().isPresent()) {
                double quantityPrevision = this.game.getUserStatus(this.user).getDiceCount()
                        .get(this.game.getCurrentBid().get().getDiceValue());
                quantityPrevision += this.getOthersTotalDiceCount(this.game)
                        / this.game.getSettings().getMaxDiceValue();

                if (quantityPrevision < this.game.getCurrentBid().get().getQuantity()) {
                    this.controller.doubt(this.user);
                    return;
                }

                // 50% play on same dice - 50% play on my most present dice (if
                // not palifico)
                if (this.game.turnIsPalifico() || this.random.nextBoolean()) {
                    this.controller.play(this.user, this.game.getCurrentBid().get().nextBid());
                } else {
                    try {
                        this.controller.play(this.user,
                                this.game.getCurrentBid().get().nextBid(this.getMostPresentDiceValue(this.game)));
                    } catch (ErrorTypeException e) {
                        e.printStackTrace();
                        this.controller.play(this.user, this.game.getCurrentBid().get().nextBid());
                    }
                }
                return;

            } else {
                // do a safe play
                int value = this.getMostPresentDiceValue(this.game);
                int quantity = this.game.getUserStatus(this.user).getDiceCount().get(value);
                if (value != 1) {
                    quantity += this.game.getUserStatus(this.user).getDiceCount().get(1);
                }
                try {
                    this.controller.play(this.user, new BidImpl(quantity, value));
                    return;
                } catch (ErrorTypeException e) {
                    e.printStackTrace();
                    this.controller.closeNow(this.user);
                }
            }
        }
    }
    
    private void sleep(final int min_ms, final int rnd_ms) {
        try {
            // sleep from min_ms to min_ms + rnd_ms seconds
            Thread.sleep((random.nextInt(rnd_ms)) + min_ms);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }

    private int getMostPresentDiceValue(final Game game) {
        return game.getUserStatus(this.user).getDiceCount().entrySet().stream()
                .max((c1, c2) -> c1.getValue().compareTo(c2.getValue())).map(p -> p.getKey()).orElse(1);
    }

    private double getOthersTotalDiceCount(final Game game) {
        return game.getUsers().stream().filter(u -> !u.equals(this.user))
                .mapToDouble(u -> game.getUserStatus(u).getRemainingDice()).sum();
    }

    @Override
    public void showError(ErrorType errorType) {
        super.showError(errorType);

        this.executor.execute(() -> {
            this.play(this.game);
        });
    }
}

package perudo.bot;

import java.util.Random;

import perudo.controller.Controller;
import perudo.model.Game;
import perudo.model.User;
import perudo.model.impl.BidImpl;

/**
 * A implementation of the dumbest bot ever.
 */
public class EasyBot extends AbstractBot {

    private static final int SLEEP_TIME_MIN_1 = 3000;
    private static final int SLEEP_TIME_MIN_RND_1 = 3000;
    private static final int SLEEP_TIME_MIN_2 = 2000;
    private static final int SLEEP_TIME_MIN_RND_2 = 2000;

    private final Random random;

    /**
     * This is called when the bot should evaluate what to do.
     * 
     * @param controller
     *            the controller to use
     * 
     * @param user
     *            the controller to use
     */
    public EasyBot(final Controller controller, final User user) {
        super(controller, user);
        this.random = new Random();
    }

    @Override
    protected void play() {
        if (this.getGame().isOver()) {
            return;
        }

        if (!this.getGame().getTurn().equals(this.getUser()) && !this.getGame().canPalifico(this.getUser())) {
            return;
        }

        this.sleep(SLEEP_TIME_MIN_1, SLEEP_TIME_MIN_RND_1);

        if (this.getGame().canPalifico(this.getUser())) {
            this.getController().callPalifico(this.getUser());
            return;
        }

        this.sleep(SLEEP_TIME_MIN_2, SLEEP_TIME_MIN_RND_2);

        // my turn
        if (this.getGame().getTurn().equals(this.getUser())) {

            // choose if doubt or play
            if (this.getGame().getCurrentBid().isPresent()) {
                double quantityPrevision = this.getGame().getUserStatus(this.getUser()).getDiceCount()
                        .get(this.getGame().getCurrentBid().get().getDiceValue());
                quantityPrevision += this.getOthersTotalDiceCount(this.getGame())
                        / this.getGame().getSettings().getMaxDiceValue();

                if (quantityPrevision < this.getGame().getCurrentBid().get().getQuantity()) {
                    this.getController().doubt(this.getUser());
                    return;
                }

                // 50% play on same dice - 50% play on my most present dice (if
                // not palifico)
                if (this.getGame().turnIsPalifico() || this.random.nextBoolean()) {
                    this.getController().play(this.getUser(), this.getGame().getCurrentBid().get().nextBid());
                } else {
                    this.getController().play(this.getUser(),
                            this.getGame().getCurrentBid().get().nextBid(this.getMostPresentDiceValue(this.getGame())));
                }
                return;

            } else {
                // do a safe play
                final int value = this.getMostPresentDiceValue(this.getGame());
                int quantity = this.getGame().getUserStatus(this.getUser()).getDiceCount().get(value);
                if (value != 1) {
                    quantity += this.getGame().getUserStatus(this.getUser()).getDiceCount().get(1);
                }
                this.getController().play(this.getUser(), new BidImpl(quantity, value));
                return;
            }
        }
    }

    private void sleep(final int minMs, final int rndMs) {
        try {
            // sleep from min_ms to min_ms + rnd_ms seconds
            Thread.sleep((random.nextInt(rndMs)) + minMs);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }

    private int getMostPresentDiceValue(final Game game) {
        return game.getUserStatus(this.getUser()).getDiceCount().entrySet().stream()
                .max((c1, c2) -> c1.getValue().compareTo(c2.getValue())).map(p -> p.getKey()).orElse(1);
    }

    private double getOthersTotalDiceCount(final Game game) {
        return game.getUsers().stream().filter(u -> !u.equals(this.getUser()))
                .mapToDouble(u -> game.getUserStatus(u).getRemainingDice()).sum();
    }

}

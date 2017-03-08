package perudo.bot;

import perudo.controller.Controller;
import perudo.model.User;
import perudo.model.UserType;
import perudo.view.View;

/**
 * The factory for all types of bots.
 */
public final class BotFactory {

    private BotFactory() {

    }

    /**
     * Generate the bot view.
     * 
     * @param controller
     *            The controller that will be used by the bot.
     * 
     * @param user
     *            The user of the bot. The bot will be created based on the type
     *            of the user. Only UserType that are bot type are allowed.
     * 
     * @return the bot view.
     */
    public static View createBot(final Controller controller, final User user) {
        if (!user.getType().isBot()) {
            throw new IllegalArgumentException();
        }
        if (user.getType().equals(UserType.BOT_EASY)) {
            return new EasyBot(controller, user);
        }
        throw new IllegalStateException();
    }
}

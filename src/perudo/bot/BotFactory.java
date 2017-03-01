package perudo.bot;

import perudo.controller.Controller;
import perudo.model.User;
import perudo.model.UserType;
import perudo.view.View;

public class BotFactory {
    public static View createBot(final Controller controller, final User user) {
        if(!user.getType().isBot()){
            throw new IllegalArgumentException();
        }
        if(user.getType().equals(UserType.BOT_EASY)){
            return new EasyBot(controller, user);
        }
        throw new IllegalStateException();
    }
}

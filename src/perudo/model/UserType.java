package perudo.model;

/**
 * Represent the users type.
 */
public enum UserType {
    /**
     * A normal user.
     */
    PLAYER(false),
    /**
     * A bot which difficulty is easy.
     */
    BOT_EASY(true);

    private final boolean isBot;

    UserType(final boolean isBot) {
        this.isBot = isBot;
    }

    /**
     * Gets this type of user indicates a player or a bot.
     * 
     * @return true if this type is bot-type, false otherwise
     */
    public boolean isBot() {
        return this.isBot;
    }
}

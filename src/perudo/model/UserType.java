package perudo.model;

public enum UserType {
    PLAYER(false), BOT_EASY(true);

    private final boolean isBot;

    private UserType(final boolean isBot) {
        this.isBot = isBot;
    }

    public boolean isBot() {
        return this.isBot;
    }
}

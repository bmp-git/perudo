package perudo.utility;

/**
 * This enumerator contains all kind of errors that can occur in this
 * game/application.
 */
public enum ErrorType {
    /**
     * The name is already taken.
     */
    NAME_ALREADY_TAKEN(1000, "The name is already taken."),

    /**
     * The lobby does not exist.
     */
    LOBBY_NOT_EXISTS(2000, "The lobby does not exist."),
    /**
     * The lobby already exist.
     */
    LOBBY_ALREADY_EXISTS(2004, "The lobby already exist."),
    /**
     * Lobby already joined.
     */
    LOBBY_ALREADY_JOINED(2001, "Lobby already joined."),
    /**
     * The lobby is full.
     */
    LOBBY_IS_FULL(2003, "The lobby is full."),
    /**
     * The lobby can not start.
     */
    LOBBY_CANT_START_GAME(2006, "The lobby can not start."),
    /**
     * You are not the owner of this lobby.
     */
    LOBBY_USER_NOT_OWNER(2007, "You are not the owner of this lobby."),
    /**
     * The user is not in the lobby.
     */
    LOBBY_USER_NOT_PRESENT(2008, "The user is not in the lobby."),
    /**
     * The lobby is already started.
     */
    LOBBY_ALREADY_STARTED(2009, "The lobby is already started."),

    /**
     * The user is in game.
     */
    USER_IS_IN_GAME(3000, "The user is in game."),
    /**
     * The user is in a lobby.
     */
    USER_IS_IN_LOBBY(3001, "The user is in a lobby."),
    /**
     * The user already exists.
     */
    USER_ALREADY_EXISTS(3002, "The user already exists."),
    /**
     * The user does not exists.
     */
    USER_DOES_NOT_EXISTS(3003, "The user does not exists."),
    /**
     * The user do not own the lobby.
     */
    USER_DOES_NOT_OWN_LOBBY(3004, "The user do not own the lobby."),
    /**
     * The user is not in a lobby.
     */
    USER_IS_NOT_IN_A_LOBBY(3005, "The user is not in a lobby."),
    /**
     * The user is not in a game.
     */
    USER_IS_NOT_IN_A_GAME(3006, "The user is not in a game."),
    /**
     * The user name is too long.
     */
    USER_NAME_TOO_LONG(3007, "The user name is too long."),
    /**
     * The user name is too long.
     */
    USER_NAME_TOO_SHORT(3008, "The user name is too short."),
    /**
     * The user name contains invalid characters.
     */
    USER_NAME_INVALID(3009, "The user name contains invalid characters."),

    /**
     * It is not your turn.
     */
    GAME_NOT_YOUR_TURN(4000, "It is not your turn."),
    /**
     * Invalid bid.
     */
    GAME_INVALID_BID(4001, "Invalid bid."),
    /**
     * Palifico already called.
     */
    GAME_PALIFICO_ALREADY_USED(4002, "Palifico already called."),
    /**
     * You can not call palifico now.
     */
    GAME_CANT_CALL_PALIFICO_NOW(4003, "You can not call palifico now."),
    /**
     * You can not urge now.
     */
    GAME_CANT_CALL_URGE_NOW(4004, "You can not urge now."),
    /**
     * You already lose.
     */
    GAME_YOU_ALREADY_LOSE(4005, "You already lose."),
    /**
     * You can not doubt now.
     */
    GAME_CANT_DOUBT_NOW(4008, "You can not doubt now."),
    /**
     * The game does not exist.
     */
    GAME_NOT_EXISTS(4006, "The game does not exist."),
    /**
     * The game already exist.
     */
    GAME_ALREADY_EXISTS(4007, "The game already exist."),
    /**
     * The game is over.
     */
    GAME_IS_OVER(4009, "The game is over."),

    /**
     * Network error or application not updated.
     */
    METHOD_NOT_FOUND(5001, "Network error or application not updated."),
    /**
     * Network error or application not updated.
     */
    METHOD_INVALID(5002, "Network error or application not updated.");

    private final int id;
    private final String message;

    ErrorType(final int id, final String message) {
        this.id = id;
        this.message = message;

    }

    /**
     * Gets the id for this kind of error.
     * 
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the message for this kind of error.
     * 
     * @return the message string
     */
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ErrorType (" + id + "): " + message;
    }
}

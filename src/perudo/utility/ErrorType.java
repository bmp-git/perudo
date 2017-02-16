package perudo.utility;

public enum ErrorType {
	NAME_ALREADY_TAKEN(1000, "The name is already taken."),
	
	LOBBY_NOT_EXISTS(2000, "The lobby does not exit."),
	LOBBY_ALREADY_EXISTS(2004, " "),
	
	LOBBY_ALREADY_JOINED(2001, "You are already in a lobby."),
	LOBBY_IS_FULL(2003, "The lobby is full."),
	LOBBY_CANT_START_GAME(2006, " "),
	LOBBY_USER_NOT_OWNER(2007, " "),
	LOBBY_USER_NOT_PRESENT(2008, " "),
	LOBBY_ALREADY_STARTED(2009, " "),

	USER_IS_IN_GAME(3000, "The user is in game."),
	USER_IS_IN_LOBBY(3001, "The user is in a lobby."),
	USER_ALREADY_EXISTS(3002, "The user already exists."),
    USER_DOES_NOT_EXISTS(3003, "The user does not exists."),
    USER_DOES_NOT_OWN_LOBBY(3004, " "),
    USER_IS_NOT_IN_A_LOBBY(3005, "The user is not in a lobby."),
    USER_IS_NOT_IN_A_GAME(3006, "The user is not in a game."),
    
    GAME_NOT_YOUR_TURN(4000, " "),
    GAME_INVALID_BID(4001, " "),
    GAME_PALIFICO_ALREADY_USED(4002, " "),
    GAME_CANT_CALL_PALIFICO_NOW(4003, " "),
    GAME_CANT_CALL_URGE_NOW(4004, " "),
    GAME_YOU_ALREADY_LOSE(4005, " "),
    GAME_NOT_EXISTS(4006, " "),
    GAME_ALREADY_EXISTS(4007, " "),
    GAME_CANT_DOUBT_NOW(4008, " "),
    
    NONE(0, "No error.");
    
	private int id;
	private String message;

	private ErrorType(int id, String message) {
		this.id = id;
		this.message = message;

	}

	public int getId() {
		return id;
	}

	public String getMessage() {
		return message;
	}
	
	@Override
	public String toString(){
		return "ErrorType (" + id + "): " + message;
	}
}

package perudo.model;

import java.util.Set;

import perudo.utility.Response;
import perudo.utility.Result;

public interface Lobby {
	int getId();
	
	Set<User> getUsers();

	Result addUser(User user);

	Result removeUser(User user);

	Response<Game> startGame(User starter);

	GameSettings getInfo();

	User getOwner();
	
	//support methods
	default int getFreeSpace(){
		return getInfo().getMaxPlayer() - getUsers().size();
	}
}

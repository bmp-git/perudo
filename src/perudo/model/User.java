package perudo.model;

public interface User {

	int getId();

	String getName();
	
	User changeName(String newName);

}

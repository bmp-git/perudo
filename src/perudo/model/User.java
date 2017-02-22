package perudo.model;

import java.io.Serializable;

import perudo.utility.ErrorTypeException;

public interface User extends Serializable{

    /**
     * Gets the unique id of the user.
     * 
     * @return id value
     */
    int getId();

    /**
     * Gets the user's name
     * 
     * @return the name
     */
    String getName();

    /**
     * Set a new name for the user
     * 
     * @return the user with the changed name
     */
    User changeName(String newName) throws ErrorTypeException;

}

package perudo.model;

import java.io.Serializable;

import perudo.utility.ErrorTypeException;

/**
 * Represent a user.
 */
public interface User extends Serializable {

    /**
     * Gets the unique id of the user.
     * 
     * @return id value
     */
    int getId();

    /**
     * Gets the user's name.
     * 
     * @return the name
     */
    String getName();

    /**
     * Sets a new name for the user.
     * 
     * @param newName
     *            the wanted name
     * 
     * @throws ErrorTypeException
     *             if an error occurs during invocation
     * 
     * @return the user with the changed name
     */
    User changeName(String newName) throws ErrorTypeException;

    /**
     * Gets the type of this user.
     * 
     * @return the type
     */
    UserType getType();
}

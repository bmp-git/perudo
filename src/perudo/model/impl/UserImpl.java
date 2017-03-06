package perudo.model.impl;

import perudo.model.User;
import perudo.model.UserType;
import perudo.utility.ErrorType;
import perudo.utility.ErrorTypeException;
import perudo.utility.IdDispenser;

/**
 * The implementation of User interface.
 */
public class UserImpl implements User {

    private static final long serialVersionUID = 3324295008073807947L;

    /**
     * Maximum characters count allowed for user name.
     */
    public static final int MAX_USER_NAME_LENGTH = 12;
    /**
     * Minimum characters count allowed for user name.
     */
    public static final int MIN_USER_NAME_LENGTH = 2;
    /**
     * The set of allowed characters for user name.
     */
    public static final String PERMITTED_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    /**
     * Gets a new user with a default prefix name. If the prefix generate
     * problems it will be omitted.
     * 
     * @param prefix
     *            the prefix
     * 
     * @param type
     *            the user's type
     * 
     * @return an initialized new user
     */
    public static User getNewAnonymousUser(final String prefix, final UserType type) {
        final int id = IdDispenser.getUserIdDispenser().getNextId();
        try {
            return new UserImpl(id, prefix + id, type);
        } catch (ErrorTypeException e) {
            // try without the prefix
            try {
                return new UserImpl(id, Integer.toString(id), type);
            } catch (ErrorTypeException e1) {

            }
            IdDispenser.getUserIdDispenser().freeId(id);
            // can't generate an anonymous user correctly
            throw new IllegalStateException();
        }
    }

    /**
     * Create a standard user.
     * 
     * @param name
     *            the name of the user
     * 
     * @throws ErrorTypeException
     *             if the name is not following the rules
     * 
     * @return an instance of the User
     */
    public static User createPlayer(final String name) throws ErrorTypeException {
        return new UserImpl(IdDispenser.getUserIdDispenser().getNextId(), name, UserType.PLAYER);
    }

    /**
     * Create a bot user type.
     * 
     * @param name
     *            the name of the bot
     * 
     * @param type
     *            the bot's type
     * 
     * @throws ErrorTypeException
     *             if the name is not following the rules
     * 
     * @return an instance of the User
     */
    public static User createBot(final String name, final UserType type) throws ErrorTypeException {
        if (!type.isBot()) {
            throw new IllegalArgumentException();
        }
        return new UserImpl(IdDispenser.getUserIdDispenser().getNextId(), name, type);
    }

    private final int id;
    private final String name;
    private final UserType type;

    private UserImpl(final int id, final String name, final UserType type) throws ErrorTypeException {
        this.id = id;
        this.name = name;
        this.type = type;

        if (name.length() > MAX_USER_NAME_LENGTH) {
            throw new ErrorTypeException(ErrorType.USER_NAME_TOO_LONG);
        }
        if (name.length() < MIN_USER_NAME_LENGTH) {
            throw new ErrorTypeException(ErrorType.USER_NAME_TOO_SHORT);
        }
        for (int i = 0; i < name.length(); i++) {
            if (!PERMITTED_CHARACTERS.contains(Character.toString(name.charAt(i)))) {
                throw new ErrorTypeException(ErrorType.USER_NAME_INVALID);
            }
        }
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public User changeName(final String newName) throws ErrorTypeException {
        return new UserImpl(this.getId(), newName, this.type);
    }

    @Override
    public UserType getType() {
        return this.type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        UserImpl other = (UserImpl) obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }
}

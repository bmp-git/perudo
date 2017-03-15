package test.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import perudo.model.User;
import perudo.model.UserType;
import perudo.model.impl.UserImpl;
import perudo.utility.ErrorType;
import perudo.utility.ErrorTypeException;

/**
 * Test for User implementation.
 */
public class UserTest {
    /**
     * The main test.
     */
    @org.junit.Test
    public void main() {
        User u1 = null, u2 = null;
        try {
            u1 = UserImpl.createPlayer("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        } catch (ErrorTypeException e) {
            assertEquals(e.getErrorType(), ErrorType.USER_NAME_TOO_LONG);
        }
        try {
            u1 = UserImpl.createPlayer("");
        } catch (ErrorTypeException e) {
            assertEquals(e.getErrorType(), ErrorType.USER_NAME_TOO_SHORT);
        }
        try {
            u1 = UserImpl.createPlayer("!?\"£$%&/()");
        } catch (ErrorTypeException e) {
            assertEquals(e.getErrorType(), ErrorType.USER_NAME_INVALID);
        }
        try {
            u1 = UserImpl.createPlayer("Nome1");
            u2 = UserImpl.createPlayer("Nome2");
        } catch (ErrorTypeException e) {
            throw new IllegalStateException("Should be ok, 1");
        }

        assertEquals(u1.getType(), UserType.PLAYER);
        assertEquals(u1.getName(), "Nome1");
        assertNotEquals(u1.getId(), u2.getId());
        assertNotEquals(u1, u2);

        User u3 = null;
        try {
            u3 = u1.changeName("NuovoNome");
        } catch (ErrorTypeException e) {
            throw new IllegalStateException("Should be ok, 2");
        }
        assertEquals(u3.getName(), "NuovoNome");
        assertEquals(u1.getName(), "Nome1"); // immutable
        assertEquals(u1, u3); // same user
        assertEquals(u1.getId(), u3.getId());

        try {
            u1 = UserImpl.createBot("bot", UserType.PLAYER);
            throw new IllegalStateException("Can't create bot with UserType.PLAYER");
        } catch (ErrorTypeException e) {
            throw new IllegalStateException("Should be ok, 3");
        } catch (IllegalArgumentException ex) {
            assertEquals(ex.getClass(), IllegalArgumentException.class);
        }

        try {
            u1 = UserImpl.createBot("bot", UserType.BOT_EASY);
        } catch (ErrorTypeException e) {
            throw new IllegalStateException("Should be ok, 4");
        }

        assertEquals(u1.getName(), "bot");
        assertTrue(u1.getType().isBot());
    }
}

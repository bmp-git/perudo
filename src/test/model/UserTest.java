package test.model;

import perudo.model.User;
import perudo.model.impl.UserImpl;
import perudo.utility.ErrorType;
import perudo.utility.ErrorTypeException;

import static org.junit.Assert.*;

public class UserTest {
    @org.junit.Test
    public void main() {
        User u1 = null, u2 = null;
        try{
            u1 = UserImpl.createPlayer("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        }catch(ErrorTypeException e){
            assertEquals(e.getErrorType(), ErrorType.USER_NAME_TOO_LONG);
        }
        try{
            u1 = UserImpl.createPlayer("");
        }catch(ErrorTypeException e){
            assertEquals(e.getErrorType(), ErrorType.USER_NAME_TOO_SHORT);
        }
        try{
            u1 = UserImpl.createPlayer("!?\"£$%&/()");
        }catch(ErrorTypeException e){
            assertEquals(e.getErrorType(), ErrorType.USER_NAME_INVALID);
        }
        try {
            u1 = UserImpl.createPlayer("Nome");
            u2 = UserImpl.createPlayer("Nome");
        } catch (ErrorTypeException e) {
            throw new IllegalStateException("Should be ok");
        }

        assertEquals(u1.getName(), "Nome");
        assertNotEquals(u1.getId(), u2.getId());
        assertNotEquals(u1, u2);

        User u3 = null;
        try {
            u3 = u1.changeName("NuovoNome");
        } catch (ErrorTypeException e) {
            throw new IllegalStateException("Should be ok");
        }
        assertEquals(u3.getName(), "NuovoNome");
        assertEquals(u1.getName(), "Nome"); // immutable
        assertEquals(u1, u3); // same user
        assertEquals(u1.getId(), u3.getId());
    }
}

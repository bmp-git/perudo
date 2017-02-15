package test.model;

import perudo.model.User;
import perudo.model.impl.UserImpl;
import static org.junit.Assert.*;

public class UserTest {
    @org.junit.Test
    public void main() {
        User u1 = new UserImpl("Nome");
        User u2 = new UserImpl("Nome");
        
        assertEquals(u1.getName(), "Nome");
        assertNotEquals(u1.getId(), u2.getId());
        assertNotEquals(u1, u2);
        
        User u3 = u1.changeName("NuovoNome");
        assertEquals(u3.getName(), "NuovoNome");
        assertEquals(u1.getName(), "Nome"); //immutable
        assertEquals(u1, u3); //same user
        assertEquals(u1.getId(), u3.getId()); 
    }
}

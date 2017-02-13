package perudo.model.impl;

import perudo.model.User;
import perudo.utility.IdDispenser;

public class UserImpl implements User {

    private final int id;
    private final String name;

    public UserImpl(String name) {
        this(IdDispenser.getUserIdDispenser().getNextId(), name);
    }

    private UserImpl(int id, String name) {
        this.id = id;
        this.name = name;
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
    public User changeName(String newName) {
        return new UserImpl(this.getId(), newName);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserImpl other = (UserImpl) obj;
        if (id != other.id)
            return false;
        return true;
    }

}

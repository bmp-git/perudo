package perudo.utility;

import java.util.LinkedList;
import java.util.List;

/**
 * This class contains the dispenser for the model's id.
 */
public final class IdDispenser {

    private static final int MAX_UNUSED_ID = 128;
    private static final IdDispenser LOBBIES = new IdDispenser();
    private static final IdDispenser GAMES = new IdDispenser();
    private static final IdDispenser USER = new IdDispenser();

    private int nextId;
    private final List<Integer> unusedId;

    private IdDispenser() {
        this.nextId = 1;
        this.unusedId = new LinkedList<>();
    }

    /**
     * Gets the next generated id.
     * 
     * @return the id
     */
    public synchronized int getNextId() {
        return (unusedId.size() > MAX_UNUSED_ID || (nextId == Integer.MAX_VALUE && !unusedId.isEmpty()))
                ? unusedId.remove(0) : nextId++;
    }

    /**
     * Frees an id generated in the past.
     * 
     * @param id
     *            the id to free
     */
    public synchronized void freeId(final int id) {
        if (id >= nextId) {
            throw new IllegalArgumentException("Can't free a non-generated id.");
        }
        if (!unusedId.contains(id)) {
            unusedId.add(id);
        }
    }

    /**
     * Gets the IdDispenser for the lobbies.
     * 
     * @return the IdDispenser required
     */
    public static IdDispenser getLobbyIdDispenser() {
        return LOBBIES;
    }

    /**
     * Gets the IdDispenser for the games.
     * 
     * @return the IdDispenser required
     */
    public static IdDispenser getGameIdDispenser() {
        return GAMES;
    }

    /**
     * Gets the IdDispenser for the users.
     * 
     * @return the IdDispenser required
     */
    public static IdDispenser getUserIdDispenser() {
        return USER;
    }

}

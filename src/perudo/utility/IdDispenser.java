package perudo.utility;

import java.util.LinkedList;
import java.util.List;

public class IdDispenser {
    private static final int MAX_UNUSED_ID = 128;

    private int nextId;
    private List<Integer> unusedId;

    private IdDispenser() {
        this.nextId = 1;
        this.unusedId = new LinkedList<>();
    }

    public synchronized int getNextId() {
        return (unusedId.size() > MAX_UNUSED_ID || (nextId == Integer.MAX_VALUE && unusedId.size() > 0))
                ? unusedId.remove(0) : nextId++;
    }

    public synchronized void freeId(int id) {
        if (!unusedId.contains(id)) {
            unusedId.add(id);
        }
    }

    private static final IdDispenser lobbies = new IdDispenser();

    public static IdDispenser getLobbyIdDispenser() {
        return lobbies;
    }

    private static final IdDispenser games = new IdDispenser();

    public static IdDispenser getGameIdDispenser() {
        return games;
    }

    private static final IdDispenser users = new IdDispenser();

    public static IdDispenser getUserIdDispenser() {
        return users;
    }

}

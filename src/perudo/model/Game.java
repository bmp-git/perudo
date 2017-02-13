package perudo.model;

import java.time.Duration;
import java.util.List;
import perudo.utility.ErrorTypeException;

public interface Game {

    int getId();

    List<User> getUsers();

    PlayerStatus getUserStatus(User user);

    void play(Bid bid, User user) throws ErrorTypeException;

    Boolean doubt(User user) throws ErrorTypeException;

    Boolean urge(User user) throws ErrorTypeException;

    void callPalifico(User user) throws ErrorTypeException;

    boolean turnIsPalifico();

    GameSettings getSettings();

    User getTurn();

    Bid getCurrentBid();

    Duration getTurnRemainingTime();

    default boolean isOver() {
        return getUsers().stream().filter(u -> getUserStatus(u).getRemainingDice() > 0).count() <= 1;
    }
}

package perudo.model;

import java.time.Duration;
import java.util.List;

import perudo.utility.Response;
import perudo.utility.Result;

public interface Game {

    int getId();

    List<User> getUsers();

    PlayerStatus getUserStatus(User user);

    Result play(Bid bid, User user);

    Response<Boolean> doubt(User user);

    Response<Boolean> urge(User user);

    Result callPalifico(User user);

    boolean turnIsPalifico();

    GameSettings getSettings();

    User getTurn();

    Bid getCurrentBid();

    Duration getTurnRemainingTime();

    default boolean isOver() {
        return getUsers().stream().filter(u -> getUserStatus(u).getRemainingDice() > 0).count() <= 1;
    }
}

package test.model;

/**
 * All model's tests.
 */
public class AllTest {

    /**
     * Run User test.
     */
    @org.junit.Test
    public void userTest() {
        new UserTest().main();
    }

    /**
     * Run Lobby test.
     */
    @org.junit.Test
    public void lobbyTest() {
        new LobbyTest().main();
    }

    /**
     * Run PlayerStatus test.
     */
    @org.junit.Test
    public void playerStatusTest() {
        new PlayerStatusTest().main();
    }

    /**
     * Run GameSettings test.
     */
    @org.junit.Test
    public void gameSettingsTest() {
        new GameSettingsTest().main();
    }

    /**
     * Run Bid test.
     */
    @org.junit.Test
    public void bidTest() {
        new BidTest().main();
    }

    /**
     * Run Model test.
     */
    @org.junit.Test
    public void modelTest() {
        new ModelTest().main();
    }

    /**
     * Run Game test.
     */
    @org.junit.Test
    public void gameTest() {
        new GameTest().main();
    }
}

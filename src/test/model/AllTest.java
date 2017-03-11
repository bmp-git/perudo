package test.model;

public class AllTest {
    @org.junit.Test
    public void userTest() {
        new UserTest().main();
    }

    @org.junit.Test
    public void lobbyTest() {
        new LobbyTest().main();
    }

    @org.junit.Test
    public void playerStatusTest() {
        new PlayerStatusTest().main();
    }

    @org.junit.Test
    public void gameSettingsTest() {
        new GameSettingsTest().main();
    }

    @org.junit.Test
    public void bidTest() {
        new BidTest().main();
    }

    @org.junit.Test
    public void modelTest() {
        new ModelTest().main();
    }

    @org.junit.Test
    public void gameTest() {
        new GameTest().main();
    }
}

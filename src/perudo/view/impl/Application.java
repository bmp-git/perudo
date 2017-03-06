package perudo.view.impl;

import java.io.IOException;
import java.time.Duration;
import perudo.controller.Controller;
import perudo.controller.impl.StandardControllerImpl;
import perudo.model.UserType;
import perudo.model.impl.GameSettingsImpl;
import perudo.view.impl.Startpage.StartingFrameResult;

public class Application {

    public static void main(String[] args) throws InterruptedException {
        //testGame(); 
        Startpage start = new Startpage();
        start.showFrame();
        while (!start.getResult().isPresent()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
            }
        }

        if (start.getResult().get() == StartingFrameResult.SINGLEPLAYER) {
            ControllerSingleton.setSingleplayerController();
        } else if (start.getResult().get() == StartingFrameResult.MULTIPLAYER) {
            try {
                ControllerSingleton.setMultiplayerController("2.224.173.8", 45555);
            } catch (IOException e) {
                System.out.println("Server non raggiungibile..");
                System.exit(1);
            }
        } else if (start.getResult().get() == StartingFrameResult.EXIT) {
            System.exit(1);
        }

        ViewImpl view = new ViewImpl();
        ViewImpl view2 = new ViewImpl();
        view.await();
        view2.await();

        try {
            ControllerSingleton.getController().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("exited");
    }
    
    public static void testGame() throws InterruptedException{
        ControllerSingleton.setSingleplayerController();
        ViewImpl view = new ViewImpl();
        ControllerSingleton.getController().createLobby(view.getUser(), new GameSettingsImpl(4,6,2,Duration.ofSeconds(15),"LOL"));
        Thread.sleep(500);
        ControllerSingleton.getController().addBotToLobby(view.getUser(), view.getLobby(), UserType.BOT_EASY);
        ControllerSingleton.getController().addBotToLobby(view.getUser(), view.getLobby(), UserType.BOT_EASY);
        Thread.sleep(500);
        ControllerSingleton.getController().startLobby(view.getUser());
        
        view.await();
    }

}

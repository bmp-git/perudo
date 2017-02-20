package perudo.view.impl;

import perudo.view.impl.Startpage.StartingFrameResult;

public class Application {

    public static void main(String[] args) {
        Startpage start = new Startpage();
        start.showFrame();
        while(!start.getResult().isPresent()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
            }
        }
        
        if(start.getResult().get() == StartingFrameResult.SINGLEPLAYER) {
            ControllerSingleton.setSingleplayerController();
        } else if (start.getResult().get() == StartingFrameResult.MULTIPLAYER) {
            ControllerSingleton.setMultiplayerController();
        } else if (start.getResult().get() == StartingFrameResult.EXIT) {
            System.exit(1);
        }
        
        ViewImpl view = new ViewImpl();
    }

}

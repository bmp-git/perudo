package perudo.view.impl;

import java.io.IOException;
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
            ControllerSingleton.setMultiplayerController("2.224.173.8",45555);
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
    }

}

package perudo.view.impl;

import java.io.IOException;
import perudo.view.impl.StartFrame.StartingFrameResult;

/**
 * Application starting class.
 */
public final class Application {

    private static final int SERVER_PORT = 45555;

    private Application() {

    }

    /**
     * Start application.
     * 
     * @param args
     *            --
     */
    public static void main(final String[] args) {
        final StartFrame start = new StartFrame();
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
                ControllerSingleton.setMultiplayerController("2.224.173.8", SERVER_PORT);
            } catch (IOException e) {
                System.out.println("Server non raggiungibile..");
                System.exit(1);
            }
        } else if (start.getResult().get() == StartingFrameResult.EXIT) {
            System.exit(1);
        }

        ViewImpl view = new ViewImpl();
        //ViewImpl view2 = new ViewImpl();
        view.await();
        //view2.await();

        try {
            ControllerSingleton.getController().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("exited");
        System.exit(0);
    }
}

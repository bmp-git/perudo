package perudo.view.impl;

import java.io.IOException;

import perudo.controller.Controller;
import perudo.controller.impl.StandardControllerImpl;
import perudo.controller.net.client.ControllerClientImpl;

public class ControllerSingleton {
    private static Controller SINGLETON = null;
    private static ControllerType CONTROLLER_TYPE = null;

    private ControllerSingleton() {
    };

    public enum ControllerType {
        SINGLEPLAYER, MULTIPLAYER;
    }

    public static Controller getController() {
        return SINGLETON;
    }

    public static void setSingleplayerController() {
        SINGLETON = new StandardControllerImpl();
        CONTROLLER_TYPE = ControllerType.SINGLEPLAYER;
    }

    public static void setMultiplayerController(String ip, int port) throws IOException {
        SINGLETON = ControllerClientImpl.createFromServerName(ip, port);
        CONTROLLER_TYPE = ControllerType.MULTIPLAYER;
    }

    public static ControllerType getControllerType() {
        return CONTROLLER_TYPE;
    }
}

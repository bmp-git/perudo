package perudo.view.impl;

import java.io.IOException;

import perudo.controller.Controller;
import perudo.controller.impl.StandardControllerImpl;
import perudo.controller.net.client.ControllerClientImpl;

public class ControllerSingleton {
    private static Controller SINGLETON = null;

    private ControllerSingleton() {};
    
    public static Controller getController() {
        return SINGLETON;
    }
    
    public static void setSingleplayerController() {
        SINGLETON = new StandardControllerImpl();
    }
    
    public static void setMultiplayerController(String ip, int port) {
        try {
            SINGLETON = ControllerClientImpl.createFromIPv4(ip,port);
        } catch (IOException e) {
            System.out.println("Server non raggiungibile...");
            System.exit(1);
        }
    }
}

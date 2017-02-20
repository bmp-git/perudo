package perudo.view.impl;

import perudo.controller.Controller;
import perudo.controller.impl.StandardControllerImpl;

public class ControllerSingleton {
    private static Controller SINGLETON = null;

    private ControllerSingleton() {};
    
    public static Controller getController() {
        return SINGLETON;
    }
    
    public static void setSingleplayerController() {
        SINGLETON = new StandardControllerImpl();
    }
    
    public static void setMultiplayerController() {
        SINGLETON = new StandardControllerImpl();
    }
}

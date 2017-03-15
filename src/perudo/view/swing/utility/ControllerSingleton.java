package perudo.view.swing.utility;

import java.io.IOException;

import perudo.controller.Controller;
import perudo.controller.impl.StandardControllerImpl;
import perudo.controller.net.client.ControllerClientImpl;

/**
 * Class containing a controller with Singleton pattern.
 */
public final class ControllerSingleton {
    private static Controller singleton;
    private static ControllerType controllerType;

    private ControllerSingleton() {
    };

    /**
     * Enumeration that define the type of controller.
     *
     */
    public enum ControllerType {
        /**
         * Define a singleplayer controller.
         */
        SINGLEPLAYER,
        /**
         * Define a multiplayer controller.
         */
        MULTIPLAYER;
    }

    /**
     * Get the controller initialized.
     * 
     * @return the controller istance
     */
    public static Controller getController() {
        return singleton;
    }

    /**
     * Get the controller initialized type.
     * 
     * @return the controller type
     */
    public static ControllerType getControllerType() {
        return controllerType;
    }

    /**
     * Set the controller with a StandardController.
     */
    public static void setSingleplayerController() {
        singleton = StandardControllerImpl.newStandardControllerImpl();
        controllerType = ControllerType.SINGLEPLAYER;
    }

    /**
     * Set the controller with a NetworkController.
     * 
     * @param ip
     *            server ip
     * @param port
     *            server executing port
     * @throws IOException
     *             if the connection is not possible
     * 
     */
    public static void setMultiplayerController(final String ip, final int port) throws IOException {
        singleton = ControllerClientImpl.createFromServerName(ip, port);
        controllerType = ControllerType.MULTIPLAYER;
    }
}

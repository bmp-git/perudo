package perudo.view.impl;

import perudo.view.GUIFactory;

/**
 * A singleton for a GUIFactory.
 */
public final class GUIFactorySingleton {

    private static class GUIFactoryHolder {
        private static final GUIFactory SINGLETON = new StandardGUIFactory();
    }

    private GUIFactorySingleton() {
    };

    /**
     * Get the factory from singleton. It is created at the first call.
     * 
     * @return the gui factory
     */
    public static GUIFactory getFactory() {
        return GUIFactoryHolder.SINGLETON;
    }
}

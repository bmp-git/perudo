package perudo.utility.impl;

import perudo.utility.Logger;

/**
 * A singleton Logger.
 *
 */
public final class LoggerSingleton implements Logger {
    private static class Holder {
        private static final LoggerSingleton SINGLETON = newLoggerSingleton();
        private static volatile boolean enabled;
    }

    private static LoggerSingleton newLoggerSingleton() {
        return new LoggerSingleton();
    }

    private LoggerSingleton() {
    };

    /**
     * Provides the logger singleton.
     * 
     * @return the logger singleton
     */
    public static LoggerSingleton get() {
        return Holder.SINGLETON;
    }

    @Override
    public void add(final Class<?> source, final String message) {
        if (Holder.enabled) {
            System.out.println(source.getSimpleName() + ": " + message);
        }
    }

    @Override
    public void enable() {
        Holder.enabled = true;
    }

    @Override
    public void disable() {
        Holder.enabled = false;
    }

}

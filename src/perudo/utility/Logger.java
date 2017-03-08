package perudo.utility;

/**
 * Defines a logger.
 */
public interface Logger {
    /**
     * Adds a message to log.
     * 
     * @param source
     *            the source class that generated the message
     * @param message
     *            the message to be logged
     */
    void add(Class<?> source, String message);

    /**
     * Enables the logger.
     */
    void enable();

    /**
     * Disables the logger.
     */
    void disable();
}

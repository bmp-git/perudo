package perudo.utility;

/**
 * Describes the severity of a log message.
 */
public enum LogSeverity {
    /**
     * Information message.
     */
    INFO("INFO"),
    /**
     * An expected error.
     */
    ERROR_REGULAR("ERROR_REGULAR"),
    /**
     * An unexpected error.
     */
    ERROR_UNEXPECTED("ERROR_UNEXPECTED");

    private final String describer;

    LogSeverity(final String message) {
        this.describer = message;
    }

    /**
     * Describes the severity.
     * 
     * @return a string that describes the severity
     */
    public String getDescriber() {
        return this.describer;
    }

}

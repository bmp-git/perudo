package perudo.utility;

/**
 * Exception that carry a ErrorType.
 */
public class ErrorTypeException extends Exception {

    private static final long serialVersionUID = -1977790415708164320L;

    private final ErrorType errorType;

    /**
     * Create the exception from ErrorType.
     * 
     * @param errorType
     *            The error type.
     */
    public ErrorTypeException(final ErrorType errorType) {
        super();
        this.errorType = errorType;
    }

    /**
     * Gets the ErrorType.
     * 
     * @return the ErrorType
     */
    public ErrorType getErrorType() {
        return this.errorType;
    }

}

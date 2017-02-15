package perudo.utility;

public class ErrorTypeException extends Exception {

    private static final long serialVersionUID = -1977790415708164320L;

    private final ErrorType errorType;

    public ErrorTypeException(ErrorType errorType) {
        super();
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return this.errorType;
    }

}

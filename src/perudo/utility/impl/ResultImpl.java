package perudo.utility.impl;

import java.util.Optional;

import perudo.utility.ErrorType;
import perudo.utility.Result;

/**
 * The implementation of Result interface.
 */
public class ResultImpl implements Result {

    private static final long serialVersionUID = -8127988248059956835L;
    private final boolean valid;
    private final ErrorType errorType;

    /**
     * Create a result with an error.
     * 
     * @param error
     *            the error of the result
     * 
     * @return the result created.
     */
    public static Result error(final ErrorType error) {
        return new ResultImpl(false, Optional.of(error));
    }

    /**
     * Create a valid result.
     * 
     * @return the result created.
     */
    public static Result ok() {
        return new ResultImpl(true, Optional.empty());
    }

    /**
     * Create a valid response with a value.
     * 
     * @param valid
     *            if the result is valid or not
     * 
     * @param errorType
     *            the error type
     * 
     */
    protected ResultImpl(final boolean valid, final Optional<ErrorType> errorType) {
        this.valid = valid;
        this.errorType = errorType.orElse(null);
    }

    /**
     * Checks if the result is valid.
     * 
     * @return true if valid, false otherwise.
     */
    public boolean isOk() {
        return valid;
    }

    /**
     * Gets the error type. throws IllegalStateException if isOK() is true.
     * 
     * @return the error type.
     */
    public ErrorType getErrorType() {
        if (isOk()) {
            throw new IllegalStateException();
        }
        return errorType;
    }
}

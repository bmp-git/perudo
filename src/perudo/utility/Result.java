package perudo.utility;

import java.io.Serializable;

/**
 * Represent a result yes/no, if the result is not ok this class carries an
 * ErrorType.
 */
public interface Result extends Serializable {

    /**
     * Check if the result is correct, if is false call getErrorType for more
     * information.
     * 
     * @return true if is correct, false otherwise
     */
    boolean isOk();

    /**
     * Return the error occurred.
     * 
     * @return return the type of error
     */
    ErrorType getErrorType();
}

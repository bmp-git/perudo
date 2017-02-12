package perudo.utility;

public interface Result {
    /**
     * Check if the result is correct, if is false call getErrorType for more information
     * @return
     *          true if is correct, false otherwise
     */
    public boolean isOk();

    /**
     * Return the error occured.
     * @return 
     *          return the type of error
     */
    public ErrorType getErrorType();
}

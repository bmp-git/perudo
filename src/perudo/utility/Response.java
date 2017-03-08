package perudo.utility;

/**
 * Represent a result yes/no, if the result is not ok this class carries an
 * ErrorType, if it is ok this class carries a T object.
 * 
 * @param <T>
 *            The value type
 */
public interface Response<T> extends Result {

    /**
     * The given value, throws an IllegalStatusException if isOk() is false.
     * 
     * @return return the optional value
     */
    T getValue();
}

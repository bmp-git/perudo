package perudo.utility.impl;

import java.util.Optional;
import perudo.utility.ErrorType;
import perudo.utility.Response;

/**
 * The implementation of Response interface.
 * 
 * @param <T>
 *            The value type
 */
public final class ResponseImpl<T> extends ResultImpl implements Response<T> {
    private static final long serialVersionUID = -4669470512321320418L;
    private final T value;

    /**
     * Create a valid response with a value.
     * 
     * @param value
     *            The object to add to the response
     * 
     * @param <T>
     *            The value type
     * 
     * @return the response created
     */
    public static <T> Response<T> of(final T value) {
        return new ResponseImpl<T>(true, Optional.empty(), value);
    }

    /**
     * Create a invalid response with an error.
     * 
     * @param error
     *            The error type
     * 
     * @param <T>
     *            The value type
     * 
     * @return the response created
     */
    public static <T> Response<T> empty(final ErrorType error) {
        return new ResponseImpl<T>(false, Optional.of(error), null);
    }

    private ResponseImpl(final boolean ok, final Optional<ErrorType> error, final T value) {
        super(ok, error);
        this.value = value;
    }

    @Override
    public T getValue() {
        if (!super.isOk()) {
            throw new IllegalStateException();
        }
        return value;
    }
}

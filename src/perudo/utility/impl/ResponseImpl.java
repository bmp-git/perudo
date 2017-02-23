package perudo.utility.impl;

import perudo.utility.ErrorType;
import perudo.utility.Response;

public class ResponseImpl<T> extends ResultImpl implements Response<T> {
    private static final long serialVersionUID = -4669470512321320418L;
    private final T value;

    private ResponseImpl(final boolean ok, final ErrorType error, final T value) {
        super(ok, error);
        this.value = value;
    }

    public T getValue() {
        if (!super.isOk()) {
            throw new IllegalStateException();
        }
        return value;
    }

    public static <T> Response<T> of(final T value) {
        return new ResponseImpl<T>(true, ErrorType.NONE, value);
    }

    public static <T> Response<T> empty(final ErrorType error) {
        return new ResponseImpl<T>(false, error, null);
    }
}

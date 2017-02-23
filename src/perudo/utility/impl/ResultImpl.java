package perudo.utility.impl;

import perudo.utility.ErrorType;
import perudo.utility.Result;

public class ResultImpl implements Result {
    private static final long serialVersionUID = -8127988248059956835L;
    private final boolean ok;
    private final ErrorType error;

    protected ResultImpl(final boolean ok, final ErrorType error) {
        this.ok = ok;
        this.error = error;
    }

    public boolean isOk() {
        return ok;
    }

    public ErrorType getErrorType() {
        if (isOk()) {
            throw new IllegalStateException();
        }
        return error;
    }

    public static Result error(final ErrorType error) {
        return new ResultImpl(false, error);
    }

    public static Result ok() {
        return new ResultImpl(true, ErrorType.NONE);
    }
}

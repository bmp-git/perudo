package perudo.utility;

public interface Response<T> extends Result {
    /**
     * The given value, throws an IllegalStatusException if isOk() is false
     * @return 
     *          return the optional value
     */
    public T getValue();
}

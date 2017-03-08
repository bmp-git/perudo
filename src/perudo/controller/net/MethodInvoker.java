package perudo.controller.net;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import perudo.utility.ErrorType;
import perudo.utility.ErrorTypeException;

/**
 * A Method invoker related to a specific class.
 */
public class MethodInvoker {

    private final Map<String, Method> indexer;

    /**
     * Creates a new MethodInvoker.
     * 
     * @param classType
     *            the class of the object on which the methods will be called.
     */
    public MethodInvoker(final Class<?> classType) {
        indexer = new ConcurrentHashMap<>();
        for (final Method m : classType.getMethods()) {
            final Optional<String> paramsName = Arrays.asList(m.getParameterTypes()).stream().map(t -> t.getName())
                    .reduce((t1, t2) -> t1 + ", " + t2);
            indexer.put(m.getName() + "(" + paramsName.orElse("") + ")", m);
        }
    }

    /**
     * Executes a method on an instance.
     * 
     * @param istance
     *            the instance on which the method will be called.
     * @param datagram
     *            the datagram that contains the method to call.
     * @throws ErrorTypeException
     *             if an error occurs during invocation.
     */
    public void execute(final Object istance, final Datagram datagram) throws ErrorTypeException {
        if (!indexer.containsKey(datagram.getMethodName())) {
            throw new ErrorTypeException(ErrorType.METHOD_NOT_FOUND);
        }
        try {
            indexer.get(datagram.getMethodName()).invoke(istance, datagram.getParams());
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new ErrorTypeException(ErrorType.METHOD_INVALID);
        }
    }
}

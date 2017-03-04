package perudo.controller.net;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * A Datagram to be sent over the network.
 *
 */
public class Datagram implements Serializable {

    private static final long serialVersionUID = -1108468855651132362L;

    /**
     * Creates a Datagram with the method name of that calls this function.
     * 
     * @param paramsType
     *            Types of the function's arguments
     * 
     * @param params
     *            Arguments of the functions
     * 
     * @return the created Datagram
     */
    public static Datagram createCurrentMethodDatagram(final List<Class<?>> paramsType,
            final List<Serializable> params) {
        return new Datagram(Thread.currentThread().getStackTrace()[2].getMethodName(), paramsType, params);
    }

    private final String methodName;
    private final List<Serializable> params;
    private final Instant creationTime;

    /**
     * Create a new datagram.
     * 
     * @param methodName
     *            the name of the method that needs to be invoked.
     * @param paramsType
     *            the types of the method parameters.
     * @param params
     *            the instances of the method parameters.
     */
    public Datagram(final String methodName, final List<Class<?>> paramsType, final List<Serializable> params) {
        Optional<String> paramsName = paramsType.stream().map(t -> t.getName()).reduce((t1, t2) -> t1 + ", " + t2);
        this.methodName = methodName + "(" + paramsName.orElse("") + ")";
        this.params = params;
        this.creationTime = Instant.now();
    }

    /**
     * Gets the name of the method contained in this datagram.
     * 
     * @return the method name.
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * Gets the parameters contained in this datagram.
     * 
     * @return the parameters instances.
     */
    public Object[] getParams() {
        return params.toArray();
    }

    /**
     * Gets the date of creation of this datagram.
     * 
     * @return the instant of creation.
     */
    public Instant getCreationTime() {
        return creationTime;
    }

}

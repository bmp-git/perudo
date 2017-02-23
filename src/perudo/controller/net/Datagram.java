package perudo.controller.net;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

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
    public static Datagram createCurrentMethodDatagram(List<Class<?>> paramsType, List<Serializable> params) {
        return new Datagram(Thread.currentThread().getStackTrace()[2].getMethodName(), paramsType, params);
    }

    private final String methodName;
    private final List<Serializable> params;

    public Datagram(String methodName, List<Class<?>> paramsType, List<Serializable> params) {
        Optional<String> paramsName = paramsType.stream().map(t -> t.getName()).reduce((t1, t2) -> t1 + ", " + t2);
        this.methodName = methodName + "(" + paramsName.orElse("") + ")";
        this.params = params;
    }

    public String getMethodName() {
        return methodName;
    }

    public Object[] getParams() {
        return params.toArray();
    }

}

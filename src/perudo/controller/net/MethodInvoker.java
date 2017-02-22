package perudo.controller.net;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import perudo.utility.ErrorType;
import perudo.utility.ErrorTypeException;

public class MethodInvoker {

	private final Map<String, Method> indexer;

	public MethodInvoker(final Class<?> classType) {

		indexer = new ConcurrentHashMap<>();
		for (Method m : classType.getMethods()) {
			Optional<String> paramsName = Arrays.asList(m.getParameterTypes()).stream().map(t -> t.getName())
					.reduce((t1, t2) -> t1 + ", " + t2);
			indexer.put(m.getName() + "(" + paramsName.orElse("") + ")", m);
		}

		System.out.println(indexer);
	}

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

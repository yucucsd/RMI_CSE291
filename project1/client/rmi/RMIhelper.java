package rmi;

import java.lang.reflect.Method;

public class RMIhelper {

	public RMIhelper() {
		// TODO Auto-generated constructor stub
	}
	public static <T> boolean isremoteinterface(Class<T> c) {

		if (c == null || !c.isInterface())
			return false;

		Method[] servermethods = c.getMethods();
		for (Method m : servermethods) {
			Class[] excs = m.getExceptionTypes();
			boolean findrmiexc = false;
			for (Class e : excs) {
				if (e.getName().contains("RMIException")) {
					findrmiexc = true;
					break;
				}
			}
			if (!findrmiexc)
				return false;
		}
		return true;
	}
}

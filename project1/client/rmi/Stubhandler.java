package rmi;

import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.*;
import java.lang.reflect.Proxy;

public class Stubhandler implements InvocationHandler, Serializable {

	private InetSocketAddress serveradr = null;
	private Class myinterface = null;

	public <T> Stubhandler(Class<T> c, InetSocketAddress serveradr) {
		this.myinterface = c;
		this.serveradr = serveradr;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// TODO Auto-generated method stub
		mypair res = null;
		try {

			String methodName = method.getName();
			// handle three special cases
			if (methodName.equals("toString") && method.getReturnType().getName().equals("java.lang.String")
					&& method.getParameterTypes().length == 0) {
				return this.serveradr.toString() + this.myinterface.toString();
			} else if (methodName.equals("hashCode") && method.getReturnType().getName().equals("int")
					&& method.getParameterTypes().length == 0) {
				return this.serveradr.hashCode();
			} else if (methodName.equals("equals") && method.getReturnType().getName().equals("boolean")
					&& method.getParameterTypes().length == 1
					&& method.getParameterTypes()[0].getName().equals("java.lang.Object")) {
				
				if (args == null || args[0] == null) {
					return false;
				}

				if (!Proxy.isProxyClass(args[0].getClass()))// args[0] not a //
															// proxy, false
					return false;
				Stubhandler that = (Stubhandler) Proxy.getInvocationHandler(args[0]);

				return ((this.getserveradr().equals(that.getserveradr()))
						&& (this.getmyinterface().equals(that.getmyinterface())));

			} else {// general cases, communicate with skeleton
				// System.out.println("serveradr host: " +
				// this.serveradr.getHostName());
				// System.out.println("serveradr port: " +
				// this.serveradr.getPort());

				Socket stubsocket = new Socket(this.serveradr.getHostName(), this.serveradr.getPort());
				OutputStream out = stubsocket.getOutputStream();
				// System.out.println("stubout is" + out);
				ObjectOutputStream stubout = new ObjectOutputStream(out);
				// System.out.println("pass out hhhhhhhhhhhhhh");
				stubout.flush();
				// System.out.println("pass out hhhhhhhhhhhhhh2");
				// System.out.println("method name: " + method.getName());
				stubout.writeObject(method.getName());
				// System.out.println("pass out hhhhhhhhhhhhhh3");
				stubout.writeObject(method.getParameterTypes());
				stubout.writeObject(method.getReturnType().getName());

				stubout.writeObject(args);
				stubout.flush();
				ObjectInputStream stubin = new ObjectInputStream(stubsocket.getInputStream());
				// System.out.println("before stub read obj");

				res = (mypair) stubin.readObject();// ? depends on the skeleton
													// side

				// System.out.println("after sbut read obj");
				stubout.close();
				stubin.close();
				//System.out.println("hererererererererere");
				stubsocket.close();
			}

		} catch (Exception e) {
			System.out.println("some IOexception in stubhandler invoke ");
			throw new RMIException(e.getCause());

		}

		if (res == null)
			return null;
		// System.out.println(res.flag);
		// System.out.println(res.result);
		if (!res.flag) {
			// System.out.println(res.flag);
			throw (Exception) res.result;
		}
		return res.result;

	}

	public InetSocketAddress getserveradr() {
		return this.serveradr;
	}

	public Class getmyinterface() {
		return this.myinterface;
	}

}

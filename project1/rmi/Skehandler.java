package rmi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.io.*;

public class Skehandler extends Thread {

	private Skeleton ske = null;// the skeleton related with this listener
								// thread
	private Socket csocket = null;// the client socket of the listener thread
									// related to this skehandler

	Skehandler(Skeleton ske, Socket csocket) {
		this.ske = ske;
		this.csocket = csocket;
	}

	public void run() {
		// System.out.println("in skehandler run " + this.ske.isrunning);

		boolean flag = true; // status variable to show type of exception or
								// success

		try {
			if (this.ske.isrunning) {// check if skeleton is still running
				// System.out.println("in skehand run try");

				ObjectInputStream skein = new ObjectInputStream(this.csocket.getInputStream());

				String methodname = (String) skein.readObject();// read in the
																// methodname
				Class[] paramTypes = (Class[]) skein.readObject();
				Object rettype = skein.readObject();
				Object[] args = (Object[]) skein.readObject();

				// Method m =
				// this.ske.getremoteinterface().getMethod(methodname,
				// paramTypes);

				mypair res = null;

				try {
					Method m = this.ske.getremoteinterface().getMethod(methodname, paramTypes);
					Object result = m.invoke(this.ske.getserver(), args);
					res = new mypair(true, result);

				} catch (IllegalAccessException e) {
					// System.out.println("111111111111111111111111111");
					res = new mypair(false, new RMIException(e.getCause()));

				} catch (IllegalArgumentException e) {
					// System.out.println("22222222222222222222222222");
					res = new mypair(false, new RMIException(e.getCause()));

				} catch (InvocationTargetException e) {
					// System.out.println("3333333333333333333333333333");
					res = new mypair(false, e.getCause());

				} catch (NoSuchMethodException e) {
					res = new mypair(false, new RMIException(e.getCause()));
				}

				OutputStream out = this.csocket.getOutputStream();
				ObjectOutputStream skeout = new ObjectOutputStream(out);
				skeout.flush();
				skeout.writeObject(res);// the flag

				skeout.flush();
				skein.close();
				skeout.close();
				this.csocket.close();// need to do this?
				// System.out.println("end of skehand run()");
			} else {
				this.csocket.close();// if the ske is already not running, just
										// close client socket
				// System.out.println("ske not running and client closed");
			}
		} catch (IOException e) {
			// System.out.println("11111111111111111111111111111");
			this.ske.service_error(new RMIException(e.getCause()));
		} catch (ClassNotFoundException e) {
			// System.out.println("22222222222222222222222222222");
			this.ske.service_error(new RMIException(e.getCause()));
		}

	}

}
